package com.carmabs.ema.android.permission

import androidx.activity.result.ActivityResultLauncher
import com.carmabs.ema.core.manager.PermissionState
import com.carmabs.ema.core.model.emaFlowSingleEvent
import kotlinx.coroutines.flow.first

/**
 * Created by Carlos Mateo Benito on 2022-08-12.
 *
 * <p>
 * Copyright (c) 2022 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class EmaContractSinglePermission(shouldShowRequestPermissionRationaleFunction: (String) -> Boolean) : EmaContractPermission(
    shouldShowRequestPermissionRationaleFunction
) {

    private var permissionId: String? = null
    private var flow = emaFlowSingleEvent<PermissionState>()

    val contract = { granted: Boolean ->
        permissionId?.also {
            flow.tryEmit(granted.toState(shouldShowRequestPermissionRationale(it)))
        }
        Unit
    }

    suspend fun launch(
        permission: String,
        permissionRequest: ActivityResultLauncher<String>,
    ): PermissionState {
        permissionId = permission
        permissionRequest.launch(permission)
        return flow.first()
    }
}