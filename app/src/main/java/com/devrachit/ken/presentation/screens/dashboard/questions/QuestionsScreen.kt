package com.devrachit.ken.presentation.screens.dashboard.questions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devrachit.ken.R
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionsScreen(uiState: QuestionUiState, onQuestionClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
        //.background(color = colorResource(R.color.bg_neutral))
        //.padding(24.sdp)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            Text(text = "Loading...", color = colorResource(R.color.white))
        } else if (uiState.errorMessage != null) {

            Text(text = uiState.errorMessage, color = colorResource(R.color.white))
        } else
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.sdp, bottom = 100.sdp)) {
                items(uiState.questionList) { que ->
                    que.title?.let {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = it,
                                color = colorResource(R.color.white),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp
                            )
                            Text(
                                modifier = Modifier
                                    .padding(top = 8.sdp)
//                                    .align(Alignment.End)
                                ,
                                text = que.difficulty ?: "",
                                color = colorResource(R.color.white).copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.sdp),
                        )
                    }
                }
            }
    }
}
