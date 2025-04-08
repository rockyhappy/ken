package com.devrachit.ken.presentation.screens.dashboard.Widgets

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.devrachit.ken.ui.theme.TextStyleInter24Lh36Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun UsernameDisplay(username: String, drawerProgress: Float = 0f) {
    val scale = animateFloatAsState(
        targetValue = if (drawerProgress < 0.5f) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "username_scale_animation"
    )
    
    val alpha = lerp(1f, 0f, drawerProgress)
    
    Text(
        text = username,
        style = TextStyleInter24Lh36Fw700(),
        color = Color.White,
        modifier = Modifier
            .padding(start = 10.sdp, top = 36.dp, end = 60.dp)
            .fillMaxWidth()
            .alpha(alpha)
            .scale(scale.value),
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}