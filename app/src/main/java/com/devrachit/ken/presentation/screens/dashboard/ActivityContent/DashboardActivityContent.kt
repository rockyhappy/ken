package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import androidx.compose.animation.core.calculateTargetValue
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.devrachit.ken.ui.theme.KenTheme
import kotlinx.coroutines.launch

@Composable
fun DashboardContent()
{
    KenTheme {
        val coroutineScope = rememberCoroutineScope()
        val drawerWidth = 600.dp
        Scaffold(
            modifier = Modifier.Companion
                .fillMaxSize()
                .background(Color.Companion.White)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(paddingValues = innerPadding)
            ){
                var drawerState by remember { mutableStateOf(DrawerValue.Closed) }
                val traslationX = remember {
                    androidx.compose.animation.core.Animatable(0f)
                }
                traslationX.updateBounds(0f, drawerWidth.value)
                val draggableState = rememberDraggableState(
                    onDelta = { dragAmount ->
                        coroutineScope.launch {
                            traslationX.snapTo(traslationX.value + dragAmount)
                        }
                    }
                )
                fun toggleDrawerState() {
                    coroutineScope.launch {
                        if (drawerState == DrawerValue.Open) {
                            traslationX.animateTo(0f)
                            drawerState = DrawerValue.Closed
                        } else {
                            traslationX.animateTo(drawerWidth.value)
                            drawerState = DrawerValue.Open
                        }
                    }
                }
                HomeScreenDrawer()
                val decay = rememberSplineBasedDecay<Float>()
                ScreenContents(
                    onClick = {
                        toggleDrawerState()
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            this.translationX = traslationX.value
                            val scale = lerp(1f, 0.8f, traslationX.value / drawerWidth.value)
                            val corners = if (drawerState == DrawerValue.Open)
                                32.dp else 0.dp
                            this.scaleX = scale
                            this.scaleY = scale
                            this.shape = RoundedCornerShape(topStart = corners, bottomStart = corners)
                        }
                        .draggable(draggableState, Orientation.Horizontal,
                            onDragStopped = { velocity ->
                                val decayX = decay.calculateTargetValue(
                                    traslationX.value,
                                    velocity
                                )
                                coroutineScope.launch {
                                    val targetX = if (decayX > drawerWidth.value / 2) {
                                        drawerWidth.value
                                    } else {
                                        0f
                                    }
                                    val canReachTargetWithDecay =
                                        (decayX > targetX && targetX == drawerWidth.value) || (decayX < targetX && targetX == 0f)
                                    if (canReachTargetWithDecay) {
                                        traslationX.animateDecay(
                                            initialVelocity = velocity,
                                            animationSpec = decay
                                        )
                                    } else {
                                        traslationX.animateTo(targetX, initialVelocity = velocity)
                                    }
                                    drawerState =
                                        if (targetX == drawerWidth.value) DrawerValue.Open else DrawerValue.Closed
                                }

                            }
                        )
                )




            }
        }
    }
}

@Composable
fun HomeScreenDrawer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,

        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        {
            Text(
                text = "Home",
                color = Color.Black,
                modifier = Modifier
                    .background(Color.Blue)
                    .padding(start = 12.dp, top = 12.dp, bottom = 12.dp)
                    .fillMaxWidth()
            )
        }
        Text(text = "Home", modifier = Modifier.padding(top = 8.dp, start = 20.dp))
        Text(text = "Model 3D", modifier = Modifier.padding(top = 16.dp, start = 20.dp))
        Text(text = "Auto Complete ", modifier = Modifier.padding(top = 16.dp, start = 20.dp))
        Text(text = "Magic Drawer", modifier = Modifier.padding(top = 16.dp, start = 20.dp))

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
            .background(Color.White)
//            .clip(RoundedCornerShape(16.dp))
    )
    {



        Column(modifier = Modifier
            .background(gradient)
            .fillMaxSize()
            .clickable { }) {

            Row()
            {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.Black,
                    modifier = Modifier.padding(16.dp).clickable { onClick() }
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
