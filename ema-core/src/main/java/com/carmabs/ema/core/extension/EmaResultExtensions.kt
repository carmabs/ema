package com.carmabs.ema.core.extension

import com.carmabs.ema.core.model.EmaResult

/**
 *  *<p>
 * Copyright (c) 2023, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Created by: Carlos Mateo Benito on 03/05/23.
 */
fun <S,F>Result<S>.toEmaResult(failureParser:(Throwable)->F):EmaResult<S,F>{
    return try {
        getOrThrow().let {
            EmaResult.success(it)
        }
    }catch (exception:Throwable){
        EmaResult.failure(failureParser.invoke(exception))
    }
}