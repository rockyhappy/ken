package com.devrachit.ken.presentation.screens.dashboard.Widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.devrachit.ken.R
import com.devrachit.ken.utility.composeUtility.sdp
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.Job

@Composable
fun MenuButton(onClick: () -> Job, drawerProgress: Float = 0f) {

    val yOffset = lerp(0f, -100f, drawerProgress)
    val alpha = lerp(1f, 0f, drawerProgress)
    
    Icon(
        imageVector = Icons.Default.Menu,
        contentDescription = "Menu",
        tint = Color.White,
        modifier = Modifier
            .padding(top = 29.sdp, start = 23.dp)
            .offset(y = yOffset.dp)
            .alpha(alpha)
            .border(
                border = BorderStroke(2.sdp, Color.DarkGray),
                shape = RoundedCornerShape(5.sdp)
            )
            .size(32.sdp)
            .padding(4.sdp)
            .clickable(onClick = { onClick() }, enabled = alpha > 0.1f)
            .background(colorResource(R.color.bg_neutral))
    )
}