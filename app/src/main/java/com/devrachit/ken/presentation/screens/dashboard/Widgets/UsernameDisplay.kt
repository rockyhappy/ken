package com.devrachit.ken.presentation.screens.dashboard.Widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.devrachit.ken.ui.theme.TextStyleInter24Lh36Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun UsernameDisplay(username: String) {
    Text(
        text = username,
        style = TextStyleInter24Lh36Fw700(),
        color = Color.White,
        modifier = Modifier
            .padding(start = 10.sdp, top = 36.dp, end = 60.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}