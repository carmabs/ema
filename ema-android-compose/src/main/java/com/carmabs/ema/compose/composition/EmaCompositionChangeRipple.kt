package com.carmabs.ema.compose.composition

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

/**
 * Created by Carlos Mateo Benito on 15/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
@Composable
fun EmaCompositionChangeRipple(defaultColor: Color, rippleAlpha:Color = Color.Transparent, content:@Composable ()->Unit) {
    CompositionLocalProvider(
        LocalRippleTheme provides CustomRippleTheme(
            defaultColor = defaultColor,
            rippleAlphaColor = rippleAlpha
        ),
        content = content
    )
}

private class CustomRippleTheme(val defaultColor: Color,val rippleAlphaColor:Color = Color.Transparent) : RippleTheme {
    // Here you should return the ripple color you want
    // and not use the defaultRippleColor extension on RippleTheme.
    // Using that will override the ripple color set in DarkMode
    // or when you set light parameter to false
    @Composable
    override fun defaultColor(): Color = defaultColor

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        rippleAlphaColor,
        lightTheme = !isSystemInDarkTheme()
    )
}