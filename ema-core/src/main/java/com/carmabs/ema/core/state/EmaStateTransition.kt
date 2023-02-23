package com.carmabs.ema.core.state

sealed interface EmaStateTransition {
    data class NormalToOverlapped<S>(
        val previousNormalState: S,
        val currentOverlayedState: EmaExtraData) : EmaStateTransition

    data class OverlappedToNormal<S>(
        val previousOverlayedState: EmaExtraData,
        val currentNormalState: S) : EmaStateTransition
}