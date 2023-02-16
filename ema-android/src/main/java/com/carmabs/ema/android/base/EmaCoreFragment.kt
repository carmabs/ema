package com.carmabs.ema.android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.carmabs.ema.android.delegates.emaViewModelDelegate
import com.carmabs.ema.android.extension.addOnBackPressedListener
import com.carmabs.ema.android.extension.getSerializableCompat
import com.carmabs.ema.android.ui.EmaAndroidView
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.android.viewmodel.EmaViewModelFactory
import com.carmabs.ema.core.constants.INT_ZERO
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.navigator.EmaDestination
import com.carmabs.ema.core.navigator.EmaNavigator
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.view.EmaViewModelTrigger
import com.carmabs.ema.core.viewmodel.EmaViewModel
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
abstract class EmaCoreFragment<S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaDestination> :
    Fragment(), EmaAndroidView<S, VM, D>, AndroidScopeComponent {

    final override val scope: Scope by fragmentScope()

    private var viewJob: MutableList<Job>? = null

    private val extraViewJobs: MutableList<Job> by lazy {
        mutableListOf()
    }

    /**
     * The view model of the fragment
     */
    protected val vm: VM by emaViewModelDelegate()

    /**
     * Trigger to start viewmodel only when startViewModel is launched
     */
    override val startTrigger: EmaViewModelTrigger? = null

    /**
     * The incoming initializer in fragment instantiation. This is set up when other fragment/activity
     * launches a fragment with arguments provided by Bundle
     */
    override val initializer: EmaInitializer? by lazy { getInitializerArgument() }

    /**
     * The list which handles the extra view models attached, to unbind the observers
     * when the view fragment is destroyed
     */
    private val extraViewModelList: MutableList<EmaAndroidViewModel> by lazy { mutableListOf() }

    protected open fun provideToolbarTitle(): String? = null

    /**
     * Previous state for comparing state properties update
     */
    final override var previousEmaState: EmaState<S>? = null

    abstract override val navigator: EmaNavigator<D>?

    abstract fun provideAndroidViewModel(): EmaAndroidViewModel


    final override val androidViewModelSeed: EmaAndroidViewModel by lazy {
        provideAndroidViewModel()
    }

    final override val viewModelSeed: VM
        get() = androidViewModelSeed.emaViewModel as VM

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


    fun setInitializer(initializer: EmaInitializer) {
        arguments = Bundle().apply { putSerializable(EmaInitializer.KEY, initializer) }
    }

    /**
     * Get the incoming initializer from another fragment/activity
     */
    private fun getInitializerArgument(): EmaInitializer? {
        return arguments?.let {
            if (it.containsKey(EmaInitializer.KEY)) {
                it.getSerializableCompat(EmaInitializer.KEY)
            } else
                null
        }
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
        vm.onBackHardwarePressedListener?.also {
            addOnBackPressedListener(it)
        }

    }


    /**
     * The view model is instantiated on fragment resume.
     */
    override fun onStart() {
        super.onStart()
        viewJob = onBindView(
            getScope(),
            vm
        )
        onStartView(vm)
    }

    /**
     * Notifies the view model that view has been gone to foreground.
     */
    @CallSuper
    override fun onResume() {
        super.onResume()
        onResumeView(vm)
    }

    /**
     * Notifies the view model that view has been gone to background.
     */
    @CallSuper
    override fun onPause() {
        onPauseView(vm)
        super.onPause()
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
    fun <AVM : EmaAndroidViewModel> addExtraViewModel(
        viewModelAttachedSeed: AVM,
        fragment: Fragment,
        fragmentActivity: FragmentActivity? = null,
        observerFunction: ((attachedState: EmaState<*>) -> Unit)? = null
    ): AVM {
        val viewModel =
            fragmentActivity?.let {
                ViewModelProvider(
                    it,
                    EmaViewModelFactory(viewModelAttachedSeed)
                )[viewModelAttachedSeed::class.java]
            }
                ?: ViewModelProvider(
                    fragment,
                    EmaViewModelFactory(viewModelAttachedSeed)
                )[viewModelAttachedSeed::class.java]

        observerFunction?.also {
            val job = coroutineScope.launch {
                viewModel.emaViewModel.getObservableState().collect {
                    observerFunction.invoke(it)
                }
            }
            extraViewJobs.add(job)
        }
        extraViewModelList.add(viewModel as EmaAndroidViewModel)

        return viewModel
    }

    /**
     * Determine if the view model lifecycle is attached to the Activity or to the Fragment
     */
    open val fragmentViewModelScope: Boolean = true

    @CallSuper
    override fun onStop() {
        super.onStop()
        onUnbindView(viewJob, vm)
        removeExtraViewModels()
    }

    @CallSuper
    override fun onDestroyView() {
        previousEmaState = null
        super.onDestroyView()
    }


    @CallSuper
    override fun onNavigation(navigation: D?) {
        super.onNavigation(navigation)
    }


    final override fun onBack(): Boolean {
        val hasMoreFragments = kotlin.runCatching {
            findNavController().popBackStack()
        }.getOrNull() ?: let {
            val hasMoreFragments = parentFragmentManager.backStackEntryCount > INT_ZERO
            if (hasMoreFragments)
                parentFragmentManager.popBackStack()
            hasMoreFragments
        }

        if (!hasMoreFragments)
            requireActivity().finish()
        return hasMoreFragments
    }
}