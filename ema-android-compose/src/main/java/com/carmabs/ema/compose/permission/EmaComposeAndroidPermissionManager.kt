package com.carmabs.ema.compose.permission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.carmabs.ema.android.permission.EmaAndroidPermissionManager
import com.carmabs.ema.android.permission.EmaContractMultiplePermission
import com.carmabs.ema.android.permission.EmaContractSinglePermission
import com.carmabs.ema.compose.extension.activity
import com.carmabs.ema.compose.extension.isInPreview
import com.carmabs.ema.core.constants.INT_ZERO
import com.carmabs.ema.core.manager.EmaPermissionManager
import com.carmabs.ema.core.manager.PermissionState

/**
 * Created by Carlos Mateo Benito on 15/9/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
@Composable
fun rememberEmaPermissionManager(): EmaPermissionManager {
    val permissionManager: EmaPermissionManager = if (isInPreview())
        EmaPreviewPermissionManager
    else {
        val context = LocalContext.current

        val activity = LocalContext.activity

        val singleContract = remember {
            EmaContractSinglePermission {
                activity.shouldShowRequestPermissionRationale(it)
            }
        }
        val multipleContract = remember {
            EmaContractMultiplePermission {
                activity.shouldShowRequestPermissionRationale(it)
            }
        }

        val singlePermissionManager = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = singleContract.contract
        )

        val multiplePermissionManager = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = multipleContract.contract
        )
        remember {
            EmaAndroidPermissionManager(
                context = context,
                activitySinglePermissionResultLauncher = singlePermissionManager,
                activityMultiplePermissionResultLauncher = multiplePermissionManager,
                contractSinglePermission = singleContract,
                contractMultiplePermission = multipleContract
            )
        }
    }

    return permissionManager
}

private object EmaPreviewPermissionManager : EmaPermissionManager {
    override suspend fun requestPermission(permission: String): PermissionState {
        return PermissionState.GRANTED
    }

    override suspend fun requestMultiplePermission(vararg permission: String): Map<String, PermissionState> {
        return mapOf(Pair(permission[INT_ZERO], PermissionState.GRANTED))
    }

    override fun isPermissionGranted(permission: String): PermissionState {
        return PermissionState.GRANTED
    }

    override fun areAllPermissionsGranted(vararg permission: String): Boolean {
        return true
    }

    override fun shouldShowRequestPermissionRationale(permission: String): Boolean {
        return true
    }

    override suspend fun requestCoarseLocationPermission(): PermissionState {
        return PermissionState.GRANTED
    }

    override suspend fun requestFineLocationPermission(): PermissionState {
        return PermissionState.GRANTED
    }

    override fun isLocationFineGranted(): PermissionState {
        return PermissionState.GRANTED
    }

    override fun isLocationBackgroundGranted(): PermissionState {
        return PermissionState.GRANTED
    }

    override fun isLocationCoarseGranted(): PermissionState {
        return PermissionState.GRANTED
    }

}