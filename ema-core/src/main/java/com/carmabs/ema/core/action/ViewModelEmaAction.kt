package com.carmabs.ema.core.action

interface ViewModelEmaAction : EmaAction {
    fun checkIsValidViewActionClass() = this is EmptyViewModelEmaAction || this::class.isSealed

    override val type: String
        get() = ViewModelEmaAction::class.java.simpleName
}

object EmptyViewModelEmaAction : ViewModelEmaAction