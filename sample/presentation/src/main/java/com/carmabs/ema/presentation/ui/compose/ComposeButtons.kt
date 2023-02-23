package com.carmabs.ema.presentation.ui.compose


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carmabs.ema.sample.ema.R

@Composable
fun AppButton(text: String, modifier: Modifier = Modifier.fillMaxWidth(), onClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.radiusButton)),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.colorPrimaryDark))
    ) {
        Text(
            text = text,
            color = colorResource(id = R.color.white)
        )
    }
}

@Composable
fun AppButton2(
    text: String,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .defaultMinSize(50.dp),
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        border = BorderStroke(1.dp, colorResource(id = R.color.colorPrimary)),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white))
    ) {
        Text(
            text = text,
            color = colorResource(id = R.color.colorSecondary)
        )
    }
}

//region Previews

@Preview
@Composable
fun AppButtonPreview() {
    AppButton(text = "Test") {

    }
}

//endregion