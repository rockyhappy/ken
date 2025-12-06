package com.devrachit.ken.presentation.screens.dashboard.calendar.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun MonthlyDifficultyStatsCard(
    easyCount: Int,
    mediumCount: Int,
    hardCount: Int,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.sdp))
            .background(colorResource(R.color.card_elevated))
            .padding(vertical = 20.sdp, horizontal = 20.sdp)
    ) {
        Text(
            text = "Difficulty Distribution (This Month)",
            color = colorResource(R.color.white),
            style = TextStyleInter16Lh24Fw700(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.sdp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DifficultyStatColumn(
                label = "Easy",
                count = easyCount,
                total = totalCount,
                color = colorResource(R.color.easy_filled_blue)
            )
            DifficultyStatColumn(
                label = "Medium",
                count = mediumCount,
                total = totalCount,
                color = colorResource(R.color.medium_filled_yellow)
            )
            DifficultyStatColumn(
                label = "Hard",
                count = hardCount,
                total = totalCount,
                color = colorResource(R.color.hard_filled_red)
            )
        }
    }
}

@Composable
private fun DifficultyStatColumn(
    label: String,
    count: Int,
    total: Int,
    color: androidx.compose.ui.graphics.Color
) {
    val percent = if (total > 0) (count * 100 / total) else 0
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(44.sdp)
                .clip(RoundedCornerShape(12.sdp))
                .background(color.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$count",
                color = color,
                style = TextStyleInter16Lh24Fw700(),
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.sdp))
        Text(
            text = label,
            color = color,
            style = TextStyleInter12Lh16Fw400(),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(2.sdp))
        Text(
            text = "$percent%",
            color = colorResource(R.color.white).copy(alpha = 0.7f),
            style = TextStyleInter12Lh16Fw400()
        )
    }
}

