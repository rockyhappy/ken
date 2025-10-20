package com.devrachit.ken.presentation.screens.dashboard.question_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.devrachit.ken.R

@Composable
fun QuestionsDetailsScreen(
    questionSlug: String
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(colorResource(R.color.content_neutral_primary_white))
    ){
        Text(text = "Question Details Screen for: $questionSlug")
    }
}