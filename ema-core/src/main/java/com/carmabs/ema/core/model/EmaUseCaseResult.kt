package com.carmabs.ema.core.model

import com.carmabs.ema.core.delegate.emaSyncDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext

/**
 * Created by Carlos Mateo Benito on 16/8/22.
 *
 * <p>
 * Copyright (c) 2022 by Carmabs. All rights reserved.
 * </p>
 * Class used  to handle the coroutine result with the methods and variables
 * - onSuccess when the result of action function is successful
 * - onError when the action function has thrown an error
 * - onFinish when the action function has ended, independently if an error has been thrown
 * - job returns the job where the action function has been executed+
 * @param scope than launch the coroutine
 * @param dispatcher where the coroutine is handled
 * @param onAction function that will be executed inside the concurrency manager
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */

class EmaUseCaseResult<T> internal constructor(
    scope: CoroutineScope,
    dispatcher:CoroutineContext,
    onAction: suspend CoroutineScope.() -> T
) : EmaOnSuccessFinish<T>, EmaOnErrorFinish {

    private var result: EmaResult<T, Throwable>? by emaSyncDelegate(null)

    private var successListener: ((T) -> Unit)? = null
    private var errorListener: ((Throwable) -> Unit)? = null

    private var finishListener: (() -> Unit)? = null

    private var finished: AtomicBoolean = AtomicBoolean(false)

    override val job: Job = scope.launch(dispatcher) {
        try {
            val data = onAction.invoke(this)
            result = EmaResult.success(data)

        } catch (e: Exception) {
            result = EmaResult.failure(e)

        } finally {
            result?.apply {
                onSuccess {
                    successListener?.invoke(it)
                }.onFailure {
                    errorListener?.invoke(it)
                }
            }
            finished.set(true)
            finishListener?.invoke()
            successListener = null
            errorListener = null
            finishListener = null
        }
    }

    override fun onFinish(finishAction: () -> Unit): Job {
        if (finished.get())
            finishAction.invoke()
        else {
            finishListener = finishAction
        }
        return job
    }

    override fun onSuccess(successAction: (T) -> Unit): EmaOnErrorFinish {
        result?.onSuccess {
            successAction.invoke(it)
        } ?: also {
            successListener = successAction
        }
        return this
    }

    override fun onError(errorAction: (Throwable) -> Unit): EmaOnSuccessFinish<T> {
        result?.onFailure {
            errorAction.invoke(it)
        } ?: also {
            errorListener =  errorAction
        }
        return this
    }
}

interface EmaOnJob {
    val job: Job
}

interface EmaOnFinish : EmaOnJob {
    fun onFinish(finishAction: () -> Unit): Job
}

interface EmaOnErrorFinish : EmaOnFinish, EmaOnJob {
    fun onError(errorAction: (Throwable) -> Unit): EmaOnFinish
}

interface EmaOnSuccessFinish<T> : EmaOnFinish, EmaOnJob {
    fun onSuccess(successAction: (T) -> Unit): EmaOnFinish
}

