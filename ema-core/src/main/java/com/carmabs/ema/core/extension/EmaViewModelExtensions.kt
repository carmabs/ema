package com.carmabs.ema.core.extension

import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.ema.core.viewmodel.EmaViewModelBasic
import kotlin.reflect.KClass

fun <T:EmaViewModel<*,*>> KClass<T>.resultId(id:String?=null): ResultId {
    val resultId = id?:"Default"
    return ResultId("Result_${resultId}_${this.java.name}_${this.hashCode()}")
}

@JvmInline
value class ResultId internal constructor(val id: String)