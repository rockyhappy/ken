package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.devrachit.ken.ui.theme.KenTheme
import kotlinx.coroutines.launch
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.presentation.screens.dashboard.Widgets.DashboardHeader
import com.devrachit.ken.presentation.screens.dashboard.Widgets.MenuButton
import com.devrachit.ken.presentation.screens.dashboard.Widgets.UsernameDisplay
import com.devrachit.ken.ui.theme.TextStyleInter14Lh18Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh24Fw400
import com.devrachit.ken.ui.theme.TextStyleInter18Lh24Fw700
import com.devrachit.ken.ui.theme.TextStyleInter24Lh36Fw600
import com.devrachit.ken.ui.theme.TextStyleInter24Lh36Fw700
import com.devrachit.ken.utility.composeUtility.CompletePreviews
import com.devrachit.ken.utility.composeUtility.ProfilePictureShimmer
import com.devrachit.ken.utility.composeUtility.ProfilePictureShimmerPreview
import com.devrachit.ken.utility.composeUtility.sdp
import com.valentinilk.shimmer.shimmer

@Composable
fun DashboardContent(
    uiState: States,
) {
    KenTheme {
        val coroutineScope = rememberCoroutineScope()
        val drawerWidth = 700.sdp

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.bg_neutral))
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(paddingValues = innerPadding)
                    .background(colorResource(id = R.color.card_elevated))
            ) {
                var drawerState by remember { mutableStateOf(DrawerValue.Closed) }
                var translationX = remember {
                    androidx.compose.animation.core.Animatable(0f)
                }
                translationX.updateBounds(0f, drawerWidth.value)
                val draggableState = rememberDraggableState(
                    onDelta = { dragAmount ->
                        coroutineScope.launch {
                            translationX.snapTo(
                                (translationX.value + dragAmount).coerceIn(
                                    0f,
                                    drawerWidth.value
                                )
                            )
                        }
                    }
                )

                fun toggleDrawerState() {
                    coroutineScope.launch {
                        if (drawerState == DrawerValue.Open) {
                            translationX.animateTo(
                                0f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                            drawerState = DrawerValue.Closed
                        } else {
                            translationX.animateTo(
                                drawerWidth.value,
                                animationSpec = tween(durationMillis = 400)
                            )
                            drawerState = DrawerValue.Open
                        }
                    }
                }

                fun snapDrawerToState(targetState: DrawerValue, velocity: Float = 0f) {
                    coroutineScope.launch {
                        val targetValue =
                            if (targetState == DrawerValue.Open) drawerWidth.value else 0f

                        translationX.animateTo(
                            targetValue = targetValue,
                            initialVelocity = velocity,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessMediumLow
                            )
                        )

                        drawerState = targetState
                    }
                }

                HomeScreenDrawer(uiState = uiState, onClick = { toggleDrawerState() })

                ScreenContents(
                    uiStates = uiState,
                    onClick = { toggleDrawerState() },
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            shadowElevation = translationX.value * 0.1f

                            val progress = (translationX.value / drawerWidth.value).coerceIn(0f, 1f)

                            this.translationX = this@graphicsLayer.translationX + translationX.value

                            val scale = lerp(1f, 0.9f, progress)
                            scaleX = scale
                            scaleY = scale

                            val cornerRadius = lerp(0f, 24f, progress)
                            shape = RoundedCornerShape(cornerRadius.dp)

                            clip = progress > 0
                        }
                        .draggable(
                            state = draggableState,
                            orientation = Orientation.Horizontal,
                            onDragStopped = { velocity ->
                                val targetThreshold = drawerWidth.value * 0.5f
                                val targetState = if (translationX.value > targetThreshold ||
                                    (translationX.value > drawerWidth.value * 0.1f && velocity > 800)
                                ) {
                                    DrawerValue.Open
                                } else {
                                    DrawerValue.Closed
                                }

                                snapDrawerToState(targetState, velocity)
                            }
                        )
                )
            }
        }
    }
}

@Composable
fun HomeScreenDrawer(
    uiState: States,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(240.sdp)
            .padding(top = 24.sdp, start = 18.sdp)
            .background(colorResource(R.color.card_elevated)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start

    ) {
        Box(
            modifier = Modifier
                .padding(5.sdp)
                .border(
                    border = BorderStroke(2.sdp, Color.DarkGray),
                    shape = RoundedCornerShape(5.sdp)
                )
                .size(28.sdp)
                .padding(2.sdp)
                .clip(RoundedCornerShape(5.sdp))
                .background(Color.Transparent)

        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Menu",
                tint = Color.White,
                modifier = Modifier
                    .size(24.sdp)
                    .clickable(onClick = { onClick.invoke() })
            )
        }
        if (!uiState.isLoadingUserInfo) {
            AsyncImage(
                model = uiState.leetCodeUserInfo.profile?.userAvatar,
                contentDescription = "Example image for demonstration purposes",
                modifier = Modifier
                    .padding(top = 30.sdp, start = 10.sdp)
                    .size(100.sdp)
                    .clip(RoundedCornerShape(10.sdp))
            )
            Text(
                text = uiState.leetCodeUserInfo.username.toString(),
                style = TextStyleInter24Lh36Fw700(),
                modifier = Modifier.padding(top = 8.sdp, start = 10.sdp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = uiState.leetCodeUserInfo.profile?.realName.toString(),
                style = TextStyleInter14Lh18Fw400(),
                modifier = Modifier
                    .padding(top = 1.sdp, start = 10.sdp)
                    .alpha(0.5f)
            )
        } else {
            ProfilePictureShimmer()
        }
    }
}

@Composable
fun ScreenContents(uiStates: States, modifier: Modifier = Modifier, onClick: () -> Unit) {
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
                username = uiStates.leetCodeUserInfo.username.toString(),
                onClick = onClick
            )
        }
    }
}




