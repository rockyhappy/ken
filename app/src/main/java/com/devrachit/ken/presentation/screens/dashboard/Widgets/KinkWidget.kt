package com.devrachit.ken.presentation.screens.dashboard.Widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devrachit.ken.utility.composeUtility.ssp

@Composable
fun KinkWidget(
    colorResourceId: Int,
    tag: String,
    totalCount: Int,
    attemptedCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = colorResource(colorResourceId),
                        fontSize = 16.ssp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("$tag  ")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("$attemptedCount /")
                }
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        fontSize = 9.sp
                    )
                ) {
                    append(" $totalCount")
                }
            }
        )
        Spacer(modifier = Modifier.width(4.dp))
    }
}