package com.devrachit.ken.presentation.screens.dashboard.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun BadgeDisplayModeSelector(
    currentBadgeDisplayMode: String,
    onBadgeDisplayModeChanged: (String) -> Unit
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
            .padding(20.sdp)
    ) {
        Text(
            text = "Badge Display Mode",
            color = colorResource(R.color.white),
            style = TextStyleInter20Lh24Fw700(),
            modifier = Modifier.padding(bottom = 16.sdp)
        )

        Text(
            text = "Choose how badges appear when clicked",
            color = colorResource(R.color.white).copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 20.sdp)
        )
        
        BadgeDisplayMode.entries.forEach { displayMode ->
            BadgeDisplayModeOption(
                displayMode = displayMode,
                isSelected = currentBadgeDisplayMode == displayMode.name,
                onSelect = { onBadgeDisplayModeChanged(displayMode.name) }
            )
            
            if (displayMode != BadgeDisplayMode.entries.last()) {
                Spacer(modifier = Modifier.height(12.sdp))
            }
        }
    }
}
