package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.devrachit.ken.R
import com.devrachit.ken.presentation.navigation.Screen
import com.devrachit.ken.presentation.navigation.navigateToTab
import com.devrachit.ken.presentation.navigation.rememberNavigationItems
import com.devrachit.ken.presentation.screens.dashboard.Widgets.DrawerNavItem
import com.devrachit.ken.presentation.screens.dashboard.Widgets.NavItem
import com.devrachit.ken.ui.theme.TextStyleInter14Lh18Fw400
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw700
import com.devrachit.ken.ui.theme.TextStyleInter24Lh36Fw700
import com.devrachit.ken.utility.composeUtility.ProfilePictureShimmer
import com.devrachit.ken.utility.composeUtility.sdp
import com.devrachit.ken.utility.composeUtility.shadowEffect2
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay

@Composable
fun HomeScreenDrawer(
    username: String,
    uiState: States,
    onClick: () -> Job,
    drawerProgress: Float = 0f,
    navController: NavHostController,
) {
    val yOffset = lerp(0f, -100f, drawerProgress)
    val alpha = lerp(0f, 1f, drawerProgress)
    val xOffset = lerp(-300f, 0f, drawerProgress)

    val coroutineScope = rememberCoroutineScope()

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
                    .clickable(onClick = { onClick() })
            )
        }
        if (!uiState.isLoadingUserInfo ||
            (uiState.leetCodeUserInfo.profile?.realName != null &&
                    uiState.leetCodeUserInfo.profile?.userAvatar != null))
        {
            AsyncImage(
                model = uiState.leetCodeUserInfo.profile?.userAvatar,
                contentDescription = "Example image for demonstration purposes",
                modifier = Modifier
                    .padding(top = 30.sdp, start = 10.sdp)
                    .size(100.sdp)
                    .clip(RoundedCornerShape(10.sdp))
            )
            Text(
                text = username,
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
        Divider(
            modifier = Modifier.padding(vertical = 10.sdp),
        )
        // Get current back stack entry
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val navItems = rememberNavigationItems()
        val currentRoute = navBackStackEntry?.destination?.route ?: Screen.Home.route
        navItems.forEach { (index, itemData) ->
            DrawerNavItem(
                label = itemData.label,
                outlinedIconRes = itemData.outlinedIcon,
                filledIconRes = itemData.filledIcon,
                isSelected = currentRoute == itemData.route,
                onClick = {
                    coroutineScope.launch {
                        onClick.invoke()
                        navigateToTab(navController, itemData.route)
                    }
                },
                drawerProgress = drawerProgress
            )
        }
    }
}