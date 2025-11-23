package com.devrachit.ken.presentation.screens.dashboard.questions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.Question
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw700
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionItemCard(
    question: Question,
    onQuestionClick: (Question) -> Unit = {},
    onStatusUpdate: (Int, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.sdp, vertical = 8.sdp)
            .clickable { onQuestionClick(question) },
        elevation = CardDefaults.cardElevation(4.sdp),
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.card_elevated)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.sdp)
        ) {
            // Header with title and difficulty
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${question.frontendQuestionId}. ${question.title}",
                    style = TextStyleInter16Lh24Fw700(),
                    color = colorResource(R.color.white),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(8.sdp))

                DifficultyChip(difficulty = question.difficulty)
            }

            Spacer(modifier = Modifier.height(8.sdp))

            // Status and acceptance rate
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusChip(
                    status = question.status,
                    onStatusClick = { newStatus ->
                        onStatusUpdate(question.id, newStatus)
                    }
                )

                Text(
                    text = "${String.format("%.1f", question.acceptanceRate)}% accepted",
                    style = TextStyleInter12Lh16Fw700(),
                    color = colorResource(R.color.grayblue_normal_600)
                )
            }

            // Topic tags
            if (question.topicTags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.sdp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.sdp)
                ) {
                    items(question.topicTags.take(3)) { tag ->
                        TopicTagChip(tag = tag.name)
                    }
                    if (question.topicTags.size > 3) {
                        item {
                            Text(
                                text = "+${question.topicTags.size - 3} more",
                                style = TextStyleInter12Lh16Fw700(),
                                color = colorResource(R.color.grayblue_normal_600),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            // Likes and premium indicator
            if (question.isPaidOnly || question.likes > 0) {
                Spacer(modifier = Modifier.height(8.sdp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (question.likes > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "👍 ${question.likes}",
                                style = TextStyleInter12Lh16Fw700(),
                                color = colorResource(R.color.grayblue_normal_600)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    if (question.isPaidOnly) {
                        Text(
                            text = "Premium",
                            style = TextStyleInter12Lh16Fw700(),
                            color = colorResource(R.color.yellow_normal_500),
                            modifier = Modifier
                                .background(
                                    colorResource(R.color.yellow_normal_500).copy(alpha = 0.1f),
                                    RoundedCornerShape(4.sdp)
                                )
                                .padding(horizontal = 6.sdp, vertical = 2.sdp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DifficultyChip(difficulty: String) {
    val (color, backgroundColor) = when (difficulty.lowercase()) {
        "easy" -> colorResource(R.color.green_normal_500) to colorResource(R.color.green_normal_500).copy(alpha = 0.1f)
        "medium" -> colorResource(R.color.yellow_normal_500) to colorResource(R.color.yellow_normal_500).copy(alpha = 0.1f)
        "hard" -> colorResource(R.color.red_normal_500) to colorResource(R.color.red_normal_500).copy(alpha = 0.1f)
        else -> colorResource(R.color.grayblue_normal_600) to colorResource(R.color.grayblue_normal_600).copy(alpha = 0.1f)
    }

    Text(
        text = difficulty,
        style = TextStyleInter12Lh16Fw700(),
        color = color,
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(6.sdp))
            .padding(horizontal = 8.sdp, vertical = 4.sdp)
    )
}

@Composable
private fun StatusChip(
    status: String?,
    onStatusClick: (String) -> Unit
) {
    val (text, color, backgroundColor) = when (status) {
        "Solved" -> Triple("✓ Solved", colorResource(R.color.green_normal_500), colorResource(R.color.green_normal_500).copy(alpha = 0.1f))
        "Attempted" -> Triple("○ Attempted", colorResource(R.color.yellow_normal_500), colorResource(R.color.yellow_normal_500).copy(alpha = 0.1f))
        else -> Triple("Not Started", colorResource(R.color.grayblue_normal_600), colorResource(R.color.grayblue_normal_600).copy(alpha = 0.1f))
    }

    Text(
        text = text,
        style = TextStyleInter12Lh16Fw700(),
        color = color,
        modifier = Modifier
            .clickable {
                val newStatus = when (status) {
                    "Solved" -> "Attempted"
                    "Attempted" -> "Not Started"
                    else -> "Solved"
                }
                onStatusClick(newStatus)
            }
            .background(backgroundColor, RoundedCornerShape(6.sdp))
            .padding(horizontal = 8.sdp, vertical = 4.sdp)
    )
}

@Composable
private fun TopicTagChip(tag: String) {
    Text(
        text = tag,
        style = TextStyleInter12Lh16Fw700(),
        color = colorResource(R.color.blue_normal_500),
        fontSize = 10.sp,
        modifier = Modifier
            .background(
                colorResource(R.color.blue_normal_500).copy(alpha = 0.1f),
                RoundedCornerShape(4.sdp)
            )
            .padding(horizontal = 6.sdp, vertical = 2.sdp)
    )
}
