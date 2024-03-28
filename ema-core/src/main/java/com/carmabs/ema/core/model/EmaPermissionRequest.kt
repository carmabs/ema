package com.carmabs.ema.core.model

import com.carmabs.ema.core.manager.PermissionState

/**
 * Created by Carlos Mateo Benito on 20/6/23.
 *
 * <p>
 * Copyright (c) 2023 by Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 */
class EmaPermissionRequest private constructor(
    val shouldRequest: Boolean,
    val onPermissionResponse: ((PermissionState) -> Unit)
) {
    companion object {
        fun createRequest(onPermissionResponse: (PermissionState) -> Unit): EmaPermissionRequest {
            return EmaPermissionRequest(
                shouldRequest = true,
                onPermissionResponse = onPermissionResponse
            )
        }

        fun cancelRequest(): EmaPermissionRequest {
            return EmaPermissionRequest(
                shouldRequest = false,
                onPermissionResponse = { }
            )
        }
    }
}