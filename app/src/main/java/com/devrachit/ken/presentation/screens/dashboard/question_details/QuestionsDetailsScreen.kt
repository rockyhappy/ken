package com.devrachit.ken.presentation.screens.dashboard.question_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.question_details.components.QuestionDetailsContent
import com.devrachit.ken.presentation.screens.dashboard.settings.SettingsViewmodel
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionsDetailsScreen(
    questionSlug: String,
    onBackClick: () -> Unit = {}
) {
    val viewModel: QuestionDetailViewmodel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val settingsViewModel: SettingsViewmodel = hiltViewModel()
    val viewMode by settingsViewModel.questionDetailsViewMode.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bg_neutral))
            .statusBarsPadding()
    ) {
        when {
            uiState.isLoading -> {
                LoadingState()
            }
            uiState.error != null -> {
                ErrorState(errorMessage = uiState.error ?: "Unknown error")
            }
            uiState.questionDetails != null -> {
                QuestionDetailsContent(
                    uiState = uiState,
                    questionSlug = questionSlug,
                    onBackClick = onBackClick,
                    viewMode = viewMode
                )
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = colorResource(R.color.white)
        )
    }
}

@Composable
fun ErrorState(errorMessage: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.sdp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error",
            color = colorResource(R.color.white),
            style = TextStyleInter16Lh24Fw700(),
            modifier = Modifier.padding(bottom = 8.sdp)
        )
        Text(
            text = errorMessage,
            color = colorResource(R.color.white).copy(alpha = 0.7f),
            style = TextStyleInter14Lh20Fw400(),
            textAlign = TextAlign.Center
        )
    }
}