package com.devrachit.ken.presentation.screens.dashboard.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.Widgets.HeatmapCard
import com.devrachit.ken.presentation.screens.dashboard.Widgets.QuestionProgressCard
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun HomeScreen(
    uiState: HomeUiStates,
    onFirstLoad: () -> Unit = {},
) {
    LaunchedEffect(true) {
        onFirstLoad.invoke()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.bg_neutral)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Use the new QuestionProgressCard widget
        QuestionProgressCard(
            questionProgress = uiState.questionProgress,
            modifier = Modifier.padding(top = 20.sdp, start = 18.sdp, end = 18.sdp)
        )
        if(uiState.currentTimestamp!=null )
        HeatmapCard(
            modifier=Modifier.padding(top = 20.sdp, start = 18.sdp, end = 18.sdp),
            currentTimestamp = uiState.currentTimestamp,
            calenderDetails = uiState.userProfileCalender?.submissionCalendar?: ""
        )

    }
}