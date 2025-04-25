package com.devrachit.ken.utility.composeUtility

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
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


@Composable
fun LoadingDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmExit: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            modifier = Modifier
                .size(110.sdp)
                .clip(RoundedCornerShape(36.sdp))
                .background(colorResource(R.color.bg_neutral))
                .border(
                    BorderStroke(2.sdp, colorResource(id = R.color.white).copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(36.sdp)
                )
                .padding(6.sdp),
            onDismissRequest = onDismissRequest,

            text = {
                ProgressIndicator(color=colorResource(R.color.white), modifier= Modifier.fillMaxSize().offset(x=-6.sdp))
            },
            confirmButton = {

            },
            backgroundColor = colorResource(R.color.bg_neutral),
            dismissButton = {

            }
        )
    }
}