package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.devrachit.ken.utility.composeUtility.sdp
import kotlinx.coroutines.launch

@Composable
fun DrawerLayoutContent(username: String, uiState: States) {
    val coroutineScope = rememberCoroutineScope()
    val drawerWidth = 700.sdp

    // Create a drawer state
    var drawerState by remember { mutableStateOf(DrawerValue.Closed) }
    
    // Create an animatable for the drawer translation
    val translationX = remember {
        Animatable(0f)
    }
    translationX.updateBounds(0f, drawerWidth.value)

    // Calculate drawer progress based on translation
    val drawerProgress = (translationX.value / drawerWidth.value).coerceIn(0f, 1f)
    
    // Create a draggable state for handling drag gestures
    val draggableState = createDraggableState(
        coroutineScope = coroutineScope,
        translationX = translationX,
        drawerWidth = drawerWidth
    )

    // Toggle drawer handler
    val toggleDrawer = {
        val targetState = if (drawerState == DrawerValue.Closed) DrawerValue.Open else DrawerValue.Closed
        coroutineScope.launch {
            if (targetState == DrawerValue.Open) {
                translationX.animateTo(
                    drawerWidth.value,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            } else {
                translationX.animateTo(
                    0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            drawerState = targetState
        }
    }

    // Drawer content
    HomeScreenDrawer(
        username = username,
        uiState = uiState,
        onClick = toggleDrawer
    )

    // Main content
    MainContent(
        username = username,
        uiState = uiState,
        drawerProgress = drawerProgress,
        translationX = translationX,
        drawerWidth = drawerWidth,
        draggableState = draggableState,
        onMenuClick = toggleDrawer
    )
}