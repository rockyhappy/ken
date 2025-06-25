package com.devrachit.ken.presentation.screens.dashboard.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.questions.components.QuestionItem
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionsScreen(
    uiState: QuestionUiState,
    onQuestionClick: (String) -> Unit,
    onEvent: (QuestionEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
        //.background(color = colorResource(R.color.bg_neutral))
        //.padding(24.sdp)
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.errorMessage != null) {

            Text(text = uiState.errorMessage, color = colorResource(R.color.white))
        } else
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.sdp, bottom = 120.sdp)
            ) {
                itemsIndexed(uiState.questionList) { index, que ->
                    que.title?.let {
                        QuestionItem(que)
                    }

                    if (index > uiState.questionList.lastIndex - 2 && !uiState.isLoading){
                        onEvent(QuestionEvent.LoadQuestions)
                    }
                }
                item {
                    if (uiState.isLoading) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(text = "Loading...", color = colorResource(R.color.white))
                        }
                    }
                }
            }
    }
}

