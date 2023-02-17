package com.carmabs.ema.presentation.ui.home

import com.carmabs.ema.core.initializer.EmaInitializer

sealed class HomeInitializer : EmaInitializer {
    data class Default(val name:String,val surname:String):HomeInitializer()
}