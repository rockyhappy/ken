package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.QuestionDetails
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionHeader(
    questionDetails: QuestionDetails
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.card_elevated).copy(alpha = 0.3f))
            .padding(20.sdp)
    ) {
        // Title
        Text(
            text = questionDetails.title,
            color = colorResource(R.color.white),
            style = TextStyleInter16Lh24Fw700(),
            modifier = Modifier.padding(bottom = 12.sdp)
        )
        
        // Difficulty, Acceptance Rate, and Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Difficulty Badge
            if (questionDetails.difficulty.isNotEmpty()) {
                Text(
                    text = questionDetails.difficulty,
                    color = when (questionDetails.difficulty.lowercase()) {
                        "easy" -> Color(0xFF00C853)
                        "medium" -> Color(0xFFFFA726)
                        "hard" -> Color(0xFFEF5350)
                        else -> colorResource(R.color.white)
                    },
                    style = TextStyleInter14Lh20Fw400(),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.sdp))
                        .background(
                            when (questionDetails.difficulty.lowercase()) {
                                "easy" -> Color(0xFF00C853).copy(alpha = 0.15f)
                                "medium" -> Color(0xFFFFA726).copy(alpha = 0.15f)
                                "hard" -> Color(0xFFEF5350).copy(alpha = 0.15f)
                                else -> colorResource(R.color.card_elevated).copy(alpha = 0.3f)
                            }
                        )
                        .padding(horizontal = 8.sdp, vertical = 4.sdp)
                )
            }
            
            // Acceptance Rate
            if (questionDetails.acceptanceRate.isNotEmpty()) {
                Text(
                    text = "✓ ${questionDetails.acceptanceRate}",
                    color = colorResource(R.color.white).copy(alpha = 0.7f),
                    style = TextStyleInter12Lh16Fw400()
                )
            }
            
            // Likes and Dislikes
            if (questionDetails.likes > 0) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "👍 ${formatCount(questionDetails.likes)}",
                        color = colorResource(R.color.white).copy(alpha = 0.7f),
                        style = TextStyleInter12Lh16Fw400()
                    )
                    if (questionDetails.dislikes > 0) {
                        Text(
                            text = "👎 ${formatCount(questionDetails.dislikes)}",
                            color = colorResource(R.color.white).copy(alpha = 0.7f),
                            style = TextStyleInter12Lh16Fw400()
                        )
                    }
                }
            }
        }
        
        // Topic Tags
        if (questionDetails.topicTags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.sdp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.sdp)
            ) {
                questionDetails.topicTags.forEach { tag ->
                    Text(
                        text = tag,
                        color = Color(0xFF42A5F5),
                        style = TextStyleInter12Lh16Fw400(),
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.sdp))
                            .background(Color(0xFF42A5F5).copy(alpha = 0.15f))
                            .padding(horizontal = 8.sdp, vertical = 4.sdp)
                    )
                }
            }
        }
    }
}

private fun formatCount(count: Int): String {
    return when {
        count >= 1000 -> "${count / 1000}K"
        else -> count.toString()
    }
}
