package com.carmabs.ema.core.extension

import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlin.reflect.KClass

fun <T:EmaViewModel<*,*>> KClass<T>.resultId(id:String?=null): ResultId {
    val resultId = id?:"Default"
    return ResultId("ViewModel_Result/${resultId}_${this.java.name}_${this.hashCode()}")
}

fun <T:EmaViewModel<*,*>> KClass<T>.id(id:String?=null): ResultId {
    val id = id?:"Default"
    return ResultId("ViewModel_Id/${id}_${this.java.name}_${this.hashCode()}")
}

@JvmInline
value class ResultId internal constructor(val id: String)