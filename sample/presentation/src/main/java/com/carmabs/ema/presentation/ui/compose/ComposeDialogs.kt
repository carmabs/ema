package com.carmabs.ema.presentation.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.carmabs.ema.core.constants.FLOAT_ONE
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogData
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogListener
import com.carmabs.ema.sample.ema.R

@Composable
fun SimpleDialog(
    dialogData: SimpleDialogData,
    dialogListener: SimpleDialogListener
) {
    Dialog(onDismissRequest = {
        dialogListener.onBackPressed()
    }) {
        DialogContent(dialogData, dialogListener)
    }
}

@Composable
private fun DialogContent(
    dialogData: SimpleDialogData,
    dialogListener: SimpleDialogListener
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(dialogData.proportionWidth?: FLOAT_ONE)
            .wrapContentHeight()
            .background(
                color = colorResource(id = R.color.white),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.cardview_default_radius))
            )

    ) {
        Column (){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                if (dialogData.showCross) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(
                                top = dimensionResource(id = R.dimen.space_big),
                                end = dimensionResource(id = R.dimen.space_big),
                            )
                            .clickable {
                                dialogListener.onBackPressed()
                            },
                        painter = painterResource(id = R.drawable.ic_cross),
                        contentDescription = "Cancel"
                    )
                }
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(dimensionResource(id = R.dimen.icon_size_huge)),
                    painter = painterResource(id = dialogData.imageId ?: R.drawable.ic_cross),
                    contentDescription = "Content image"
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = dimensionResource(id = R.dimen.space_big),
                        )
                        .align(
                            Alignment.CenterHorizontally
                        ),
                    text = dialogData.title,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black)
                )
            }
            Text(
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.space_big),
                    )
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = dialogData.message,
                fontSize = 18.sp,
                color = colorResource(id = R.color.grayDark)
            )

            val padding = dimensionResource(id = R.dimen.space_big_huge)
            val paddingTop = dimensionResource(id = R.dimen.space_very_big)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(padding, paddingTop, padding, padding)
                    .fillMaxWidth(),
            ) {
                if (dialogData.showCancel) {
                    AppButton2(
                        modifier = Modifier.wrapContentSize().weight(FLOAT_ONE),
                        text = stringResource(id = R.string.dialog_cancel),
                        onClick = {
                            // Change the state to close the dialog
                            dialogListener.onConfirmClicked()
                        },
                    )
                }
                AppButtonAccent(
                    modifier = Modifier.wrapContentSize().weight(FLOAT_ONE),
                    text = stringResource(id = R.string.dialog_accept),
                    onClick = {
                        // Change the state to close the dialog
                        dialogListener.onCancelClicked()
                    },
                )
            }

        }
    }
}

//region Previews
@Preview(
    device = Devices.NEXUS_5
)
@Composable
fun OnDialogPreview() {
    DialogContent(
        SimpleDialogData(
            "Preview title sample",
            "Preview message sample",
            showCancel = true
        ),
        object : SimpleDialogListener {
            override fun onCancelClicked() {

            }

            override fun onConfirmClicked() {

            }

            override fun onBackPressed() {

            }
        }
    )
}
//endregion
