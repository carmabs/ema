package com.carmabs.ema.android.service


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import androidx.core.app.JobIntentService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent


/**
 * Created by Carlos Mateo Benito on 18/03/2021.
 *
 * <p>
 * Copyright (c) 2021 by Carmabs. All rights reserved.
 * </p>
 *
 * Service to handle job intents
 * - It has implemented koin to handle koin instances of application module
 * - If you want to use a custom module you can provide it by inject module function
 * - It has static enqueue methods to execute work implicitily
 * - Use suspend execute work function to handle async operations in the service job thread
 * - EmaJobListener implemented to handle work listener
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 **/
abstract class EmaJobService : JobIntentService(),KoinComponent {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    companion object {
        private const val EMA_SERVICE_LISTENER = "EMA_SERVICE_LISTENER"

        fun enqueueWork(
            context: Context,
            applicationId:String,
            classNamePath:String,
            jobId:Int,
            listener:ResultReceiver?=null
        ) {
            enqueueWork(
                context,
                ComponentName(applicationId, classNamePath),
                jobId,
                Intent().apply {
                    setClassName(applicationId, classNamePath)
                    listener?.also {
                        putExtra(EMA_SERVICE_LISTENER, it)
                    }
                }
            )
        }
    }

    final override fun onHandleWork(intent: Intent) {
        val resultReceiver: ResultReceiver? = intent.extras?.get(
            EMA_SERVICE_LISTENER
        ) as? ResultReceiver
        coroutineScope.launch {
            executeWork(intent, resultReceiver)
        }
    }

    abstract suspend fun executeWork(intent: Intent,resultReceiver: ResultReceiver?)

    abstract class EmaJobServiceListener(handler: Handler = Handler(Looper.getMainLooper())) :
        ResultReceiver(handler)
}