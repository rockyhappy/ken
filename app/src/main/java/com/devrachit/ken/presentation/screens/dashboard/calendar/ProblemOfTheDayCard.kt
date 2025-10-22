package com.devrachit.ken.presentation.screens.dashboard.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw500
import com.devrachit.ken.utility.composeUtility.sdp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ProblemOfTheDayCard(
    challenge: DailyChallenge,
    date: LocalDate,
    onQuestionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = colorResource(R.color.blue_normal_500)
    val isToday = date == LocalDate.now()
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.sdp))
            .background(colorResource(R.color.card_elevated))
            .border(
                border = BorderStroke(
                    width = 1.sdp,
                    color = borderColor
                ),
                shape = RoundedCornerShape(16.sdp)
            )
            .clickable { onQuestionClick(challenge.question.titleSlug) }
            .padding(16.sdp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isToday) "Problem of the Day" else "Daily Challenge",
                color = colorResource(R.color.blue_normal_500),
                style = TextStyleInter12Lh16Fw400(),
                fontWeight = FontWeight.SemiBold
            )
            
            Text(
                text = date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                color = colorResource(R.color.white).copy(alpha = 0.7f),
                style = TextStyleInter12Lh16Fw400()
            )
        }
        
        Spacer(modifier = Modifier.height(12.sdp))
        
        // Question ID Badge
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.sdp))
                    .background(colorResource(R.color.blue_normal_500).copy(alpha = 0.2f))
                    .padding(horizontal = 10.sdp, vertical = 4.sdp)
            ) {
                Text(
                    text = "#${challenge.question.questionFrontendId}",
                    color = colorResource(R.color.blue_normal_500),
                    style = TextStyleInter12Lh16Fw400(),
                    fontWeight = FontWeight.Bold
                )
            }
            
            // Status Badge
            val statusColor = when (challenge.userStatus) {
                "Finish" -> colorResource(R.color.green_normal_500)
                "NotStart" -> colorResource(R.color.white).copy(alpha = 0.5f)
                else -> colorResource(R.color.yellow_normal_500)
            }
            
            val statusText = when (challenge.userStatus) {
                "Finish" -> "Completed"
                "NotStart" -> "Not Started"
                else -> challenge.userStatus
            }
            
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.sdp))
                    .background(statusColor.copy(alpha = 0.2f))
                    .padding(horizontal = 10.sdp, vertical = 4.sdp)
            ) {
                Text(
                    text = statusText,
                    color = statusColor,
                    style = TextStyleInter12Lh16Fw400(),
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.sdp))
        
        // Question Title
        Text(
            text = challenge.question.title,
            color = colorResource(R.color.white),
            style = TextStyleInter16Lh24Fw500(),
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.sdp))
        
        // Call to action
        Text(
            text = "Tap to view problem details →",
            color = colorResource(R.color.blue_normal_500),
            style = TextStyleInter14Lh20Fw400(),
            fontWeight = FontWeight.Medium
        )
    }
}
