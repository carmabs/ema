package com.carmabs.ema.presentation.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.carmabs.ema.android.compose.ui.EmaComposableScreenView

class ProfileScreen : EmaComposableScreenView<ProfileState, ProfileViewModel, ProfileDestination, ProfileScreenActions> {

    @Composable
    override fun onStateNormal(state: ProfileState, actions: ProfileScreenActions) {

    }


    @Preview
    @Composable
    fun normalPreview() {
        onStateNormal(
            state = ProfileState(),
            object : ProfileScreenActions {
                override fun onActionAdminClicked() {

                }
            })
    }
}