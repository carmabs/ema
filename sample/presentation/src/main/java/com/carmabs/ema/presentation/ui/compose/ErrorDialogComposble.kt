package com.carmabs.ema.presentation.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.carmabs.ema.compose.extension.stringResource
import com.carmabs.ema.core.model.EmaText
import com.carmabs.ema.presentation.dialog.error.ErrorDialogData
import com.carmabs.ema.presentation.dialog.error.ErrorDialogListener
import com.carmabs.ema.sample.ema.R

@Composable
fun ErrorDialogComposable(
    dialogData: ErrorDialogData, dialogListener: ErrorDialogListener
) {
    Dialog(
        onDismissRequest = {
            dialogListener.onBackPressed()
        }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Box(modifier = Modifier
                .align(Alignment.Center)
                .run {
                    dialogData.proportionWidth?.let {
                        fillMaxWidth(it)
                    } ?: wrapContentWidth()
                }
                .clip(RoundedCornerShape(dimensionResource(id = R.dimen.radio_corner_big)))
                .background(
                    color = colorResource(id = R.color.white),
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.cardview_default_radius))
                )) {
                Column(
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(id = R.dimen.space_medium),
                            end = dimensionResource(id = R.dimen.space_medium)
                        ),
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Column {
                        Image(
                            modifier = Modifier
                                .padding(top = dimensionResource(id = R.dimen.space_big))
                                .align(Alignment.CenterHorizontally)
                                .size(dimensionResource(id = R.dimen.icon_size_big)),
                            painter = painterResource(id = R.drawable.ic_error),
                            contentDescription = "Content image"
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = dimensionResource(id = R.dimen.space_medium),
                                )
                                .align(
                                    Alignment.CenterHorizontally
                                ),
                            text = dialogData.title.stringResource(),
                            fontSize = 22.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.black)
                        )
                    }
                    Text(
                        modifier = Modifier
                            .padding(
                                bottom = dimensionResource(id = R.dimen.space_big)
                            )
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = dialogData.message.stringResource(),
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.grayDark)
                    )
                    Text(
                        modifier = Modifier
                            .padding(
                                top = dimensionResource(id = R.dimen.space_medium),
                                bottom = dimensionResource(id = R.dimen.space_big)
                            )
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.dialog_accept),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = colorResource(id = R.color.error)
                    )
                }
            }
        }
    }
}

//region Previews
@Preview(
    device = Devices.NEXUS_5
)
@Composable
fun OnErrorPreview() {

        ErrorDialogComposable(
            ErrorDialogData(
                title = EmaText.text("Error"),
                message = EmaText.text("Preview message sample"),
            ),
            object : ErrorDialogListener {
                override fun onConfirmClicked() {

                }

                override fun onBackPressed() {

                }
            })
}
//endregion
