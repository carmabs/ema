package com.carmabs.ema.core.annotation

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

/**
 * Created by Carlos Mateo Benito on 14/11/21.
 *
 * <p>
 * Copyright (c) 2021 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class EmaAndroidViewModelGenerator {

    fun generateClass(annotationClassClass: EmaViewModelAnnotationClass): TypeSpec {
        val baseClassName = annotationClassClass.viewModelName
        val packageName = annotationClassClass.packageName
        val generatedClassName = "Android" + baseClassName
        val emaViewModelClass = ClassName(packageName,baseClassName)

        val type = TypeSpec.classBuilder(generatedClassName)
            .primaryConstructor(
                FunSpec.constructorBuilder().addParameter(
                    "viewmodel",
                    TypeSpec.classBuilder(baseClassName).build().javaClass
                ).build()
            )
            .addSuperinterface(ClassName("", "EmaAndroidViewModel")
                .parameterizedBy(WildcardTypeName.producerOf(emaViewModelClass)))
            .addSuperclassConstructorParameter(CodeBlock.of("viewModel"))
            .build()

        return type
    }
}