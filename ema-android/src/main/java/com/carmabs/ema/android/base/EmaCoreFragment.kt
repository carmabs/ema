package com.carmabs.ema.android.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.carmabs.ema.android.constants.EMA_RESULT_CODE
import com.carmabs.ema.android.constants.EMA_RESULT_KEY
import com.carmabs.ema.android.extension.generateViewModel
import com.carmabs.ema.android.extension.addOnBackPressedListener
import com.carmabs.ema.android.extension.getInitializer
import com.carmabs.ema.android.navigation.EmaActivityBackDelegate
import com.carmabs.ema.android.ui.EmaAndroidView
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.android.viewmodel.EmaViewModelFactory
import com.carmabs.ema.core.constants.INT_ZERO
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.model.EmaBackHandlerStrategy
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.view.EmaViewModelTrigger
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.core.scope.Scope


/**
 *
 * Abstract base class to implement Fragment with binding to ViewModel and Koin
 * fragment scope
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */
abstract class EmaCoreFragment<S : EmaDataState, VM : EmaViewModel<S, N>, N : EmaNavigationEvent> :
    Fragment(), EmaAndroidView<S, VM, N>, AndroidScopeComponent {

    final override val scope: Scope by fragmentScope()

    open val handleBackPressedManually = false

    val viewScope: CoroutineScope
        get() = viewLifecycleOwner.lifecycleScope

    private var viewJob: MutableList<Job>? = null

    private val extraViewJobs: MutableList<Job> by lazy {
        mutableListOf()
    }

    override val viewModel: VM by lazy {
        generateViewModel(provideViewModel())
    }
    /**
     * Trigger to start viewmodel only when startViewModel is launched
     */
    override val startTrigger: EmaViewModelTrigger? = null

    /**
     * The incoming initializer in fragment instantiation. This is set up when other fragment/activity
     * launches a fragment with arguments provided by Bundle
     */
    override val initializer: EmaInitializer? by lazy { getInitializer() }

    /**
     * The list which handles the extra view models attached, to unbind the observers
     * when the view fragment is destroyed
     */
    private val extraViewModelList: MutableList<EmaAndroidViewModel<S, N>> by lazy { mutableListOf() }

    protected open fun provideToolbarTitle(): String? = null

    /**
     * Previous state for comparing state properties update
     */
    final override var previousEmaState: EmaState<S, N>? = null

    abstract override val navigator: EmaNavigator<N>?

    abstract fun provideViewModel(): VM


    override val coroutineScope: CoroutineScope
        get() = lifecycleScope

    /**
     * Remove extra view models attached
     */
    private fun removeExtraViewModels() {
        extraViewJobs.forEach {
            it.cancel()
        }
        extraViewJobs.clear()
        extraViewModelList.clear()
    }

    /**
     * Get the scope of the fragment depending the viewModelScopeSelected
     */
    protected fun getScope(): CoroutineScope {
        return if (fragmentViewModelScope)
            coroutineScope
        else
            requireActivity().lifecycleScope
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        previousEmaState = null
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!handleBackPressedManually)
            addOnBackPressedListener {
                val parentActivity = requireActivity()
                if( parentActivity is EmaActivityBackDelegate){
                    if(parentActivity.ownsBackDelegate){
                        parentActivity.onBackDelegate()
                    }else{
                        viewModel.onActionBackHardwarePressed()
                        //Cancel because we are handling manually the navigation with onActionBackHardwarePressed()
                        EmaBackHandlerStrategy.Cancelled
                    }

                }
                else{
                    viewModel.onActionBackHardwarePressed()
                    //Cancel because we are handling manually the navigation with onActionBackHardwarePressed()
                    EmaBackHandlerStrategy.Cancelled
                }

            }
        onCreate(viewModel)
    }


    /**
     * The view model is instantiated on fragment resume.
     */
    @CallSuper
    override fun onStart() {
        super.onStart()
        onStartView(viewModel)
    }

    /**
     * Notifies the view model that view has been gone to foreground.
     */
    @CallSuper
    override fun onResume() {
        //It will set the last value of normalContentData, and avoid to delivered the last value of flow if it has not be updated due to
        //updateToDataState
        //We call this and not in onStart, because is some cases, the view that receives the state could be use a
        //dialog, that needs to be restored after onRestoreInstance state is called. Otherwise, java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        //could be launched.
        //On restoreInstanceState is called between onStart and onResume, on re-initialization, so binding the views here, guarantees the state of
        //savedInstances has been restored
        if (viewJob == null) {
            viewJob = onBindView(getScope(), viewModel)
        }
        super.onResume()
        onResumeView(viewModel)
    }

    /**
     * Notifies the view model that view has been gone to background.
     */
    @CallSuper
    override fun onPause() {
        super.onPause()
        onPauseView(viewModel)
    }

    /**
     * Add a view model observer to current fragment
     * @param viewModelAttachedSeed is the view model seed will used as factory instance if there is no previous
     * view model retained by the OS
     * @param fragment the fragment scope
     * @param fragmentActivity the activity scope, if it is provided this will be the scope of the view model attached
     * @param observerFunction the observer of the view model attached
     * @return The view model attached
     */
    fun <AVM : EmaViewModel<S, N>> addExtraViewModel(
        viewModelAttachedSeed: AVM,
        fragment: Fragment,
        fragmentActivity: FragmentActivity? = null,
        observerFunction: ((attachedState: EmaState<S, N>) -> Unit)? = null
    ): AVM {
        val viewModel =
            fragmentActivity?.let {
                ViewModelProvider(
                    it,
                    EmaViewModelFactory(viewModelAttachedSeed)
                )[viewModelAttachedSeed.id, EmaAndroidViewModel::class.java]
            }
                ?: ViewModelProvider(
                    fragment,
                    EmaViewModelFactory(viewModelAttachedSeed)
                )[viewModelAttachedSeed.id, EmaAndroidViewModel::class.java]

        observerFunction?.also {
            val job = coroutineScope.launch {
                viewModel.emaViewModel.subscribeStateUpdates().collect {
                    observerFunction.invoke(it as EmaState<S, N>)
                }
            }
            extraViewJobs.add(job)
        }
        extraViewModelList.add(viewModel as EmaAndroidViewModel<S, N>)

        return viewModel.emaViewModel as AVM
    }

    /**
     * Determine if the view model lifecycle is attached to the Activity or to the Fragment
     */
    open val fragmentViewModelScope: Boolean = true

    @CallSuper
    override fun onStop() {
        super.onStop()
        onUnbindView(viewJob, viewModel)
        viewJob = null
        removeExtraViewModels()
    }

    @CallSuper
    override fun onDestroyView() {
        previousEmaState = null
        super.onDestroyView()
    }


    @CallSuper
    override fun onNavigation(navigation: EmaNavigationDirectionEvent) {
        super.onNavigation(navigation)
    }


    final override fun onBack(result: Any?): Boolean {
        val gson = Gson()
        val hasMoreFragments = kotlin.runCatching {
            findNavController().popBackStack()
        }.getOrNull() ?: let {
            val hasMoreFragments = parentFragmentManager.backStackEntryCount > INT_ZERO
            result?.also {
                setFragmentResult(EMA_RESULT_KEY, Bundle().apply {
                    putString(EMA_RESULT_KEY, gson.toJson(it))
                })
            }
            if (hasMoreFragments)
                parentFragmentManager.popBackStack()
            hasMoreFragments
        }

        if (!hasMoreFragments) {
            result?.also {
                requireActivity().setResult(
                    EMA_RESULT_CODE,
                    Intent().putExtra(EMA_RESULT_KEY, gson.toJson(it))
                )
            }
            requireActivity().finish()
        }
        return hasMoreFragments
    }
}