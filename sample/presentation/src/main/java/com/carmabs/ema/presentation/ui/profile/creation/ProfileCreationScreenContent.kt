package com.carmabs.ema.presentation.ui.profile.creation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carmabs.domain.model.Role
import com.carmabs.ema.core.model.EmaText
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.compose.BaseScreenComposable
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogListener
import com.carmabs.ema.presentation.ui.compose.AppButton
import com.carmabs.ema.presentation.ui.compose.SimpleDialogComposable
import com.carmabs.ema.sample.ema.R

class ProfileCreationScreenContent :
    BaseScreenComposable<ProfileCreationState, ProfileCreationScreenActions>() {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun onNormal(
        state: ProfileCreationState,
        actions: ProfileCreationScreenActions
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(
                        id = R.string.profile_creation_create_title,
                        state.roleText
                    )
                )
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    value = state.name,
                    label = {
                        Text(stringResource(id = R.string.profile_creation_create_name))
                    },
                    onValueChange = {
                        actions.onActionUserNameWritten(it)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.surname,
                    label = {
                        Text(stringResource(id = R.string.profile_creation_create_surname))
                    },
                    onValueChange = {
                        actions.onActionUserSurnameWritten(it)
                    }
                )

                AppButton(
                    text = stringResource(id = R.string.profile_creation_create_user),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    actions.onActionCreateClicked()
                }
            }
        }

    }

    @Composable
    override fun onOverlapped(extraData: EmaExtraData, actions: ProfileCreationScreenActions) {
        when (extraData.id) {
            ProfileCreationViewModel.OVERLAPPED_DIALOG_CONFIRMATION -> {
                val dialogData = extraData.data as SimpleDialogData
                SimpleDialogComposable(
                    dialogData = dialogData,
                    dialogListener = object : SimpleDialogListener {
                        override fun onCancelClicked() {
                            actions.onActionDialogCancelClicked()
                        }

                        override fun onConfirmClicked() {
                            actions.onActionDialogConfirmClicked()
                        }

                        override fun onBackPressed() {
                            actions.onActionDialogCancelClicked()
                        }

                    })
            }

        }
    }

    @Preview(device = Devices.NEXUS_5)
    @Composable
    fun normalPreview() {
        onStateNormal(
            state = ProfileCreationState(
                Role.ADMIN,
                "Carlos",
                "Mateo"
            ),
            object : ProfileCreationScreenActions {
                override fun onActionUserNameWritten(name: String) {
                    TODO("Not yet implemented")
                }

                override fun onActionUserSurnameWritten(surname: String) {
                    TODO("Not yet implemented")
                }

                override fun onActionCreateClicked() {
                    TODO("Not yet implemented")
                }

                override fun onActionDialogConfirmClicked() {
                    TODO("Not yet implemented")
                }

                override fun onActionDialogCancelClicked() {
                    TODO("Not yet implemented")
                }
            }
        )
    }

    @Preview(device = Devices.PIXEL_3)
    @Composable
    fun overlappedPreview() {
        normalPreview()
        showDialog(
            SimpleDialogData(
                title = EmaText.text("Test title"),
                message = EmaText.text("test message"),
                proportionWidth = 0.8f,
                showCancel = true
            ),
            object : SimpleDialogListener{
                override fun onCancelClicked() {

                }

                override fun onConfirmClicked() {

                }

                override fun onBackPressed() {

                }

            }
        )
    }
}