package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.QuestionDetails
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw500
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionTitleSection(questionDetails: QuestionDetails) {
    val borderColor = when (questionDetails.difficulty.lowercase()) {
        "easy" -> colorResource(R.color.easy_filled_blue)
        "medium" -> colorResource(R.color.medium_filled_yellow)
        "hard" -> colorResource(R.color.hard_filled_red)
        else -> colorResource(R.color.white).copy(alpha = 0.3f)
    }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.sdp, vertical = 10.sdp)
            .clip(RoundedCornerShape(16.sdp))
            .border(
                border = BorderStroke(
                    width = 1.sdp,
                    color = borderColor
                ),
                shape = RoundedCornerShape(16.sdp)
            )
            .padding(16.sdp)
    ) {
        // Title
        Text(
            text = questionDetails.title,
            color = colorResource(R.color.white),
            style = TextStyleInter16Lh24Fw500(),
            fontWeight = FontWeight.Bold
        )
        
        if (questionDetails.difficulty.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.sdp))
            
            // Difficulty Badge
            Row {
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
        }
    }
}
