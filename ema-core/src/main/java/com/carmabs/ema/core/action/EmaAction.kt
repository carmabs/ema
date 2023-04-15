package com.carmabs.ema.core.action

interface EmaAction{

    fun checkIsValidActionClass() = this is EmaActionEmpty || this::class.isSealed
}