package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.question_details.QuestionDetailsUiState
import com.devrachit.ken.presentation.screens.dashboard.settings.SettingsViewmodel
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionDetailsContent(
    uiState: QuestionDetailsUiState,
    questionSlug: String,
    onBackClick: () -> Unit = {},
    viewMode : String = "PAGER"
) {

    
    uiState.questionDetails?.let { question ->
        when (viewMode) {
            "PAGER" -> {
                // Original Pager View
                QuestionDetailsPagerView(
                    questionDetails = question,
                    parsedSections = uiState.parsedSections,
                    questionSlug = questionSlug,
                    onBackClick = onBackClick
                )
            }
            "SIMPLE" -> {
                // Simple Scrollable View
                QuestionDetailsSimpleView(
                    questionDetails = question,
                    parsedSections = uiState.parsedSections,
                    questionSlug = questionSlug,
                    onBackClick = onBackClick
                )
            }
            "WEBVIEW" -> {
                // WebView
                QuestionDetailsWebView(
                    questionSlug = questionSlug,
                    questionTitle = question.title,
                    onBackClick = onBackClick
                )
            }
            else -> {
                // Default to Pager View
                QuestionDetailsPagerView(
                    questionDetails = question,
                    parsedSections = uiState.parsedSections,
                    questionSlug = questionSlug,
                    onBackClick = onBackClick
                )
            }
        }
    }
}

@Composable
private fun QuestionDetailsPagerView(
    questionDetails: com.devrachit.ken.domain.models.QuestionDetails,
    parsedSections: List<QuestionSection>,
    questionSlug: String,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bg_neutral))
    ) {
        // Header with back button and settings
        QuestionDetailsHeader(
            questionTitle = questionDetails.title,
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
            QuestionTitleSection(questionDetails = questionDetails)
            
            Spacer(modifier = Modifier.height(12.sdp))
            
            // Stats Section (acceptance rate, likes, dislikes, submissions)
            QuestionStatsSection(questionDetails = questionDetails, modifier = Modifier.fillMaxWidth().padding(horizontal = 18.sdp))
            
            Spacer(modifier = Modifier.height(12.sdp))
            
            // Tags Section
            QuestionTagsSection(questionDetails = questionDetails, modifier = Modifier.fillMaxWidth().padding(horizontal = 18.sdp))
            
            Spacer(modifier = Modifier.height(12.sdp))
            
            // Pager with sections
            QuestionPagerContent(
                questionDetails = questionDetails,
                parsedSections = parsedSections
            )
            
            Spacer(modifier = Modifier.height(100.sdp))
        }
    }
}
