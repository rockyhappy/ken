package com.devrachit.ken.presentation.screens.dashboard.compare

import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserCalendar
import com.devrachit.ken.presentation.screens.dashboard.home.QuestionProgressUiState

data class CompareUiStates(
    val friendsQuestionProgressInfo: Map<String, QuestionProgressUiState>? = emptyMap(),
    val friendsDetails: Map<String, LeetCodeUserInfo>?= emptyMap(),
    val currentTimestamp: Double? = null,
    val userProfileCalender: Map<String, UserCalendar>? = emptyMap(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val searchResults: Map<String, LeetCodeUserInfo> = emptyMap(),
    val showSearchSuggestions: Boolean = false,
    val isSearching: Boolean = false
)

data class LoadingStates(
    var isLoadingUserList: Boolean = false,
    var isLoadingUserQuestionStatuses: Boolean = false,
    var isLoadingUserCalendars: Boolean = false,
    var currentTimeLoading: Boolean = false,
)
