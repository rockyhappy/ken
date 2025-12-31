package com.devrachit.ken.presentation.screens.dashboard.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.NavigationItems
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw600
import com.devrachit.ken.utility.composeUtility.sdp

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
            // Settings is always in side nav, so make it read-only
            NavigationItemOption(
                item = item,
                isSelected = currentSideNavItems.contains(item) || item == NavigationItems.SETTINGS,
                isEnabled = item != NavigationItems.SETTINGS,
                onSelectionChanged = { isSelected ->
                    val updatedList = if (isSelected) {
                        if (!currentSideNavItems.contains(item)) {
                            currentSideNavItems + item
                        } else currentSideNavItems
                    } else {
                        currentSideNavItems.filter { it != item }
                    }
                    onSideNavItemsChanged(updatedList)
                }
            )
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isEnabled) {
                if (isEnabled) onSelectionChanged(!isSelected)
            }
            .padding(vertical = 4.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            enabled = isEnabled,
            onCheckedChange = { if (isEnabled) onSelectionChanged(it) },
            colors = CheckboxDefaults.colors(
                checkedColor = colorResource(R.color.blue_normal_500),
                uncheckedColor = Color.White.copy(alpha = 0.6f),
                checkmarkColor = Color.White,
                disabledCheckedColor = colorResource(R.color.blue_normal_500).copy(alpha = 0.5f),
                disabledUncheckedColor = Color.White.copy(alpha = 0.3f)
            )
        )

        Spacer(modifier = Modifier.width(8.sdp))

        Text(
            text = item,
            color = if (isEnabled) Color.White else Color.White.copy(alpha = 0.5f),
            style = TextStyleInter12Lh16Fw400(),
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )

        if (item == NavigationItems.SETTINGS) {
            Spacer(modifier = Modifier.width(8.sdp))
            Text(
                text = "(Always visible)",
                color = Color.White.copy(alpha = 0.5f),
                style = TextStyleInter12Lh16Fw400()
            )
        }
    }
}
