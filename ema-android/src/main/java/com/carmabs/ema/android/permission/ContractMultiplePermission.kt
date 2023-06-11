package com.carmabs.ema.android.permission

import androidx.activity.result.ActivityResultLauncher
import com.carmabs.ema.core.manager.PermissionState
import com.carmabs.ema.core.model.emaFlowSingleEvent
import kotlinx.coroutines.flow.MutableSharedFlow
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
internal class ContractMultiplePermission : ContractPermission() {

    private var flow = emaFlowSingleEvent<Map<String, PermissionState>>()

    val contract = { permissionsResult: Map<String, Boolean> ->
        val resultMap = hashMapOf<String, PermissionState>()
        permissionsResult.iterator().forEach { (key, value) ->
            resultMap[key] = value.toState(shouldShowRequestPermissionRationale(key))
        }
        flow.tryEmit(resultMap)
        Unit
    }

    suspend fun launch(
        permissionRequest: ActivityResultLauncher<Array<String>>,
        vararg permissions: String
    ) : (Map<String, PermissionState>) {
        permissionRequest.launch(permissions.map { it }.toTypedArray())
        return flow.first()
    }
}