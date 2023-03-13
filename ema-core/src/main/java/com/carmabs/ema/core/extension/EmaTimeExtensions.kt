package com.carmabs.ema.core.extension

import com.carmabs.ema.core.constants.LONG_ZERO
import kotlinx.coroutines.delay
import kotlin.time.Duration

suspend fun <T> waitMinTime(minTime: Duration, action: suspend () -> T): T {
    val initTime = System.currentTimeMillis()
    val resultValue = action.invoke()
    val timeSpent = System.currentTimeMillis() - initTime
    val differenceTime = minTime.inWholeMilliseconds - timeSpent
    if (differenceTime > LONG_ZERO) {
        delay(differenceTime)
    }
    return resultValue
}