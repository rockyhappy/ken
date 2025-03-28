package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil.compose.AsyncImage
import com.devrachit.ken.ui.theme.KenTheme
import kotlinx.coroutines.launch
import com.devrachit.ken.R
import com.devrachit.ken.utility.composeUtility.CompletePreviews
import com.devrachit.ken.utility.composeUtility.sdp

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
                    .background(colorResource(id=R.color.card_elevated))
            ) {
                var drawerState by remember { mutableStateOf(DrawerValue.Closed) }
                var translationX = remember {
                    androidx.compose.animation.core.Animatable(0f)
                }
                translationX.updateBounds(0f, drawerWidth.value)
                val draggableState = rememberDraggableState(
                    onDelta = { dragAmount ->
                        coroutineScope.launch {
                            translationX.snapTo((translationX.value + dragAmount).coerceIn(0f, drawerWidth.value))
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
                
                HomeScreenDrawer(uiState=uiState)
                
                val decay = rememberSplineBasedDecay<Float>()
                
                ScreenContents(
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
                                val decayX = decay.calculateTargetValue(
                                    translationX.value,
                                    velocity
                                )
                                
                                coroutineScope.launch {
                                    val targetThreshold = drawerWidth.value * 0.5f
                                    val targetX = if (decayX > targetThreshold || 
                                        (translationX.value > targetThreshold && velocity > -800)) {
                                        drawerWidth.value
                                    } else {
                                        0f
                                    }
                                    
                                    val canReachTargetWithDecay =
                                        (velocity > 0 && targetX == drawerWidth.value) || 
                                        (velocity < 0 && targetX == 0f)
                                    
                                    if (canReachTargetWithDecay && kotlin.math.abs(velocity) > 500) {
                                        translationX.animateDecay(
                                            initialVelocity = velocity,
                                            animationSpec = decay
                                        )
                                    } else {
                                        translationX.animateTo(
                                            targetValue = targetX,
                                            initialVelocity = velocity,
                                            animationSpec = spring(
                                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                                stiffness = Spring.StiffnessLow
                                            )
                                        )
                                    }
                                    
                                    drawerState = 
                                        if (translationX.value > targetThreshold) DrawerValue.Open 
                                        else DrawerValue.Closed
                                }
                            }
                        )
                )
            }
        }
    }
}

@Composable
fun HomeScreenDrawer(
    uiState: States
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.sdp)
            .padding(top=60.sdp)
            .background(colorResource(R.color.card_elevated)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally

        ) {
        AsyncImage(
            model = uiState.leetCodeUserInfo.profile?.userAvatar,
            contentDescription = "Example image for demonstration purposes",
            modifier= Modifier.size(100.sdp).clip(RoundedCornerShape(10.sdp))
        )

    }
}

@Composable
fun ScreenContents(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color.Blue,
            Color.White
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, 1000f)
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable(onClick = onClick)
                )
                
                Text(
                    text = "Magic Drawer",
                    style = TextStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                    color = Color.Black,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .padding(start = 0.dp, top = 16.dp, bottom = 12.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@CompletePreviews
@Composable
fun DashboardActivityContentPreview() {
    ScreenContents(onClick = { /* Handle click event */ })
}