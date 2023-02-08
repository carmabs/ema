package com.carmabs.ema.core.state

sealed interface EmaStateTransition {
    data class NormalToOverlayed<S>(
        val previousNormalState: S,
        val currentOverlayedState: EmaExtraData) : EmaStateTransition

    data class OverlayedToNormal<S>(
        val previousOverlayedState: EmaExtraData,
        val currentNormalState: S) : EmaStateTransition
}