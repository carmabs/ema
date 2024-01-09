package com.carmabs.ema.core.state

sealed interface EmaStateTransition {
    data class NormalToOverlapped<S>(
        val previousNormalState: S,
        val currentOverlappedState: EmaExtraData) : EmaStateTransition

    data class OverlappedToNormal<S>(
        val previousOverlappedState: EmaExtraData,
        val currentNormalState: S) : EmaStateTransition
}