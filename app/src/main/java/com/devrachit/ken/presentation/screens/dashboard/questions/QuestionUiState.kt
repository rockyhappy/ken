package com.devrachit.ken.presentation.screens.dashboard.questions

import Question

data class QuestionUiState(
    val questionList: List<Question> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
