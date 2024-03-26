package com.carmabs.ema.core.viewmodel

import com.carmabs.ema.core.broadcast.BackBroadcastId

/**
 * Created by Carlos Mateo Benito on 2019-11-03.
 *
 * <p>
 * Copyright (c) 2019 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
internal class EmaResultHandler private constructor() {


    companion object {
        private val emaResultHandler = EmaResultHandler()

        fun getInstance(): EmaResultHandler = emaResultHandler
    }

    private val resultMap: HashMap<String, EmaResultModel> = HashMap()
    private val receiverMap: HashMap<String, EmaReceiverModel> = HashMap()
    private val pendingResultMap by lazy {
        HashMap<String,EmaResultModel>()
    }

    /**
     * Used for notify result data between views
     * @param emaResultModel notified
     */
    fun addResult(emaResultModel: EmaResultModel) {
        resultMap[emaResultModel.key] = emaResultModel
    }

    /**
     * Notify the results to all receivers are listening to them. The owner code is the id of
     * the class (viewmodel) is notifying, to avoid notify results that are not theirs
     */
    fun notifyResults(ownerId: String) {
        val keysToRemove = mutableListOf<String>()

        resultMap.forEach {
            if (it.value.ownerId == ownerId) {
                val result = it.value
                val resultKey = result.key
                val receiver = receiverMap[resultKey]
                receiver?.also {
                    if (ownerId != receiver.ownerId) {
                        receiver.function.invoke(result.data)
                        //If a receiver is attached we remove the result. Otherwise we keep it due to if process is killed and
                        //is restarted it must be retained to be delivered when previous screen is shown due to it is created on demand
                        keysToRemove.add(result.key)
                    }
                }?:also {
                    pendingResultMap[resultKey] = result
                }
            }
        }

        keysToRemove.forEach {
            resultMap.remove(it)
        }
        keysToRemove.clear()
    }

    /**
     * Add listener when result is notified
     */
    fun addResultReceiver(receiverModel: EmaReceiverModel) {
        if (receiverMap.containsKey(receiverModel.resultKey))
            throw IllegalStateException("The key receiver has already been registered. Each receiver should have a unique key")
        receiverMap[receiverModel.resultKey] = receiverModel
    }

    /**
     * Remove listener based on ownerId
     */
    fun removeResultListener(ownerId: String) {
        val receivers = receiverMap.filterValues {
            it.ownerId == ownerId
        }
        receivers.map {
            it.key
        }.forEach {
            receiverMap.remove(it)
            //We remove the result due to only one receiver is allowed, so the result wouldn't be needed
            resultMap.remove(it)
        }
    }

    fun notifyPendingResults(ownerId: String,backBroadcastId: BackBroadcastId) {
        pendingResultMap[backBroadcastId.id]?.also {
            if(ownerId!=it.ownerId) {
                receiverMap[backBroadcastId.id]?.function?.invoke(it.data)
                pendingResultMap.remove(backBroadcastId.id)
            }
        }

    }
}