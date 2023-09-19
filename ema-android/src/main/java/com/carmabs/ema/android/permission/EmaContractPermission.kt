package com.carmabs.ema.android.permission

/**
 * Created by Carlos Mateo Benito on 2022-08-12.
 *
 * <p>
 * Copyright (c) 2022 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
abstract class EmaContractPermission(private val shouldShowRequestPermissionRationaleFunction:(String)->Boolean) {
    protected fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return shouldShowRequestPermissionRationaleFunction.invoke(permission)
    }

}