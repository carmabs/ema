package com.carmabs.ema.core.manager

/**
 * Created by Carlos Mateo Benito on 2022-08-12.
 *
 * <p>
 * Copyright (c) 2022 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
interface EmaPermissionManager {

    suspend fun requestPermission(permission: String): PermissionState

    suspend fun requestMultiplePermission(
        vararg permission: String
    ): (Map<String, PermissionState>)

    fun isPermissionGranted(permission: String): PermissionState

    fun areAllPermissionsGranted(vararg permission: String): Boolean

    fun shouldShowRequestPermissionRationale(permission: String): Boolean

    suspend fun requestCoarseLocationPermission():PermissionState

    suspend fun requestFineLocationPermission():PermissionState

    fun isLocationFineGranted(): PermissionState

    fun isLocationBackgroundGranted(): PermissionState

    fun isLocationCoarseGranted(): PermissionState
}