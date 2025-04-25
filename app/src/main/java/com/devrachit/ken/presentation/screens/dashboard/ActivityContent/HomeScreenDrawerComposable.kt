package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.util.lerp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.devrachit.ken.R
import com.devrachit.ken.presentation.navigation.Screen
import com.devrachit.ken.presentation.navigation.rememberNavigationItems
import com.devrachit.ken.presentation.screens.dashboard.Widgets.DrawerNavItem
import com.devrachit.ken.ui.theme.TextStyleInter14Lh18Fw400
import com.devrachit.ken.ui.theme.TextStyleInter24Lh36Fw700
import com.devrachit.ken.utility.composeUtility.ProfilePictureShimmer
import com.devrachit.ken.utility.composeUtility.sdp
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import com.devrachit.ken.utility.composeUtility.ExitAppDialog
import com.devrachit.ken.utility.composeUtility.LogoutDialog

@Composable
fun HomeScreenDrawer(
    username: String,
    uiState: States,
    onClick: () -> Unit,
    drawerProgress: Float = 0f,
    navController: NavHostController,
    navigateAndCloseDrawer: (String) -> Unit,
    logout: () -> Unit
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
                .clickable(onClick = onClick)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Menu",
                tint = Color.White,
                modifier = Modifier
                    .size(24.sdp)

            )
        }
        if (!uiState.isLoadingUserInfo ||
            (uiState.leetCodeUserInfo.profile?.realName != null &&
                    uiState.leetCodeUserInfo.profile?.userAvatar != null)
        ) {
            AsyncImage(
                model = uiState.leetCodeUserInfo.profile?.userAvatar,
                placeholder = painterResource(R.drawable.profile_placeholder),
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
        val showLogoutDialog = remember { mutableStateOf(false) }
        if (showLogoutDialog.value)
            LogoutDialog(
                showDialog = true,
                onConfirmExit = { logout.invoke(); showLogoutDialog.value = false },

                onDismissRequest = { showLogoutDialog.value = false })

        navItems.forEach { (index, itemData) ->
            DrawerNavItem(
                label = itemData.label,
                outlinedIconRes = itemData.outlinedIcon,
                filledIconRes = itemData.filledIcon,
                isSelected = currentRoute == itemData.route,
                onClick = {
                    if (itemData.route == Screen.Logout.route) {
                        showLogoutDialog.value = true
                    } else if (currentRoute != itemData.route) {
                        navigateAndCloseDrawer(itemData.route)
                    } else {
                        onClick()
                    }
                },
                drawerProgress = drawerProgress
            )
        }

    }
}