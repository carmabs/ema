package com.carmabs.ema.core.state

import com.carmabs.ema.core.model.EmaEvent
import com.carmabs.ema.core.navigator.EmaNavigationDirection
import com.carmabs.ema.core.navigator.EmaNavigationDirectionEvent
import com.carmabs.ema.core.navigator.EmaNavigationEvent

/**
 * Class to represent basic view states
 *
 * @author <a href="mailto:apps.carmabs@gmail.com">Carlos Mateo Benito</a> *
 * @constructor T is the state model of the view, data represents the current state of the view
 */

@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
annotation class EmaStateDsl

@EmaStateDsl
sealed class EmaState<T : EmaDataState, N : EmaNavigationEvent> private constructor(
    open val data: T,
    open val navigation: EmaNavigationDirectionEvent = EmaNavigationDirectionEvent.None,
    open val result: Any? = null,
    open val singleEvent: EmaEvent = EmaEvent.Consumed
) : EmaDataState {

    /**
     * State that represents the current state of a view.
     * @constructor T is the state model of the view
     */
    data class Normal<T : EmaDataState, N : EmaNavigationEvent>(
        override val data: T,
        override val navigation: EmaNavigationDirectionEvent = EmaNavigationDirectionEvent.None,
        override val result: Any? = null,
        override val singleEvent: EmaEvent = EmaEvent.Consumed
    ) : EmaState<T, N>(
        data = data,
        navigation = navigation,
        result = result,
        singleEvent = singleEvent
    )

    /**
     * State that represents an overlapped state of a view.
     * @constructor T is the state model of the view, data represents the current state of the view, dataOverlated represents extra data to handle the overlapped state
     */
    data class Overlapped<T : EmaDataState, N : EmaNavigationEvent>(
        override val data: T,
        override val navigation: EmaNavigationDirectionEvent = EmaNavigationDirectionEvent.None,
        val extraData: EmaExtraData = EmaExtraData(),
        override val result: Any? = null,
        override val singleEvent: EmaEvent = EmaEvent.Consumed
    ) : EmaState<T, N>(
        data = data,
        navigation = navigation,
        result = result,
        singleEvent = singleEvent
    )

    fun update(updateAction: T.() -> T): EmaState<T, N> {
        return when (this) {
            is Normal -> copy(data.updateAction())
            is Overlapped -> copy(data.updateAction())
        }
    }

    fun normal(updateAction: T.() -> T): Normal<T, N> {
        return Normal(data.updateAction(), navigation, result)
    }

    fun normal(): Normal<T, N> {
        return Normal(data, navigation, result)
    }

    fun overlapped(extraData: EmaExtraData = EmaExtraData()): Overlapped<T, N> {
        return Overlapped(this.data, navigation, extraData, result)
    }

    fun navigate(navigationEvent: N): EmaState<T, N> {
        return when (this) {
            is Normal -> copy(
                navigation = EmaNavigationDirectionEvent.Launched(
                    EmaNavigationDirection.Forward(navigationEvent)
                )
            )

            is Overlapped -> copy(
                navigation = EmaNavigationDirectionEvent.Launched(
                    EmaNavigationDirection.Forward(navigationEvent)
                )
            )
        }
    }

    fun setResult(result: Any): EmaState<T, N> {
        return when (this) {
            is Normal -> copy(result = result)
            is Overlapped -> copy(result = result)
        }
    }

    fun clearResult(): EmaState<T, N> {
        return when (this) {
            is Normal -> copy(result = null)
            is Overlapped -> copy(result = null)
        }
    }

    fun singleEvent(extraData: EmaExtraData = EmaExtraData()): EmaState<T, N> {
        return when (this) {
            is Normal -> copy(singleEvent = EmaEvent.Launched(extraData))
            is Overlapped -> copy(singleEvent = EmaEvent.Launched(extraData))
        }
    }

    internal fun onNavigated(): EmaState<T, N> {
        return when (this) {
            is Normal -> copy(navigation = EmaNavigationDirectionEvent.OnNavigated)
            is Overlapped -> copy(navigation = EmaNavigationDirectionEvent.OnNavigated)
        }
    }

    internal fun consumeSingleEvent(): EmaState<T, N> {
        return when (this) {
            is Normal -> copy(singleEvent = EmaEvent.Consumed)
            is Overlapped -> copy(singleEvent = EmaEvent.Consumed)
        }
    }

    fun navigateBack(): EmaState<T, N> {
        return when (this) {
            is Normal -> copy(navigation = EmaNavigationDirectionEvent.Launched(EmaNavigationDirection.Back(result)))
            is Overlapped -> copy(navigation = EmaNavigationDirectionEvent.Launched(EmaNavigationDirection.Back(result)))

        }
    }
}

