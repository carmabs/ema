package com.carmabs.ema.android.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.carmabs.ema.android.delegates.emaViewModelDelegate
import com.carmabs.ema.android.extension.addOnBackPressedListener
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.android.viewmodel.EmaFactory
import com.carmabs.ema.core.delegate.emaBooleanDelegate
import com.carmabs.ema.core.navigator.EmaNavigationState
import com.carmabs.ema.core.state.EmaBaseState
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.view.EmaView
import com.carmabs.ema.core.view.EmaViewModelTrigger
import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * Base fragment to bind and unbind view model
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
abstract class EmaFragment<S : EmaBaseState, VM : EmaViewModel<S, NS>, NS : EmaNavigationState> :
    EmaBaseFragment(), EmaAndroidView<S, VM, NS> {

    override val viewModelSeed: VM
        get() = androidViewModelSeed.emaViewModel

    private var viewJob: MutableList<Job>? = null

    private val extraViewJobs: MutableList<Job> by lazy {
        mutableListOf()
    }

    override val coroutineScope: CoroutineScope
        get() = lifecycleScope

    /**
     * Determines first execution for each one of the state methods. EmaView determines when to set it to false.
     */
    final override var isFirstNormalExecution: Boolean by emaBooleanDelegate(true)

    final override var isFirstAlternativeExecution: Boolean by emaBooleanDelegate(true)

    final override var isFirstErrorExecution: Boolean by emaBooleanDelegate(true)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        previousState = null
        isFirstNormalExecution = true
        isFirstAlternativeExecution = true
        isFirstErrorExecution = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addOnBackPressedListener {
            vm.onActionHardwareBackPressed()
        }
    }

    /**
     * The view model of the fragment
     */
    protected val vm: VM by emaViewModelDelegate()

    /**
     * The key id for incoming data through Bundle in fragment instantiation.This is set up when other fragment/activity
     * launches a fragment with arguments provided by Bundle
     */
    protected open val inputStateKey: String = EmaView.KEY_INPUT_STATE_DEFAULT


    /**
     * Trigger to start viewmodel only when startViewModel is launched
     */
    override val startTrigger: EmaViewModelTrigger? = null

    /**
     * Automatically updates previousState
     */
    override val updatePreviousStateAutomatically: Boolean = true

    /**
     * The incoming state in fragment instantiation. This is set up when other fragment/activity
     * launches a fragment with arguments provided by Bundle
     */
    override val inputState: S? by lazy { getInState() }

    /**
     * The list which handles the extra view models attached, to unbind the observers
     * when the view fragment is destroyed
     */
    private val extraViewModelList: MutableList<EmaAndroidViewModel<VM>> by lazy { mutableListOf() }


    /**
     * The view model is instantiated on fragment resume.
     */
    override fun onStart() {
        super.onStart()
        viewJob = onStartAndBindData(
            if (fragmentViewModelScope)
                this@EmaFragment
            else
                requireActivity(),
            vm
        )
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

    protected open fun provideToolbarTitle(): String? = null

    /**
     * Previous state for comparing state properties update
     */
    override var previousState: S? = null

    /**
     * Add a view model observer to current fragment
     * @param viewModelAttachedSeed is the view model seed will used as factory instance if there is no previous
     * view model retained by the OS
     * @param fragment the fragment scope
     * @param fragmentActivity the activity scope, if it is provided this will be the scope of the view model attached
     * @param observerFunction the observer of the view model attached
     * @return The view model attached
     */
    fun <AVM : EmaAndroidViewModel<out EmaViewModel<*,*>>> addExtraViewModel(
        viewModelAttachedSeed: AVM,
        fragment: Fragment,
        fragmentActivity: FragmentActivity? = null,
        observerFunction: ((attachedState: EmaState<*>) -> Unit)? = null
    ): AVM {
        val viewModel =
            fragmentActivity?.let {
                ViewModelProvider(
                    it,
                    EmaFactory(viewModelAttachedSeed)
                )[viewModelAttachedSeed::class.java]
            }
                ?: ViewModelProvider(
                    fragment,
                    EmaFactory(viewModelAttachedSeed)
                )[viewModelAttachedSeed::class.java]

        observerFunction?.also {
            val job = coroutineScope.launch {
                viewModel.emaViewModel.getObservableState().collect {
                    observerFunction.invoke(it)
                }
            }
            extraViewJobs.add(job)
        }
        extraViewModelList.add(viewModel as EmaAndroidViewModel<VM>)

        return viewModel
    }

    /**
     * Determine if the view model lifecycle is attached to the Activity or to the Fragment
     */
    abstract val fragmentViewModelScope: Boolean


    /**
     * Destroy the view and unbind the observers from view model
     */
    override fun onStop() {
        removeExtraViewModels()
        onStopBinding(vm, viewJob)
        super.onStop()
        Log.d("NAV","ONSTOP")
    }

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
     * Get the incoming state from another fragment/activity by the key [inputStateKey] provided
     */
    private fun getInState(): S? {
        return arguments?.let {
            if (it.containsKey(inputStateKey)) {
                it.get(inputStateKey) as? S

            } else
                null
        }
    }

    fun setInputState(inState: S) {
        arguments = Bundle().apply { putSerializable(inputStateKey, inState) }
    }
}