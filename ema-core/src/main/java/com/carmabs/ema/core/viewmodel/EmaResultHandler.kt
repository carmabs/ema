package com.carmabs.ema.core.viewmodel

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
    private val receiverMap: HashMap<String, HashMap<String, EmaReceiverModel>> = HashMap()

    /**
     * Used for notify result data between views
     * @param emaResultModel notified
     */
    fun addResult(emaResultModel: EmaResultModel) {
        resultMap[emaResultModel.code] = emaResultModel
    }

    /**
     * Notify the results to all receivers are listening to them. The owner code is the id of
     * the class (viewmodel) is notifying, to avoid notify results that are not theirs
     */
    fun notifyResults(ownerId: String) {
        val keysToRemove = mutableListOf<String>()

        //Map for avoid iteration exception if a result is added on receiver invocation
        val receiverExecutions = mutableListOf<() -> Unit>()

        resultMap.forEach {
            if (it.value.ownerId == ownerId) {
                val result = it.value
                val code = result.code
                receiverMap[code]?.forEach { entry ->
                    val receiver = entry.value
                    if (ownerId != receiver.ownerId) {
                        receiverExecutions.add {
                            receiver.function.invoke(result.data)
                        }
                    }

                    keysToRemove.add(result.code)
                }
            }
        }

        keysToRemove.forEach {
            resultMap.remove(it)
        }

        receiverExecutions.forEach {
            it.invoke()
        }

        keysToRemove.clear()
    }

    /**
     * Add listener when result is notified
     */
    fun addResultReceiver(receiverModel: EmaReceiverModel) {
        receiverMap[receiverModel.resultCode]?.apply {
            put(receiverModel.ownerId, receiverModel)
        } ?: also {
            receiverMap[receiverModel.resultCode] = HashMap<String, EmaReceiverModel>().also {
                it[receiverModel.ownerId] = receiverModel
            }
        }
    }

    /**
     * Remove listener based on ownerId
     */
    fun removeResultListener(ownerId: String) {
        val codeToClear = mutableSetOf<String>()
        receiverMap.forEach {
            val receiverOwners = it.value
            receiverOwners.remove(ownerId)
            if (receiverOwners.isEmpty())
                codeToClear.add(it.key)
        }
        codeToClear.forEach {
            receiverMap.remove(it)
        }
    }
}