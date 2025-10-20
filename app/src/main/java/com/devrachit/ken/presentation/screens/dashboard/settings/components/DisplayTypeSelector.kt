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
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw600
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun DisplayTypeSelector(
    currentDisplayType: String,
    onDisplayTypeChanged: (String) -> Unit
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
            text = "Friends Display Type",
            color = colorResource(R.color.white),
            style = TextStyleInter14Lh20Fw600(),
            modifier = Modifier.padding(bottom = 10.sdp)
        )

        Text(
            text = "Choose how you want to view your friends list in the Friends section.",
            color = colorResource(R.color.white).copy(alpha = 0.7f),
            style = TextStyleInter12Lh16Fw400(),
            modifier = Modifier.padding(bottom = 12.sdp)
        )
        
        DisplayType.entries.forEach { displayType ->
            DisplayTypeOption(
                displayType = displayType,
                isSelected = currentDisplayType == displayType.name,
                onSelect = { onDisplayTypeChanged(displayType.name) }
            )
            
            if (displayType != DisplayType.entries.last()) {
                Spacer(modifier = Modifier.height(8.sdp))
            }
        }
    }
}
