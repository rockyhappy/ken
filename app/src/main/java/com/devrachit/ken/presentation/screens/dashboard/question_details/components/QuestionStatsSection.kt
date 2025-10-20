package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.QuestionDetails
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionStatsSection(questionDetails: QuestionDetails, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(true) }
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "rotation"
    )
    
    Column(
        modifier = modifier
//            .fillMaxWidth()
//            .padding(horizontal = 18.sdp)
            .clip(RoundedCornerShape(16.sdp))
            .border(
                border = BorderStroke(
                    width = 1.sdp,
                    color = colorResource(R.color.white).copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(16.sdp)
            )
            .background(
                color = colorResource(R.color.card_elevated).copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.sdp)
            )
    ) {
        // Header - Always Visible
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(horizontal = 16.sdp, vertical = 12.sdp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.sdp)
            ) {
                Text(
                    text = "📊",
                    style = TextStyleInter14Lh20Fw400()
                )
                Text(
                    text = "Statistics",
                    color = colorResource(R.color.white),
                    style = TextStyleInter14Lh20Fw400(),
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Text(
                text = "▼",
                color = colorResource(R.color.white).copy(alpha = 0.6f),
                style = TextStyleInter12Lh16Fw400(),
                modifier = Modifier.rotate(rotationAngle)
            )
        }
        
        // Expandable Content
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.sdp, end = 16.sdp, bottom = 16.sdp)
            ) {
                // First Row: Acceptance Rate, Likes, Dislikes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.sdp)
                ) {
                    // Acceptance Rate
                    Text(
                        text = if (questionDetails.acceptanceRate.isNotEmpty())
                            "✓ ${questionDetails.acceptanceRate}"
                        else
                            "✓ N/A",
                        color = colorResource(R.color.green_normal_500).copy(alpha = 0.9f),
                        style = TextStyleInter12Lh16Fw400(),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.sdp))
                            .background(colorResource(R.color.green_normal_500).copy(alpha = 0.15f))
                            .border(
                                border = BorderStroke(
                                    width = 1.sdp,
                                    color = colorResource(R.color.green_normal_500).copy(alpha = 0.3f)
                                ),
                                shape = RoundedCornerShape(10.sdp)
                            )
                            .padding(horizontal = 12.sdp, vertical = 6.sdp)
                    )

                    // Likes
                    Text(
                        text = "👍 ${formatCountNew(questionDetails.likes)}",
                        color = colorResource(R.color.white).copy(alpha = 0.9f),
                        style = TextStyleInter12Lh16Fw400(),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.sdp))
                            .background(colorResource(R.color.white).copy(alpha = 0.08f))
                            .border(
                                border = BorderStroke(
                                    width = 1.sdp,
                                    color = colorResource(R.color.white).copy(alpha = 0.2f)
                                ),
                                shape = RoundedCornerShape(10.sdp)
                            )
                            .padding(horizontal = 12.sdp, vertical = 6.sdp)
                    )

                    // Dislikes
                    Text(
                        text = "👎 ${formatCountNew(questionDetails.dislikes)}",
                        color = colorResource(R.color.white).copy(alpha = 0.9f),
                        style = TextStyleInter12Lh16Fw400(),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.sdp))
                            .background(colorResource(R.color.white).copy(alpha = 0.08f))
                            .border(
                                border = BorderStroke(
                                    width = 1.sdp,
                                    color = colorResource(R.color.white).copy(alpha = 0.2f)
                                ),
                                shape = RoundedCornerShape(10.sdp)
                            )
                            .padding(horizontal = 12.sdp, vertical = 6.sdp)
                    )
                }
                
                // Second Row: Submissions Stats
                if (questionDetails.totalSubmissions.isNotEmpty() || questionDetails.totalAccepted.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.sdp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.sdp)
                    ) {
                        if (questionDetails.totalAccepted.isNotEmpty()) {
                            Text(
                                text = "✅ ${questionDetails.totalAccepted} Accepted",
                                color = colorResource(R.color.green_normal_500).copy(alpha = 0.95f),
                                style = TextStyleInter12Lh16Fw400(),
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.sdp))
                                    .background(colorResource(R.color.green_normal_500).copy(alpha = 0.12f))
                                    .border(
                                        border = BorderStroke(
                                            width = 1.sdp,
                                            color = colorResource(R.color.green_normal_500).copy(alpha = 0.3f)
                                        ),
                                        shape = RoundedCornerShape(10.sdp)
                                    )
                                    .padding(horizontal = 12.sdp, vertical = 6.sdp)
                            )
                        }
                        
                        if (questionDetails.totalSubmissions.isNotEmpty()) {
                            Text(
                                text = "📝 ${questionDetails.totalSubmissions}",
                                color = colorResource(R.color.blue_normal_500).copy(alpha = 0.95f),
                                style = TextStyleInter12Lh16Fw400(),
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.sdp))
                                    .background(colorResource(R.color.blue_normal_500).copy(alpha = 0.12f))
                                    .border(
                                        border = BorderStroke(
                                            width = 1.sdp,
                                            color = colorResource(R.color.blue_normal_500).copy(alpha = 0.3f)
                                        ),
                                        shape = RoundedCornerShape(10.sdp)
                                    )
                                    .padding(horizontal = 12.sdp, vertical = 6.sdp)
                            )
                        }
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
