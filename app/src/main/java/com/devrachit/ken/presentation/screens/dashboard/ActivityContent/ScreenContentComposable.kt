package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import android.os.Process
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.devrachit.ken.R
import com.devrachit.ken.utility.composeUtility.ExitAppDialog
import com.devrachit.ken.presentation.navigation.NavGraph
import com.devrachit.ken.presentation.navigation.Screen
import com.devrachit.ken.presentation.navigation.navigateToTab
import com.devrachit.ken.presentation.navigation.rememberNavigationItems
import com.devrachit.ken.presentation.screens.dashboard.Widgets.DashboardHeader
import com.devrachit.ken.presentation.screens.dashboard.Widgets.NavItem
import com.devrachit.ken.utility.composeUtility.sdp
import com.devrachit.ken.utility.composeUtility.shadowEffect
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.Job

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScreenContents(
    username: String,
    uiStates: States,
    modifier: Modifier = Modifier,
    onClick: () -> Job,
    drawerProgress: Float = 0f,
    navController: NavHostController,
    appNavController: NavHostController? = null
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val yOffset = lerp(0f, 100f, drawerProgress)
    val alpha = lerp(1f, 0f, drawerProgress)

    val scale = animateFloatAsState(
        targetValue = if (drawerProgress < 0.5f) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "bottom_row_scale_animation"
    )
    val navItems = rememberNavigationItems()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route

    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = currentRoute == Screen.Home.route) {
        showExitDialog = true
    }

    if (showExitDialog) {
        ExitAppDialog(
            showDialog = showExitDialog,
            onDismissRequest = { showExitDialog = false },
            onConfirmExit = { Process.killProcess(Process.myPid()) }
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(0.dp))
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
                NavGraph(navController = navController, appNavController = appNavController)

                Row(
                    modifier = Modifier
                        .padding(vertical = 28.sdp)
                        .offset(y = yOffset.dp)
                        .shadowEffect()
                        .alpha(alpha)
                        .scale(scale.value)
                        .widthIn(max = (LocalConfiguration.current.screenWidthDp - 24).sdp, min = 300.sdp)
                        .height(70.sdp)
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(36.sdp))
                        .border(
                            border = BorderStroke(
                                width = 2.sdp,
                                color = colorResource(R.color.white).copy(alpha = 0.2f)
                            ),
                            shape = RoundedCornerShape(36.sdp)
                        )
                        .background(colorResource(R.color.card_elevated))
                        .padding(horizontal = 22.sdp, vertical = 8.sdp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    navItems.forEach { (_, itemData) ->
                        if (itemData.route != Screen.Logout.route && itemData.route!= Screen.CompareUsers.route)
                        NavItem(
                            label = itemData.label,
                            outlinedIconRes = itemData.outlinedIcon,
                            filledIconRes = itemData.filledIcon,
                            isSelected = currentRoute == itemData.route,
                            onClick = {
                                navigateToTab(navController, itemData.route)
                            }
                        )
                    }
                }
            }
        }
    }
}
