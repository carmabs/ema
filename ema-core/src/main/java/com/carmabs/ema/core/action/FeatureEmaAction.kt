package com.carmabs.ema.core.action

interface FeatureEmaAction : EmaAction{
        fun checkIsValidActionClass() = this is EmaActionEmpty || this::class.isSealed
    }