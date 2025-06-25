package com.devrachit.ken.presentation.screens.dashboard.questions.components

import Question
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devrachit.ken.R
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionItem(question: Question) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "${question.id}. ${question.title}",
            color = colorResource(R.color.white),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
        question.difficulty?.let {
            Text(
                modifier = Modifier
                    .padding(top = 8.sdp),
                text = it,
                color = colorResource(R.color.white).copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
    }
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 16.sdp),
    )
}