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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.QuestionDetails
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.utility.composeUtility.sdp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuestionTagsSection(questionDetails: QuestionDetails) {
    if (questionDetails.topicTags.isNotEmpty()) {
        var isExpanded by remember { mutableStateOf(true) }
        val rotationAngle by animateFloatAsState(
            targetValue = if (isExpanded) 180f else 0f,
            label = "rotation"
        )
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.sdp)
                .clip(RoundedCornerShape(16.sdp))
                .border(
                    border = BorderStroke(
                        width = 1.sdp,
                        color = colorResource(R.color.blue_normal_500).copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(16.sdp)
                )
                .background(
                    color = colorResource(R.color.blue_normal_500).copy(alpha = 0.08f),
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
                        text = "🏷️",
                        style = TextStyleInter14Lh20Fw400()
                    )
                    Text(
                        text = "Topics (${questionDetails.topicTags.size})",
                        color = colorResource(R.color.blue_normal_500),
                        style = TextStyleInter14Lh20Fw400(),
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                Text(
                    text = "▼",
                    color = colorResource(R.color.blue_normal_500).copy(alpha = 0.7f),
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
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.sdp, end = 16.sdp, bottom = 16.sdp),
                    horizontalArrangement = Arrangement.spacedBy(8.sdp),
                    verticalArrangement = Arrangement.spacedBy(8.sdp)
                ) {
                    questionDetails.topicTags.forEach { tag ->
                        Text(
                            text = tag,
                            color = colorResource(R.color.blue_normal_500).copy(alpha = 0.95f),
                            style = TextStyleInter12Lh16Fw400(),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.sdp))
                                .background(colorResource(R.color.blue_normal_500).copy(alpha = 0.15f))
                                .border(
                                    border = BorderStroke(
                                        width = 1.sdp,
                                        color = colorResource(R.color.blue_normal_500).copy(alpha = 0.35f)
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
