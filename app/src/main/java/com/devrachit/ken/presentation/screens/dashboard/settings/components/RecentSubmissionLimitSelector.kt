package com.devrachit.ken.presentation.screens.dashboard.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw600
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.utility.composeUtility.sdp
import com.devrachit.ken.utility.composeUtility.ssp

@Composable
fun RecentSubmissionLimitSelector(
    currentLimit: Int,
    onLimitChanged: (Int) -> Unit
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
            text = "Recent Submissions Count",
            color = colorResource(R.color.white),
            style = TextStyleInter14Lh20Fw600(),
            modifier = Modifier.padding(bottom = 10.sdp)
        )

        Text(
            text = "Select how many recent submissions to fetch (15-20)",
            color = colorResource(R.color.white).copy(alpha = 0.7f),
            style = TextStyleInter12Lh16Fw400(),
            modifier = Modifier.padding(bottom = 12.sdp)
        )
        
        // Number selector with increment/decrement buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.sdp))
                .background(colorResource(R.color.card_elevated).copy(alpha = 0.3f))
                .padding(12.sdp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decrement button
            Text(
                text = "−",
                color = if (currentLimit > 15) {
                    colorResource(R.color.white)
                } else {
                    colorResource(R.color.white).copy(alpha = 0.3f)
                },
                fontSize = 24.ssp,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.sdp))
                    .clickable(enabled = currentLimit > 15) {
                        if (currentLimit > 15) {
                            onLimitChanged(currentLimit - 1)
                        }
                    }
                    .background(
                        if (currentLimit > 15) {
                            colorResource(R.color.white).copy(alpha = 0.1f)
                        } else {
                            colorResource(R.color.card_elevated).copy(alpha = 0.1f)
                        }
                    )
                    .padding(horizontal = 16.sdp, vertical = 8.sdp)
            )
            
            // Current limit display
            Text(
                text = "$currentLimit submissions",
                color = colorResource(R.color.white),
                fontSize = 14.ssp,
                modifier = Modifier.padding(horizontal = 16.sdp)
            )
            
            // Increment button
            Text(
                text = "+",
                color = if (currentLimit < 20) {
                    colorResource(R.color.white)
                } else {
                    colorResource(R.color.white).copy(alpha = 0.3f)
                },
                fontSize = 24.ssp,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.sdp))
                    .clickable(enabled = currentLimit < 20) {
                        if (currentLimit < 20) {
                            onLimitChanged(currentLimit + 1)
                        }
                    }
                    .background(
                        if (currentLimit < 20) {
                            colorResource(R.color.white).copy(alpha = 0.1f)
                        } else {
                            colorResource(R.color.card_elevated).copy(alpha = 0.1f)
                        }
                    )
                    .padding(horizontal = 16.sdp, vertical = 8.sdp)
            )
        }
    }
}
