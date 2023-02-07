package com.carmabs.ema.presentation.ui.unlogged.compose

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carmabs.ema.R
import com.carmabs.ema.android.delegates.emaViewModelSharedDelegate
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.core.state.EmaState
import com.carmabs.ema.presentation.base.BaseComposableFragment
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogListener
import com.carmabs.ema.presentation.ui.compose.AppButton
import com.carmabs.ema.presentation.ui.compose.AppButton2
import com.carmabs.ema.presentation.ui.compose.SimpleDialog
import com.carmabs.ema.presentation.ui.unlogged.EmaAndroidUnloggedToolbarViewModel
import com.carmabs.ema.presentation.ui.unlogged.EmaAndroidUnloggedViewModel
import com.carmabs.ema.presentation.ui.unlogged.EmaUnloggedNavigator
import com.carmabs.ema.presentation.ui.unlogged.EmaUnloggedState
import com.carmabs.ema.presentation.ui.unlogged.EmaUnloggedToolbarState
import com.carmabs.ema.presentation.ui.unlogged.EmaUnloggedViewModel
import org.koin.android.ext.android.inject

class EmaComposableUnloggedViewFragment :
    BaseComposableFragment<EmaUnloggedState, EmaUnloggedViewModel, EmaUnloggedNavigator.Navigation>() {

    /**
     * If you wouldn't want to use dependency injection you can provide it instantiating the class.
     * Not recommended
     */
    private val toolbarViewModel: EmaAndroidUnloggedToolbarViewModel by emaViewModelSharedDelegate(
        {
            EmaAndroidUnloggedToolbarViewModel(injectDirect())
        }
    ) {
        vm.onToolbarUpdated(it as EmaState<EmaUnloggedToolbarState>)
    }

    override val navigator: EmaUnloggedNavigator by inject()

    override fun provideAndroidViewModel(): EmaAndroidViewModel<EmaUnloggedViewModel> {
        return EmaAndroidUnloggedViewModel(injectDirect())
    }

    override fun onStart() {
        super.onStart()
        vm.toolbarViewModel = toolbarViewModel.emaViewModel
    }

    @Composable
    override fun onNormal(data: EmaUnloggedState) {
        val bottomMargin = 10.dp
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .height(IntrinsicSize.Max),
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(100.dp),
                    painter = painterResource(id = R.drawable.ic_error),
                    contentDescription = "Image error"
                )
                Spacer(modifier = Modifier.height(bottomMargin))
                Text(
                    text = stringResource(id = R.string.unlogged_invalid_credentials),
                    fontSize = 33.sp,
                    color = colorResource(id = R.color.black),
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .offset(0.dp, 30.dp),
            ) {
                if (data.showToolbarViewVisibility) {
                    AppButton2(
                        onClick = { vm.onActionToolbar() },
                        text = stringResource(R.string.unlogged_hide_toolbar)
                    )
                    Spacer(modifier = Modifier.height(bottomMargin))

                }
                AppButton(
                    onClick = { vm.onActionAddUser() },
                    text = stringResource(R.string.unlogged_add_user)
                )

                Spacer(modifier = Modifier.height(bottomMargin))
                AppButton(
                    text = stringResource(id = R.string.unlogged_show_dialog),
                    onClick = { vm.onActionShowDialog() }
                )
                Spacer(modifier = Modifier.height(bottomMargin))
                AppButton(
                    text = stringResource(id = R.string.unlogged_show_single_event),
                    onClick = { vm.onActionSingleEvent() }
                )
            }
        }
    }

    @Composable
    override fun onOverlayed(data: EmaExtraData) {
        when (data.type) {
            EmaUnloggedViewModel.EXTRA_DATA_SHOW_DIALOG -> {
                val dialogData = data.extraData as Pair<SimpleDialogData,SimpleDialogListener>
                SimpleDialog(dialogData.first,dialogData.second)
            }
        }
    }

    override fun onSingle(data: EmaExtraData) {
        Toast.makeText(requireContext(), R.string.unlogged_toast_sample, Toast.LENGTH_SHORT).show()
    }

    @Preview(
        device = Devices.NEXUS_5
    )
    @Composable
    fun OnNormalPreview() {
        onNormal(EmaUnloggedState(showToolbarViewVisibility = true))
    }
    
}