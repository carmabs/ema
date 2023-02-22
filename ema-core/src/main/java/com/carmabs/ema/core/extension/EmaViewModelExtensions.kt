package com.carmabs.ema.core.extension

import com.carmabs.ema.core.viewmodel.EmaViewModel
import kotlin.reflect.KClass

fun <T:EmaViewModel<*,*>> KClass<T>.resultId(id:String? = null): String {
    val resultId = id?:"Default"
    return "Result_${resultId}_${this.java.name}_${this.hashCode()}"
}