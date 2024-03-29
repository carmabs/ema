package com.carmabs.ema.android.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.carmabs.ema.android.constants.EMA_RESULT_CODE
import com.carmabs.ema.android.constants.EMA_RESULT_KEY
import com.carmabs.ema.android.extension.addOnBackPressedListener
import com.carmabs.ema.android.extension.generateViewModel
import com.carmabs.ema.android.initializer.bundle.BundleSerializer
import com.carmabs.ema.android.initializer.bundle.strategy.BundleSerializerStrategy
import com.carmabs.ema.android.navigation.EmaActivityBackDelegate
import com.carmabs.ema.android.navigation.EmaActivityNavControllerNavigator
import com.carmabs.ema.android.navigation.EmaNavControllerNavigator
import com.carmabs.ema.android.ui.EmaAndroidView
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.android.viewmodel.EmaViewModelFactory
import com.carmabs.ema.core.constants.INT_ZERO
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.core.initializer.EmaInitializerSerializer
import com.carmabs.ema.core.model.EmaBackHandlerStrategy
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.core.view.EmaViewModelTrigger
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope

/**
 *
 * Abstract base class to implement Kodein framework in activity context
 * to handle dependency injection
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */
abstract class EmaCoreActivity<S : EmaDataState, VM : EmaViewModel<S, N>, N : EmaNavigationEvent> :
    AppCompatActivity(), EmaAndroidView<S, VM, N>, AndroidScopeComponent, EmaActivityBackDelegate {


    final override val scope: Scope by activityScope()


    override val ownsBackDelegate = false

    /**
     * The onCreate base will set the view specified in [.getLayout] and will
     * inject dependencies and views.
     *
     */
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        //Call scope to call scope to enable injection
        scope
        if (!ownsBackDelegate) {
            addOnBackPressedListener {
                onBackDelegate()
            }
        }

        onCreate(viewModel)
    }

    final override fun onBackDelegate(): EmaBackHandlerStrategy {
        viewModel.onActionBackHardwarePressed()
        //Cancel because we are handling manually the navigation with onActionBackHardwarePressed()
        return EmaBackHandlerStrategy.Cancelled
    }

    @CallSuper
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        (navigator as? EmaActivityNavControllerNavigator)?.setup(overrideDestinationInitializer(), initializerStrategy)
    }

    /**
     * Set up the up action navigation
     */
    override fun onSupportNavigateUp() =
        (navigator as? EmaNavControllerNavigator)?.navController?.navigateUp() ?: false

    protected open fun overrideDestinationInitializer(): EmaInitializer? = null

    abstract fun provideViewModel(): VM

    override val coroutineScope: CoroutineScope
        get() = lifecycleScope


    @Suppress("UNCHECKED_CAST")
    override val viewModel: VM by lazy {
        generateViewModel(provideViewModel()).emaViewModel as VM
    }


    private var viewJob: MutableList<Job>? = null

    /**
     * The map which handles the view model attached with their respective scopes, to unbind the observers
     * when the view activity is destroyed
     */
    private val extraViewModelList: MutableList<EmaViewModel<S, N>> by lazy { mutableListOf() }

    private val extraViewJobs: MutableList<Job> by lazy {
        mutableListOf()
    }

    /**
     * Trigger to start viewmodel only when startViewModel is launched
     */
    override val startTrigger: EmaViewModelTrigger? = null

    /**
     * The incoming initializer in activity instantiation. This is set up when other fragment/activity
     * launches an activity with arguments provided by Bundle
     */
    final override val initializerSerializer: EmaInitializerSerializer?
        get() = intent?.let {
            it.extras?.let { bundle ->
                BundleSerializer(bundle, initializerStrategy)
            }
        }

    abstract val initializerStrategy: BundleSerializerStrategy

    /**
     * Initialize ViewModel on activity creation
     */
    @CallSuper
    override fun onStart() {
        onStartView(viewModel)
        super.onStart()
    }

    /**
     * Notifies the view model that view has been gone to foreground.
     */
    @CallSuper
    override fun onResume() {

        //We call this and not in onStart, because is some cases, the view that receives the state could be use a
        //dialog, that needs to be restored after onRestoreInstance state is called. Otherwise, java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        //could be launched.
        //On restoreInstanceState is called between onStart and onResume, on re-initialization, so binding the views here, guarantees the state of
        //savedInstances has been restored
        if (viewJob == null) {
            viewJob = onBindView(this.lifecycleScope, viewModel)
        }
        super.onResume()
        onResumeView(viewModel)
    }

    /**
     * Notifies the view model that view has been gone to background.
     */
    @CallSuper
    override fun onPause() {
        onPauseView(viewModel)
        super.onPause()
    }

    /**
     * Previous state for comparing state properties update
     */
    final override var previousEmaState: EmaState<S, N>? = null


    /**
     * Add a view model observer to current fragment
     * @param viewModelAttachedSeed is the view model seed will used as factory instance if there is no previous
     * view model retained by the OS
     * @param fragment the fragment scope
     * @param observerFunction the observer of the view model attached
     * @return The view model attached
     */
    @Suppress("UNCHECKED_CAST")
    fun <AVM : EmaViewModel<S, N>> addExtraViewModel(
        viewModelAttachedSeed: AVM,
        fragment: Fragment? = null,
        observerFunction: ((attachedState: EmaState<*, *>) -> Unit)? = null
    ): AVM {

        val viewModel =
            fragment?.let {
                ViewModelProvider(
                    it,
                    EmaViewModelFactory(viewModelAttachedSeed)
                )[viewModelAttachedSeed.id, EmaAndroidViewModel::class.java]
            }
                ?: ViewModelProvider(
                    this,
                    EmaViewModelFactory(viewModelAttachedSeed)
                )[viewModelAttachedSeed.id, EmaAndroidViewModel::class.java]

        observerFunction?.also {
            extraViewJobs.add(coroutineScope.launch {
                viewModel.emaViewModel.subscribeStateUpdates().collect {
                    observerFunction.invoke(it)
                }
            }
            )
        }
        extraViewModelList.add(viewModel.emaViewModel as EmaViewModel<S, N>)

        return viewModel.emaViewModel as AVM
    }


    /**
     * Destroy the activity and unbind the observers from view model
     */
    @CallSuper
    override fun onStop() {
        removeExtraViewModels()
        onUnbindView(viewJob, viewModel)
        viewJob = null
        super.onStop()
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


    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        scope.close()
    }

    @CallSuper
    override fun finish() {
        super.finish()
        overridePopTransitionAnimations()?.also {
            overridePendingTransition(it.enterTransition ?: INT_ZERO, it.exitTransition ?: INT_ZERO)
        }
    }


    override fun onEmaStateOverlapped(extraData: EmaExtraData) = Unit

    override fun onSingleEvent(extra: EmaExtraData) = Unit

    final override fun onBack(result: Any?): Boolean {
        result?.also {
            setResult(EMA_RESULT_CODE, Intent().putExtra(EMA_RESULT_KEY, Gson().toJson(result)))
        }
        finish()
        return false
    }

    //Override it to make it final to avoid deprecated implementation
    @Suppress("DEPRECATION")
    @Deprecated(
        "Overrides deprecated member in 'androidx.core.app.ComponentActivity'. Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    final override fun onBackPressed() {
        super.onBackPressed()
        overridePopTransitionAnimations()?.also {
            overridePendingTransition(it.enterTransition ?: INT_ZERO, it.exitTransition ?: INT_ZERO)
        }
    }

    protected open fun overridePopTransitionAnimations(): EmaPopActivityTransitionAnimations? = null


    protected data class EmaPopActivityTransitionAnimations(
        @AnimRes val enterTransition: Int?,
        @AnimRes val exitTransition: Int?
    )
}