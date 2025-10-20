package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.question_details.QuestionDetailsUiState
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionDetailsContent(
    uiState: QuestionDetailsUiState,
    questionSlug: String,
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bg_neutral))
    ) {
        uiState.questionDetails?.let { question ->
            // Header with back button and settings
            QuestionDetailsHeader(
                questionTitle = question.title,
                questionSlug = questionSlug,
                onBackClick = onBackClick
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(top = 12.sdp)
            ) {
                // Title Section with difficulty badge and colored border
                QuestionTitleSection(questionDetails = question)
                
                Spacer(modifier = Modifier.height(12.sdp))
                
                // Stats Section (acceptance rate, likes, dislikes, submissions)
                QuestionStatsSection(questionDetails = question)
                
                Spacer(modifier = Modifier.height(12.sdp))
                
                // Tags Section
                QuestionTagsSection(questionDetails = question)
                
                Spacer(modifier = Modifier.height(12.sdp))
                
                // Pager with sections
                QuestionPagerContent(questionDetails = question)
                
                Spacer(modifier = Modifier.height(100.sdp))
            }
        }
    }
}
