package com.devrachit.ken.presentation.screens.dashboard.Widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.devrachit.ken.R
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun MenuButton(onClick: () -> Unit) {
    Icon(
        imageVector = Icons.Default.Menu,
        contentDescription = "Menu",
        tint = Color.White,
        modifier = Modifier
            .padding(top = 29.sdp, start = 23.dp)
            .border(
                border = BorderStroke(2.sdp, Color.DarkGray),
                shape = RoundedCornerShape(5.sdp)
            )
            .size(32.sdp)
            .padding(4.sdp)
            .clickable(onClick = onClick)
            .background(colorResource(R.color.bg_neutral))
    )
}