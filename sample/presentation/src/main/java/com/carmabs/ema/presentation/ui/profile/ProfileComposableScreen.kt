package com.carmabs.ema.presentation.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.carmabs.ema.android.compose.ui.EmaComposableScreen
import com.carmabs.ema.android.di.injectDirect

@Composable
fun ProfileComposableScreen() {
    EmaComposableScreen<ProfileState, ProfileViewModel, ProfileDestination>(
        ProfileState(),
        androidViewModel = ProfileAndroidViewModel(injectDirect())
    ) { vm, state ->
        Column {
            Button(onClick = { vm.onActionAdminClicked() }) {
                Text(text = "Admin")
            }
        }

    }
}