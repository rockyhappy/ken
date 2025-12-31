package com.devrachit.ken.presentation.screens.dashboard.settings.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.NavigationItems
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw600
import com.devrachit.ken.utility.composeUtility.sdp
import com.devrachit.ken.utility.composeUtility.ssp

@Composable
fun NavigationPreferencesSelector(
    currentBottomNavItems: List<String>,
    currentSideNavItems: List<String>,
    onBottomNavItemsChanged: (List<String>) -> Unit,
    onSideNavItemsChanged: (List<String>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.sdp))
            .border(
                border = BorderStroke(
                    width = 2.sdp,
                    color = colorResource(R.color.card_elevated)
                ),
                shape = RoundedCornerShape(16.sdp)
            )
            .background(color = colorResource(R.color.card_elevated).copy(alpha = 0.3f))
            .padding(14.sdp)
    ) {
        Text(
            text = "Navigation Preferences",
            color = colorResource(R.color.white),
            style = TextStyleInter14Lh20Fw600(),
            modifier = Modifier.padding(bottom = 10.sdp)
        )

        Text(
            text = "Choose which navigation items appear in the bottom navigation and side drawer.",
            color = colorResource(R.color.white).copy(alpha = 0.7f),
            style = TextStyleInter12Lh16Fw400(),
            modifier = Modifier.padding(bottom = 16.sdp)
        )

        // Bottom Navigation Section
        Text(
            text = "Bottom Navigation",
            color = colorResource(R.color.white),
            style = TextStyleInter14Lh20Fw600(),
            modifier = Modifier.padding(bottom = 8.sdp)
        )

        NavigationItems.ALL_ITEMS.forEach { item ->
            // Don't show Settings in bottom nav options (it's always in side nav)
            if (item != NavigationItems.SETTINGS) {
                NavigationItemOption(
                    item = item,
                    isSelected = currentBottomNavItems.contains(item),
                    onSelectionChanged = { isSelected ->
                        val updatedList = if (isSelected) {
                            if (!currentBottomNavItems.contains(item)) {
                                currentBottomNavItems + item
                            } else currentBottomNavItems
                        } else {
                            currentBottomNavItems.filter { it != item }
                        }
                        onBottomNavItemsChanged(updatedList)
                    }
                )

                val bottomNavItems = NavigationItems.ALL_ITEMS.filter { it != NavigationItems.SETTINGS }
                if (item != bottomNavItems.last()) {
                    Spacer(modifier = Modifier.height(8.sdp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.sdp))

        // Side Navigation Section
        Text(
            text = "Side Navigation",
            color = colorResource(R.color.white),
            style = TextStyleInter14Lh20Fw600(),
            modifier = Modifier.padding(bottom = 8.sdp)
        )

        NavigationItems.ALL_ITEMS.forEach { item ->
            NavigationItemOption(
                item = item,
                isSelected = currentSideNavItems.contains(item) || item == NavigationItems.SETTINGS,
                isEnabled = item != NavigationItems.SETTINGS,
                onSelectionChanged = { isSelected ->
                    if (item != NavigationItems.SETTINGS) {
                        val updatedList = if (isSelected) {
                            if (!currentSideNavItems.contains(item)) {
                                currentSideNavItems + item
                            } else currentSideNavItems
                        } else {
                            currentSideNavItems.filter { it != item }
                        }
                        onSideNavItemsChanged(updatedList)
                    }
                }
            )

            if (item != NavigationItems.ALL_ITEMS.last()) {
                Spacer(modifier = Modifier.height(8.sdp))
            }
        }
    }
}

@Composable
private fun NavigationItemOption(
    item: String,
    isSelected: Boolean,
    isEnabled: Boolean = true,
    onSelectionChanged: (Boolean) -> Unit
) {
    val backgroundColor by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(300),
        label = "background_alpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = tween(300),
        label = "scale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(12.sdp))
            .background(
                color = colorResource(R.color.card_elevated)
                    .copy(alpha = 0.3f + (backgroundColor * 0.4f))
            )
            .border(
                border = BorderStroke(
                    width = if (isSelected) 2.sdp else 1.sdp,
                    color = if (isSelected) {
                        colorResource(R.color.white)
                    } else {
                        colorResource(R.color.card_elevated)
                    }
                ),
                shape = RoundedCornerShape(12.sdp)
            )
            .clickable(enabled = isEnabled) {
                if (isEnabled) onSelectionChanged(!isSelected)
            }
            .padding(12.sdp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(32.sdp)
                    .clip(RoundedCornerShape(8.sdp))
                    .background(
                        if (isSelected) {
                            colorResource(R.color.white).copy(alpha = 0.15f)
                        } else {
                            Color.Transparent
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(getNavigationItemIcon(item)),
                    contentDescription = item,
                    tint = if (isEnabled) Color.White else Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(18.sdp)
                )
            }

            Spacer(modifier = Modifier.padding(horizontal = 8.sdp))

            Column {
                Text(
                    text = item,
                    color = if (isEnabled) Color.White else Color.White.copy(alpha = 0.5f),
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 14.ssp
                )

                if (item == NavigationItems.SETTINGS) {
                    Text(
                        text = "Always visible in side navigation",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.ssp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        // Selection indicator
        Box(
            modifier = Modifier
                .size(20.sdp)
                .clip(RoundedCornerShape(10.sdp))
                .background(
                    if (isSelected) {
                        colorResource(R.color.white)
                    } else {
                        Color.Transparent
                    }
                )
                .border(
                    border = BorderStroke(
                        width = 1.sdp,
                        color = Color.White.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(10.sdp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(10.sdp)
                        .clip(RoundedCornerShape(5.sdp))
                        .background(colorResource(R.color.card_elevated))
                )
            }
        }
    }
}

private fun getNavigationItemIcon(item: String): Int {
    return when (item) {
        NavigationItems.HOME -> R.drawable.ic_home_outlined
        NavigationItems.FRIENDS -> R.drawable.ic_friends_outlined
        NavigationItems.QUESTIONS -> R.drawable.ic_questions_outlined
        NavigationItems.TRACK -> R.drawable.ic_calender_outlined
        NavigationItems.COMPARE -> R.drawable.ic_compare_outlined
        NavigationItems.SHEETS -> R.drawable.ic_sheets_outlined
        NavigationItems.SETTINGS -> R.drawable.ic_settings_outlined
        NavigationItems.LOGOUT -> R.drawable.ic_logout_outlined
        else -> R.drawable.ic_home_outlined
    }
}
