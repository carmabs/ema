package com.carmabs.ema.core.action

/**
 * Created by Carlos Mateo Benito on 1/10/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
interface EmaAction {

    companion object {
        val type: String = EmaAction::class.java.simpleName
        data object EMPTY : EmaAction
    }


    val type: String
        get() = type

    interface Initializer : EmaAction {
        override val type: String
            get() = Initializer::class.java.simpleName
    }

    sealed interface Lifecycle : EmaAction {
        data object Started : Lifecycle
        data object Resumed : Lifecycle
        data object Paused : Lifecycle
        data object Stopped : Lifecycle

    }

    sealed interface ViewModel: EmaAction{
        data object ConsumeSingleEvent:ViewModel

        data object NavigationBack:ViewModel

        data object OnNavigated:ViewModel

    }

    interface Screen : EmaAction {
        fun checkIsValidScreenActionClass() = this is EMPTY || this::class.isSealed
        override val type: String
            get() = Screen::class.java.simpleName

        object EMPTY : Screen
    }
}



