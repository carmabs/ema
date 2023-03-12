package com.carmabs.ema.core.extension

import com.carmabs.ema.core.constants.LONG_ZERO
import kotlinx.coroutines.delay
import kotlin.time.Duration

suspend fun <T>waitMinTime(minTime: Duration, action:suspend ()->T):T{
    val initTime = System.currentTimeMillis()
    val resultValue = action.invoke()
    val timeSpent = System.currentTimeMillis()-initTime
    delay((minTime.inWholeMilliseconds - timeSpent).coerceAtLeast(LONG_ZERO))
    return resultValue
}