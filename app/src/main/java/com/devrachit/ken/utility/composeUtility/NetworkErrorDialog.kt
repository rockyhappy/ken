package com.devrachit.ken.utility.composeUtility

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.devrachit.ken.R
import androidx.compose.ui.tooling.preview.Preview
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw600
import com.devrachit.ken.ui.theme.TextStyleInter14Lh16Fw600
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw600
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw700

@Composable
fun NetworkErrorDialog(
    onRetry: () -> Unit
) {
    AlertDialog(
        modifier = Modifier
            .clip(RoundedCornerShape(36.sdp))
            .background(colorResource(R.color.bg_neutral))
            .border(
                BorderStroke(2.sdp, colorResource(id = R.color.white).copy(alpha = 0.5f)),
                shape = RoundedCornerShape(36.sdp),

            )
        ,
        onDismissRequest = {},
        title = {
            Text(
                text = "ⓘ   Network Error",
                color = colorResource(R.color.white),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.sdp),
                style = TextStyleInter20Lh24Fw700()
            )
        },
        text = {
            Column(
                modifier = Modifier.background(colorResource(id = R.color.bg_neutral))
            ) {
                Text(
                    "Oops! Looks like you're offline.",
                    color = colorResource(R.color.white),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.sdp),
                    style = TextStyleInter14Lh16Fw600()
                )
                Spacer(modifier = Modifier.height(8.sdp))
                Text(
                    "Please check your internet connection and try again.",
                    color = colorResource(R.color.white),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.sdp),
                    style = TextStyleInter14Lh16Fw600()
                )
//                Spacer(modifier = Modifier.height(8.sdp))
//                Text(
//                    "Oops! Looks like you're offline.",
//                    color = colorResource(R.color.white),
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 16.sdp),
//                    style = TextStyleInter14Lh16Fw600()
//                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onRetry,
                modifier = Modifier
                    .padding(bottom = 16.sdp, start = 16.sdp, end = 16.sdp)
                    .clip(RoundedCornerShape(36.sdp))
                    .fillMaxWidth()
                    .background(colorResource(R.color.white))
            ) {
                Text(
                    text = "Retry",
                    color = colorResource(R.color.black),
                    style = TextStyleInter16Lh24Fw600()
                )
            }
        },
        backgroundColor = colorResource(R.color.bg_neutral)
    )
}

@Preview(showBackground = true)
@Composable
fun NetworkErrorDialogPreview() {
    NetworkErrorDialog(onRetry = {})
}