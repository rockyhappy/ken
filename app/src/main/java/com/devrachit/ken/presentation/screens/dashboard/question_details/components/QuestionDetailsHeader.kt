package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionDetailsHeader(
    questionTitle: String,
    questionSlug: String,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.bg_neutral))
            .padding(horizontal = 12.sdp, vertical = 8.sdp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(40.sdp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = colorResource(R.color.white),
                    modifier = Modifier.size(24.sdp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.sdp))
            
            Text(
                text = "Question Details",
                color = colorResource(R.color.white),
                style = TextStyleInter16Lh24Fw700(),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        QuestionDetailsSettings(
            questionSlug = questionSlug,
            questionTitle = questionTitle
        )
    }
}
