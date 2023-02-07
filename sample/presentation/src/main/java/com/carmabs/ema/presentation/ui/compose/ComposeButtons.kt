package com.carmabs.ema.presentation.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carmabs.ema.R

@Composable
fun AppButton(text: String, modifier: Modifier = Modifier.fillMaxWidth(), onClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.colorPrimary))
    ) {
        Text(
            text = text,
            color = colorResource(id = R.color.white)
        )
    }
}

@Composable
fun AppButtonAccent(
    text: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.colorAccent))
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
    modifier: Modifier = Modifier.fillMaxWidth(),
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        border = BorderStroke(1.dp, colorResource(id = R.color.colorAccent)),
        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.white))
    ) {
        Text(
            text = text,
            color = colorResource(id = R.color.colorAccent)
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

@Preview
@Composable
fun AppButton2Preview() {
    AppButton2(text = "Test") {

    }
}

//endregion