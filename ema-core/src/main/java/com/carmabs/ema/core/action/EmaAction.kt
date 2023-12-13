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
        object Empty : EmaAction
    }


    val type: String
        get() = type

    interface Initializer : EmaAction {
        override val type: String
            get() = Initializer::class.java.simpleName
    }

    sealed interface Lifecycle : EmaAction {
        object Started : Lifecycle
        object Resumed : Lifecycle
        object Paused : Lifecycle
        object Stopped : Lifecycle

    }

    interface Screen : EmaAction {
        fun checkIsValidScreenActionClass() = this is Empty || this::class.isSealed
        override val type: String
            get() = Screen::class.java.simpleName

        object Empty : Screen
    }
}



