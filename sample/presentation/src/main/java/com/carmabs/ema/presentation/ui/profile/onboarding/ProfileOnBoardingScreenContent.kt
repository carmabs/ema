package com.carmabs.ema.presentation.ui.profile.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carmabs.domain.model.User
import com.carmabs.ema.android.compose.ui.EmaComposableScreenContent
import com.carmabs.ema.presentation.ui.compose.AppButton
import com.carmabs.ema.sample.ema.R

class ProfileOnBoardingScreenContent :
    EmaComposableScreenContent<ProfileOnBoardingState, ProfileOnBoardingScreenActions> {

    @Composable
    override fun onStateNormal(
        state: ProfileOnBoardingState,
        actions: ProfileOnBoardingScreenActions
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .width(100.dp)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    text = stringResource(id = R.string.profile_panel)
                )

                Text(text = stringResource(id = R.string.profile_admin, state.userName))
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.2f),
                    contentScale = ContentScale.FillBounds,
                    painter = painterResource(id = R.drawable.ic_admin_panel),
                    contentDescription = "Add user"
                )
                Text(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
                    text = stringResource(R.string.profile_type_user_title),
                    textAlign = TextAlign.Start
                )
                AppButton(
                    text = stringResource(id = R.string.profile_user_admin)
                ) {
                    actions.onActionAdminClicked()
                }

                Spacer(modifier = Modifier.height(10.dp))
                AppButton(
                    text = stringResource(id = R.string.profile_user_basic)
                ) {
                    actions.onActionUserClicked()
                }
            }
        }

    }


    @Preview(device = Devices.NEXUS_5)
    @Composable
    fun normalPreview() {
        onStateNormal(
            state = ProfileOnBoardingState(
                User(
                    "Carlos",
                    "Mateo"
                )
            ),
            object : ProfileOnBoardingScreenActions {
                override fun onActionAdminClicked() = Unit
                override fun onActionUserClicked() = Unit
            })
    }
}