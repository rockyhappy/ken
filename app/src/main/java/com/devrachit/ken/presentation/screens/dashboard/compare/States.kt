package com.devrachit.ken.presentation.screens.dashboard.compare

import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.presentation.screens.dashboard.home.QuestionProgressUiState

data class CompareUiStates(
    val friendsQuestionProgressInfo: Map<String, QuestionProgressUiState>? = emptyMap(),
    val friendsDetails: Map<String, LeetCodeUserInfo>?= emptyMap()
)