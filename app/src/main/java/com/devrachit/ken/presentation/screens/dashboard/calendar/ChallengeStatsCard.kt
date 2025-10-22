package com.devrachit.ken.presentation.screens.dashboard.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.devrachit.ken.domain.models.DailyChallenge
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw600
import com.devrachit.ken.utility.composeUtility.sdp
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun ChallengeStatsCard(
    dailyChallenges: Map<LocalDate, DailyChallenge>,
    displayedMonth: YearMonth,
    modifier: Modifier = Modifier
) {
    val completedCount = dailyChallenges.values.count { it.userStatus == "Finish" }
    val totalCount = dailyChallenges.size
    val notStartedCount = dailyChallenges.values.count { it.userStatus == "NotStart" }
    val completionRate = if (totalCount > 0) (completedCount * 100) / totalCount else 0
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.sdp))
            .background(colorResource(R.color.card_elevated))
            .border(
                width = 1.sdp,
                color = colorResource(R.color.white).copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.sdp)
            )
            .padding(16.sdp)
    ) {
        Text(
            text = "${displayedMonth.month.name.take(3)} ${displayedMonth.year} Stats",
            color = colorResource(R.color.white),
            style = TextStyleInter16Lh24Fw700(),
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.sdp))
        
        // Stats Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                value = totalCount.toString(),
                label = "Total Challenges",
                color = colorResource(R.color.blue_normal_500),
                modifier = Modifier.weight(1f)
            )
            
            StatItem(
                value = completedCount.toString(),
                label = "Completed",
                color = colorResource(R.color.green_normal_500),
                modifier = Modifier.weight(1f)
            )
            
            StatItem(
                value = notStartedCount.toString(),
                label = "Not Started",
                color = colorResource(R.color.white).copy(alpha = 0.5f),
                modifier = Modifier.weight(1f)
            )
        }
        
        if (totalCount > 0) {
            Spacer(modifier = Modifier.height(16.sdp))
            
            // Completion Rate
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Completion Rate: ",
                    color = colorResource(R.color.white).copy(alpha = 0.7f),
                    style = TextStyleInter14Lh20Fw400()
                )
                
                Text(
                    text = "$completionRate%",
                    color = when {
                        completionRate >= 80 -> colorResource(R.color.green_normal_500)
                        completionRate >= 50 -> colorResource(R.color.yellow_normal_500)
                        else -> colorResource(R.color.white).copy(alpha = 0.5f)
                    },
                    style = TextStyleInter16Lh24Fw700(),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            color = color,
            style = TextStyleInter20Lh24Fw600(),
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(4.sdp))
        
        Text(
            text = label,
            color = colorResource(R.color.white).copy(alpha = 0.6f),
            style = TextStyleInter12Lh16Fw400()
        )
    }
}
