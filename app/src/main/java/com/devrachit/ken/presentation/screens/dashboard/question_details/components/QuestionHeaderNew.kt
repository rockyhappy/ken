package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.QuestionDetails
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionHeaderNew(
    questionDetails: QuestionDetails
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.sdp, vertical = 20.sdp)
            .clip(RoundedCornerShape(36.sdp))
            .border(
                border = BorderStroke(
                    width = 2.sdp,
                    color = colorResource(R.color.card_elevated)
                ),
                shape = RoundedCornerShape(36.sdp)
            )
            .padding(20.sdp)
    ) {
        // Title
        Text(
            text = questionDetails.title,
            color = colorResource(R.color.white),
            style = TextStyleInter20Lh24Fw700(),
            modifier = Modifier.padding(bottom = 12.sdp),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        
        // Difficulty, Acceptance Rate, and Stats - First Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.sdp)
        ) {
            // Difficulty Badge
            if (questionDetails.difficulty.isNotEmpty()) {
                Text(
                    text = questionDetails.difficulty,
                    color = when (questionDetails.difficulty.lowercase()) {
                        "easy" -> colorResource(R.color.easy_filled_blue)
                        "medium" -> colorResource(R.color.medium_filled_yellow)
                        "hard" -> colorResource(R.color.hard_filled_red)
                        else -> colorResource(R.color.white)
                    },
                    style = TextStyleInter12Lh16Fw400(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.sdp))
                        .border(
                            border = BorderStroke(
                                width = 1.sdp,
                                color = when (questionDetails.difficulty.lowercase()) {
                                    "easy" -> colorResource(R.color.easy_filled_blue)
                                    "medium" -> colorResource(R.color.medium_filled_yellow)
                                    "hard" -> colorResource(R.color.hard_filled_red)
                                    else -> colorResource(R.color.white).copy(alpha = 0.3f)
                                }
                            ),
                            shape = RoundedCornerShape(12.sdp)
                        )
                        .padding(horizontal = 10.sdp, vertical = 4.sdp)
                )
            }
            
            // Acceptance Rate - Always show with debug
            Text(
                text = if (questionDetails.acceptanceRate.isNotEmpty()) 
                    "✓ ${questionDetails.acceptanceRate}" 
                else 
                    "✓ N/A",
                color = colorResource(R.color.white).copy(alpha = 0.8f),
                style = TextStyleInter12Lh16Fw400(),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.sdp))
                    .border(
                        border = BorderStroke(
                            width = 1.sdp,
                            color = colorResource(R.color.white).copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(12.sdp)
                    )
                    .padding(horizontal = 10.sdp, vertical = 4.sdp)
            )
            
            // Likes - Always show
            Text(
                text = "👍 ${formatCountNew(questionDetails.likes)}",
                color = colorResource(R.color.white).copy(alpha = 0.8f),
                style = TextStyleInter12Lh16Fw400(),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.sdp))
                    .border(
                        border = BorderStroke(
                            width = 1.sdp,
                            color = colorResource(R.color.white).copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(12.sdp)
                    )
                    .padding(horizontal = 10.sdp, vertical = 4.sdp)
            )
            
            // Dislikes - Always show
            Text(
                text = "👎 ${formatCountNew(questionDetails.dislikes)}",
                color = colorResource(R.color.white).copy(alpha = 0.8f),
                style = TextStyleInter12Lh16Fw400(),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.sdp))
                    .border(
                        border = BorderStroke(
                            width = 1.sdp,
                            color = colorResource(R.color.white).copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(12.sdp)
                    )
                    .padding(horizontal = 10.sdp, vertical = 4.sdp)
            )
        }
        
        // Submission Stats - Second Row
        if (questionDetails.totalSubmissions.isNotEmpty() || questionDetails.totalAccepted.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.sdp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.sdp)
            ) {
                if (questionDetails.totalAccepted.isNotEmpty()) {
                    Text(
                        text = "${questionDetails.totalAccepted} Accepted",
                        color = colorResource(R.color.green_normal_500).copy(alpha = 0.9f),
                        style = TextStyleInter12Lh16Fw400(),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.sdp))
                            .border(
                                border = BorderStroke(
                                    width = 1.sdp,
                                    color = colorResource(R.color.green_normal_500).copy(alpha = 0.3f)
                                ),
                                shape = RoundedCornerShape(12.sdp)
                            )
                            .padding(horizontal = 10.sdp, vertical = 4.sdp)
                    )
                }
                
                if (questionDetails.totalSubmissions.isNotEmpty()) {
                    Text(
                        text = "${questionDetails.totalSubmissions} Submissions",
                        color = colorResource(R.color.white).copy(alpha = 0.7f),
                        style = TextStyleInter12Lh16Fw400(),
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.sdp))
                            .border(
                                border = BorderStroke(
                                    width = 1.sdp,
                                    color = colorResource(R.color.white).copy(alpha = 0.2f)
                                ),
                                shape = RoundedCornerShape(12.sdp)
                            )
                            .padding(horizontal = 10.sdp, vertical = 4.sdp)
                    )
                }
            }
        }
        
        // Topic Tags
        if (questionDetails.topicTags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.sdp))
            // Simple wrapping row for tags
            Column {
                var currentRow = mutableListOf<String>()
                questionDetails.topicTags.forEachIndexed { index, tag ->
                    currentRow.add(tag)
                    if (currentRow.size == 3 || index == questionDetails.topicTags.lastIndex) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 6.sdp),
                            horizontalArrangement = Arrangement.spacedBy(6.sdp)
                        ) {
                            currentRow.forEach { currentTag ->
                                Text(
                                    text = currentTag,
                                    color = colorResource(R.color.blue_normal_500),
                                    style = TextStyleInter12Lh16Fw400(),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.sdp))
                                        .border(
                                            border = BorderStroke(
                                                width = 1.sdp,
                                                color = colorResource(R.color.blue_normal_500).copy(alpha = 0.3f)
                                            ),
                                            shape = RoundedCornerShape(12.sdp)
                                        )
                                        .padding(horizontal = 8.sdp, vertical = 4.sdp)
                                )
                            }
                        }
                        currentRow = mutableListOf()
                    }
                }
            }
        }
    }
}

private fun formatCountNew(count: Int): String {
    return when {
        count >= 1000 -> "${count / 1000}K"
        else -> count.toString()
    }
}
