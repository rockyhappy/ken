package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.Widgets.DashboardHeader
import com.devrachit.ken.utility.composeUtility.sdp
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.Job


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScreenContents(
    username: String,
    uiStates: States,
    modifier: Modifier = Modifier,
    onClick: () -> Job,
    drawerProgress: Float = 0f
) {
    // Bottom row animation parameters
    val yOffset = lerp(0f, 100f, drawerProgress)
    val alpha = lerp(1f, 0f, drawerProgress)
    
    // Add spring animation for scale when row reappears
    val scale = animateFloatAsState(
        targetValue = if (drawerProgress < 0.5f) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "bottom_row_scale_animation"
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(0.dp)
            )
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .background(colorResource(R.color.bg_neutral))
                .fillMaxSize()
        ) {
            DashboardHeader(
                username = username,
                onClick = onClick,
                drawerProgress = drawerProgress
            )
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Cyan),
                ) {}

                // Bottom navigation row with spring animation when reappearing
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 28.dp)
                        .offset(y = yOffset.dp) // Move down when drawer opens
                        .alpha(alpha)
                        .scale(scale.value) // Apply spring animation when reappearing
                        .fillMaxWidth()
                        .height(70.sdp)
                        .align(Alignment.BottomEnd)
                        .clip(RoundedCornerShape(36.sdp))
                        .background(colorResource(R.color.card_elevated)),
                    horizontalArrangement = Arrangement.Absolute.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically

                ) {

                }
            }
        }
    }
}
