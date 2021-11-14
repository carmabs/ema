package com.carmabs.ema.core.annotation

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * Created by Carlos Mateo Benito on 14/11/21.
 *
 * <p>
 * Copyright (c) 2021 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class EmaAndroidViewModelProcessor : AbstractProcessor() {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> = mutableSetOf(EmaAndroidViewModel::class.java.canonicalName)

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
                ?: return false
        val typeElements = roundEnv.getElementsAnnotatedWith(EmaAndroidViewModel::class.java).map {
            getViewModel(it)
        }
        generateAndroidViewModelClass(typeElements,kaptKotlinGeneratedDir)
        return true
    }

    private fun generateAndroidViewModelClass(
        viewModelClasses: List<EmaViewModelAnnotationClass>,
        kaptKotlinGeneratedDir: String
    ) {
        if(viewModelClasses.isEmpty()){
            return
        }

        viewModelClasses.forEach {
            val emaAndroidViewModelClass = ClassName(it.packageName, "Android"+it.viewModelName)
            val generatedClass = EmaAndroidViewModelGenerator().generateClass(it)
            FileSpec.builder(it.packageName, emaAndroidViewModelClass.simpleName)
                .addType(generatedClass)
                .build()
                .writeTo(File(kaptKotlinGeneratedDir))
       }


    }

    private fun getViewModel(elem: Element):EmaViewModelAnnotationClass{
        val packageName = processingEnv.elementUtils.getPackageOf(elem).toString()
        val modelName = elem.simpleName.toString()
        return EmaViewModelAnnotationClass(packageName,modelName)
    }

}