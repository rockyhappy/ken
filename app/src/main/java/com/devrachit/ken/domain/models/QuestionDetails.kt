package com.devrachit.ken.domain.models

data class QuestionDetails(
    val questionId: String = "",
    val title: String = "",
    val description: String = "",
    val difficulty: String = "",
    val acceptanceRate: String = "",
    val likes: Int = 0,
    val dislikes: Int = 0,
    val totalSubmissions: String = "",
    val totalAccepted: String = "",
    val topicTags: List<String> = emptyList(),
    val hints: List<String> = emptyList(),
    val htmlContent: String = ""
)
