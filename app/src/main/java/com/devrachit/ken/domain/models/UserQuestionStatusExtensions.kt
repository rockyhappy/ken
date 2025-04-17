package com.devrachit.ken.domain.models

import com.devrachit.ken.presentation.screens.dashboard.home.QuestionProgressUiState
import com.devrachit.ken.utility.NetworkUtility.Resource

/**
 * Extension function to convert UserQuestionStatusData to QuestionProgressUiState
 */
fun UserQuestionStatusData.toQuestionProgressUiState(): QuestionProgressUiState {
    // Default values in case data is missing
    var total = 3521
    var easyTotal = 873
    var mediumTotal = 1826
    var hardTotal = 822
    
    // Extract total counts from allQuestionsCount
    this.allQuestionsCount?.forEach { questionCount ->
        when (questionCount.difficulty) {
            "All" -> total = questionCount.count
            "Easy" -> easyTotal = questionCount.count
            "Medium" -> mediumTotal = questionCount.count
            "Hard" -> hardTotal = questionCount.count
        }
    }
    
    // Get solved counts from acSubmissionNum
    var easySolved = 0
    var mediumSolved = 0
    var hardSolved = 0
    var totalSolved = 0
    
    this.matchedUser?.submitStats?.acSubmissionNum?.forEach { stat ->
        when (stat.difficulty) {
            "All" -> totalSolved = stat.count
            "Easy" -> easySolved = stat.count
            "Medium" -> mediumSolved = stat.count
            "Hard" -> hardSolved = stat.count
        }
    }
    
    // Calculate "attempting" as difference between totalSubmission and acSubmission
    val totalAttempting = calculateAttempting(
        this.matchedUser?.submitStats?.totalSubmissionNum?.find { it.difficulty == "All" }?.count ?: 0,
        totalSolved
    )
    
    return QuestionProgressUiState(
        solved = totalSolved,
        attempting = totalAttempting,
        total = total,
        easyTotalCount = easyTotal,
        easySolvedCount = easySolved,
        mediumTotalCount = mediumTotal,
        mediumSolvedCount = mediumSolved,
        hardTotalCount = hardTotal,
        hardSolvedCount = hardSolved
    )
}

/**
 * Helper function to calculate attempting questions
 * Attempting = Total submissions - Solved submissions
 */
private fun calculateAttempting(totalSubmissions: Int, solvedSubmissions: Int): Int {
    return (totalSubmissions - solvedSubmissions).coerceAtLeast(0)
}

/**
 * Extension function to handle Resource<UserQuestionStatusData> and convert to QuestionProgressUiState
 */
fun Resource<UserQuestionStatusData>.toQuestionProgressUiState(): QuestionProgressUiState {
    return when (this) {
        is Resource.Success -> {
            this.data?.toQuestionProgressUiState() ?: QuestionProgressUiState()
        }
        is Resource.Loading, is Resource.Error -> {
            QuestionProgressUiState()
        }
    }
}