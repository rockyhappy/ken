package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import com.devrachit.ken.ui.theme.KenTheme
import kotlinx.coroutines.launch
import com.devrachit.ken.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

@Composable
fun DashboardContent(
    username: String,
    uiState: States,
) {
    KenTheme {
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
                DrawerLayoutContent(username = username, uiState = uiState)
            }
        }
    }
}

@Composable
fun MainContent(
    username: String,
    uiState: States,
    drawerProgress: Float,
    translationX: Animatable<Float, AnimationVector1D>,
    drawerWidth: Dp,
    draggableState: DraggableState,
    onMenuClick: () -> Job,
    navController: NavHostController,
) {
    val coroutineScope = rememberCoroutineScope()

    ScreenContents(
        username = username,
        uiStates = uiState,
        onClick = onMenuClick,
        drawerProgress = drawerProgress,
        navController = navController,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                applyDrawerTransformation(this, translationX.value, drawerWidth.value)
            }
            .draggable(
                state = draggableState,
                orientation = Orientation.Horizontal,
                onDragStopped = { velocity ->
                    handleDragStopped(
                        coroutineScope = coroutineScope,
                        translationX = translationX,
                        drawerWidth = drawerWidth,
                        velocity = velocity
                    )
                }
            )
    )
}

fun applyDrawerTransformation(
    graphicsLayer: androidx.compose.ui.graphics.GraphicsLayerScope,
    translationValue: Float,
    drawerWidthValue: Float
) {
    with(graphicsLayer) {
        shadowElevation = translationValue * 0.1f

        val progress = (translationValue / drawerWidthValue).coerceIn(0f, 1f)

        translationX = translationValue

        val scale = lerp(1f, 0.9f, progress)
        scaleX = scale
        scaleY = scale

        val cornerRadius = lerp(0f, 24f, progress)
        shape = RoundedCornerShape(cornerRadius.dp)

        clip = progress > 0
    }
}

@Composable
fun createDraggableState(
    coroutineScope: CoroutineScope,
    translationX: androidx.compose.animation.core.Animatable<Float, androidx.compose.animation.core.AnimationVector1D>,
    drawerWidth: androidx.compose.ui.unit.Dp
): androidx.compose.foundation.gestures.DraggableState {
    return rememberDraggableState(onDelta = { dragAmount ->
        coroutineScope.launch {
            translationX.snapTo(
                (translationX.value + dragAmount).coerceIn(0f, drawerWidth.value)
            )
        }
    })
}

fun toggleDrawer(
    coroutineScope: CoroutineScope,
    translationX: androidx.compose.animation.core.Animatable<Float, androidx.compose.animation.core.AnimationVector1D>,
    currentDrawerState: DrawerValue,
    updateDrawerState: (DrawerValue) -> Unit
) {
    coroutineScope.launch {
        if (currentDrawerState == DrawerValue.Open) {
            translationX.animateTo(
                0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            updateDrawerState(DrawerValue.Closed)
        } else {
            translationX.animateTo(
                translationX.upperBound ?: 0f,
                animationSpec = tween(durationMillis = 400)
            )
            updateDrawerState(DrawerValue.Open)
        }
    }
}

fun handleDragStopped(
    coroutineScope: CoroutineScope,
    translationX: androidx.compose.animation.core.Animatable<Float, androidx.compose.animation.core.AnimationVector1D>,
    drawerWidth: androidx.compose.ui.unit.Dp,
    velocity: Float
) {
    val targetThreshold = drawerWidth.value * 0.5f
    val targetState = if (translationX.value > targetThreshold ||
        (translationX.value > drawerWidth.value * 0.1f && velocity > 800)
    ) {
        DrawerValue.Open
    } else {
        DrawerValue.Closed
    }

    snapDrawerToState(
        coroutineScope = coroutineScope,
        translationX = translationX,
        targetState = targetState,
        drawerWidth = drawerWidth,
        velocity = velocity
    )
}

private fun snapDrawerToState(
    coroutineScope: CoroutineScope,
    translationX: androidx.compose.animation.core.Animatable<Float, androidx.compose.animation.core.AnimationVector1D>,
    targetState: DrawerValue,
    drawerWidth: androidx.compose.ui.unit.Dp,
    velocity: Float = 0f
) {
    coroutineScope.launch {
        val targetValue = if (targetState == DrawerValue.Open) drawerWidth.value else 0f

        translationX.animateTo(
            targetValue = targetValue,
            initialVelocity = velocity,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    }
}
