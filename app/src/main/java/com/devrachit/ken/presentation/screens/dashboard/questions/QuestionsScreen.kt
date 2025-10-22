package com.devrachit.ken.presentation.screens.dashboard.questions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.calendar.CalendarComponent
import com.devrachit.ken.presentation.screens.dashboard.calendar.CalendarViewModel
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionsScreen(
    onQuestionClick: (String) -> Unit = {},
    calendarViewModel: CalendarViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.bg_neutral))
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.sdp)
    ) {
        // Header
        Text(
            text = "Questions will appear here",
            style = TextStyleInter16Lh24Fw700(),
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.white),
            modifier = Modifier.padding(bottom = 8.sdp)
        )

        
        // Additional info or features can be added here
    }
}
