package com.carmabs.ema.compose.permission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.carmabs.ema.android.permission.EmaContractMultiplePermission
import com.carmabs.ema.android.permission.EmaAndroidPermissionManager
import com.carmabs.ema.android.permission.EmaContractSinglePermission
import com.carmabs.ema.compose.extension.findComponentActivity
import com.carmabs.ema.core.manager.EmaPermissionManager

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
    val context = LocalContext.current

    val activity = remember {
        context.findComponentActivity()
    }

    val singleContract = remember {
        EmaContractSinglePermission{
            activity.shouldShowRequestPermissionRationale(it)
        }
    }
    val multipleContract = remember {
        EmaContractMultiplePermission{
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
    val permissionManager = remember {
        EmaAndroidPermissionManager(
            context = context,
            activitySinglePermissionResultLauncher = singlePermissionManager,
            activityMultiplePermissionResultLauncher = multiplePermissionManager,
            contractSinglePermission = singleContract,
            contractMultiplePermission = multipleContract
        )
    }

    return permissionManager
}