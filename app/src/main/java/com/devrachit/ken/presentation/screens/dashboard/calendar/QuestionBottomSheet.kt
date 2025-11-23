package com.devrachit.ken.presentation.screens.dashboard.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
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
import com.devrachit.ken.utility.composeUtility.sdp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionBottomSheet(
    challenge: DailyChallenge,
    onDismiss: () -> Unit,
    onOpenQuestion: (String) -> Unit
) {
    // Difficulty color
    val difficultyColor = when (challenge.question.difficulty.lowercase()) {
        "easy" -> colorResource(R.color.easy_filled_blue)
        "medium" -> colorResource(R.color.medium_filled_yellow)
        "hard" -> colorResource(R.color.hard_filled_red)
        else -> colorResource(R.color.white).copy(alpha = 0.7f)
    }
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colorResource(R.color.card_elevated),
        contentColor = colorResource(R.color.white),
        tonalElevation = 0.sdp,
        shape = RoundedCornerShape(topStart = 20.sdp, topEnd = 20.sdp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.sdp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(40.sdp)
                        .height(4.sdp)
                        .clip(RoundedCornerShape(2.sdp))
                        .background(colorResource(R.color.white).copy(alpha = 0.3f))
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.sdp, vertical = 16.sdp)
        ) {
            // Header with close button
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
                Text(
                    text = "Daily Challenge",
                    color = colorResource(R.color.blue_normal_500),
                    style = TextStyleInter12Lh16Fw400(),
                    fontWeight = FontWeight.SemiBold
                )
//
//                IconButton(
//                    onClick = onDismiss,
//                    modifier = Modifier.size(32.sdp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Close,
//                        contentDescription = "Close",
//                        tint = colorResource(R.color.white).copy(alpha = 0.7f),
//                        modifier = Modifier.size(20.sdp)
//                    )
//                }
//            }

            Spacer(modifier = Modifier.height(8.sdp))

            // Question ID Badge and Difficulty
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
                
                // Difficulty Badge
                if (challenge.question.difficulty.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.sdp))
                            .background(difficultyColor.copy(alpha = 0.2f))
                            .padding(horizontal = 10.sdp, vertical = 4.sdp)
                    ) {
                        Text(
                            text = challenge.question.difficulty.capitalize(),
                            color = difficultyColor,
                            style = TextStyleInter12Lh16Fw400(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.sdp))

            // Question Title
            Text(
                text = challenge.question.title,
                color = colorResource(R.color.white),
                style = TextStyleInter16Lh24Fw700(),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.sdp))

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

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.sdp)
                        .clip(CircleShape)
                        .background(statusColor)
                )

                Spacer(modifier = Modifier.width(6.sdp))

                Text(
                    text = statusText,
                    color = statusColor,
                    style = TextStyleInter12Lh16Fw400()
                )
            }

            Spacer(modifier = Modifier.height(20.sdp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.sdp)
            ) {
                // Close Button
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(44.sdp),
                    shape = RoundedCornerShape(12.sdp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = colorResource(R.color.bg_neutral).copy(alpha = 0.5f),
                        contentColor = colorResource(R.color.white)
                    ),
                    border = BorderStroke(
                        1.sdp,
                        colorResource(R.color.white).copy(alpha = 0.2f)
                    )
                ) {
                    Text(
                        text = "Close",
                        style = TextStyleInter14Lh20Fw400(),
                        fontWeight = FontWeight.Medium
                    )
                }

                // Open Question Button
                Button(
                    onClick = { onOpenQuestion(challenge.question.titleSlug) },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.sdp),
                    shape = RoundedCornerShape(12.sdp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.blue_normal_500),
                        contentColor = colorResource(R.color.white)
                    )
                ) {
                    Text(
                        text = "View Question",
                        style = TextStyleInter14Lh20Fw400(),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.sdp))
        }
    }
}
