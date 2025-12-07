package com.devrachit.ken.presentation.screens.dashboard.sheets.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter14Lh16Fw600
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw600
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun DeleteSheetDialog(
    showDialog: Boolean,
    sheetName: String,
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            modifier = Modifier
                .widthIn(min = 280.sdp, max = 400.sdp)
                .clip(RoundedCornerShape(36.sdp))
                .background(colorResource(R.color.bg_neutral))
                .border(
                    BorderStroke(2.sdp, colorResource(id = R.color.white).copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(36.sdp)
                ),
            onDismissRequest = onDismissRequest,
            title = {
                Text(
                    text = "Delete Sheet",
                    color = colorResource(R.color.white),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.sdp),
                    style = TextStyleInter20Lh24Fw700()
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete \"$sheetName\"? This will remove all questions from this sheet.",
                    color = colorResource(R.color.white),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.sdp),
                    style = TextStyleInter14Lh16Fw600()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmDelete,
                    modifier = Modifier
                        .padding(bottom = 16.sdp, start = 16.sdp, end = 16.sdp)
                        .clip(RoundedCornerShape(36.sdp))
                        .fillMaxWidth()
                        .background(colorResource(R.color.hard_filled_red))
                ) {
                    androidx.compose.material3.Text(
                        modifier = Modifier.padding(horizontal = 8.sdp),
                        text = "Delete",
                        color = colorResource(R.color.white),
                        style = TextStyleInter16Lh24Fw600()
                    )
                }
            },
            backgroundColor = colorResource(R.color.bg_neutral),
            dismissButton = {
                TextButton(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .padding(bottom = 16.sdp, start = 16.sdp, end = 16.sdp)
                        .clip(RoundedCornerShape(36.sdp))
                        .fillMaxWidth()
                        .background(colorResource(R.color.white))
                ) {
                    Text(
                        text = "Cancel",
                        modifier = Modifier.padding(horizontal = 8.sdp),
                        color = colorResource(R.color.black),
                        style = TextStyleInter16Lh24Fw600()
                    )
                }
            }
        )
    }
}
