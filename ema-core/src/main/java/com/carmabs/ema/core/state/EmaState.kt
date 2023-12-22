package com.carmabs.ema.core.state

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
sealed class EmaState<T> private constructor(open val data: T) : EmaDataState {

    /**
     * State that represents the current state of a view.
     * @constructor T is the state model of the view
     */
    data class Normal<T>(override val data: T) : EmaState<T>(data = data)

    /**
     * State that represents an overlapped state of a view.
     * @constructor T is the state model of the view, data represents the current state of the view, dataOverlated represents extra data to handle the overlapped state
     */
    data class Overlapped<T>(override val data: T, val extraData: EmaExtraData = EmaExtraData()) : EmaState<T>(data)

    fun update( updateAction: @EmaStateDsl T.()-> T):EmaState<T>{
        return when(this){
            is Normal -> Normal(data.updateAction())
            is Overlapped -> Overlapped(data.updateAction(),extraData)
        }
    }

    fun normal(updateAction: @EmaStateDsl T.()-> T):EmaState<T>{
        return Normal(data.updateAction())
    }

    fun normal():EmaState<T>{
        return Normal(data)
    }

    fun overlapped(extraData: EmaExtraData = EmaExtraData()):EmaState<T>{
        return Overlapped(this.data,extraData)
    }
}

