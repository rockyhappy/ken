package com.devrachit.ken.presentation.screens.dashboard.questions

import Question

data class QuestionUiState(
    var questionList: List<Question> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val page: Int = 0,
    val limit: Int =  10,
){
    val skip = page * limit
}
