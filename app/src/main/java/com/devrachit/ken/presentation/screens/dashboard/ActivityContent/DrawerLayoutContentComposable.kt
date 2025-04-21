package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import android.util.Log
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.devrachit.ken.utility.composeUtility.sdp
import kotlinx.coroutines.launch

@Composable
fun DrawerLayoutContent(username: String, uiState: States) {
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    
    val drawerWidth = when {
        screenWidthDp >= 600 -> (screenWidthDp * 0.6f).dp.coerceAtMost(400.dp)
        screenWidthDp >= 360 -> (screenWidthDp * 1.50f).dp.coerceAtMost(600.dp)
        else -> screenWidthDp.dp.coerceAtMost(350.dp)
    }

    var drawerState by remember { mutableStateOf(DrawerValue.Closed) }
    
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
        Log.d("DrawerLayoutContent", "toggleDrawer: $drawerState")
        val targetState = if (drawerState == DrawerValue.Closed) DrawerValue.Open else DrawerValue.Closed
        drawerState = targetState
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

        }
    }
    val navController = rememberNavController()
    // Drawer content
    HomeScreenDrawer(
        username = username,
        uiState = uiState,
        onClick = {toggleDrawer.invoke()},
        drawerProgress = drawerProgress,
        navController = navController
    )

    // Main content
    MainContent(
        username = username,
        uiState = uiState,
        drawerProgress = drawerProgress,
        translationX = translationX,
        drawerWidth = drawerWidth,
        draggableState = draggableState,
        onMenuClick = {toggleDrawer.invoke()},
        navController = navController
    )
}