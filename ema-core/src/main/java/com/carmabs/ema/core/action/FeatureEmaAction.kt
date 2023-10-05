package com.carmabs.ema.core.action

interface FeatureEmaAction : EmaAction{
        fun checkIsValidActionClass() = this is EmaEmptyAction || this::class.isSealed
    }