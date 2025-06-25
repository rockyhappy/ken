package com.devrachit.ken.presentation.screens.dashboard.questions

import Question

sealed class QuestionEvent {
    data object LoadQuestions : QuestionEvent()
}