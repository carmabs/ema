package com.carmabs.ema.android.viewmodel

import com.carmabs.ema.android.extra.EmaReceiverModel
import com.carmabs.ema.android.extra.EmaResultModel
import com.carmabs.ema.android.ui.EmaResultViewModel
import com.carmabs.ema.core.navigator.EmaNavigationState
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import java.io.Serializable

/**
 * View model to handle view states.
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a>
 */
abstract class EmaViewModel<S, NS : EmaNavigationState> : EmaBaseViewModel<EmaState<S>, NS>() {

    /**
     * State of the view
     */
    private var viewState: S? = null

    internal lateinit var resultViewModel: EmaResultViewModel

    override fun onStart(inputState: EmaState<S>?): Boolean {
        if (viewState == null)
            inputState?.let { viewState = it.data }
        onResultListenerSetup()
        return super.onStart(inputState)
    }

    /**
     * Here should implement the listener for result data from other views through [addOnResultReceived] method
     */
    protected open fun onResultListenerSetup() {
        //Calls to [addOnResultReceived] if they are needed
    }

    /**
     * Update the current state and update the view by default
     * @param notifyView updates the view
     * @param changeStateFunction create the new state
     */
    protected fun updateViewState(notifyView: Boolean = true, changeStateFunction: S.() -> S) {
        viewState?.let {
            viewState = changeStateFunction.invoke(it)
            viewState?.let { newState -> state = EmaState.Normal(newState) }

            if (notifyView) updateViewState()
        }

    }

    /**
     * Used for trigger an update on the view
     * Use the EmaState -> Normal
     * @param state of the view
     */
    protected fun updateViewState() {
        state?.let {
            viewState?.let { currentState ->
                super.updateView(EmaState.Normal(currentState))
            }
        }
    }

    /**
     * Check the current view state
     * @param checkStateFunction function to check the current state
     * @return the value returned by [checkStateFunction]
     */
    fun <T> checkViewState(checkStateFunction: (S) -> T): T {
        return viewState?.let {
            checkStateFunction.invoke(it)
        } ?: let {
            val initialState = initialViewState
            viewState = initialState
            checkStateFunction.invoke(initialState)
        }
    }

    /**
     * Used for trigger an error on the view
     * Use the EmaState -> Error
     * @param error generated
     */
    protected fun notifyError(error: Throwable) {
        viewState?.let {
            super.updateView(EmaState.Error(it, error))
        } ?: throwInitialStateException()

    }

    /**
     * Used for trigger a loading event on the view
     * Use the EmaState -> Loading
     * @param data with loading information
     */
    protected fun loading(data: EmaExtraData? = null) {
        viewState?.let { state ->
            val loadingData: EmaState.Loading<S> = data?.let {
                EmaState.Loading(state, dataLoading = it)
            } ?: EmaState.Loading(state)

            super.updateView(loadingData)
        } ?: throwInitialStateException()

    }

    /**
     * Generate the initial state with EmaState to trigger normal/loading/error states
     * for the view.
     */
    final override fun createInitialState(): EmaState<S> {
        if (viewState == null) {
            viewState = initialViewState
        }

        return EmaState.Normal(viewState!!)
    }

    /**
     * Throws exception if the state of the view has not been initialized
     */
    private fun throwInitialStateException(): Exception {
        throw RuntimeException("Initial state has not been created")
    }

    /**
     * Generate the initial state of the view
     */
    abstract val initialViewState: S

    /**
     * Set a result for previous view when the current one is destroyed
     */
    protected fun addResult(data: Serializable,code: Int = EmaResultViewModel.RESULT_ID_DEFAULT) {
        resultViewModel.addResult(
                EmaResultModel(
                        id = code,
                        ownerId = getId(),
                        data = data))
    }

    /**
     * Set the listener for back data when the result view is destroyed
     */
    protected fun addOnResultReceived(code: Int = EmaResultViewModel.RESULT_ID_DEFAULT, receiver: (EmaResultModel) -> Unit) {
        val emaReceiver = EmaReceiverModel(
                ownerCode = getId(),
                resultId = code,
                function = receiver
        )
        resultViewModel.addResultReceiver(emaReceiver)
    }

    override fun onCleared() {
        super.onCleared()
        resultViewModel.notifyResults(getId())
    }

    fun getId():Int{
        return this.javaClass.name.hashCode()
    }
}