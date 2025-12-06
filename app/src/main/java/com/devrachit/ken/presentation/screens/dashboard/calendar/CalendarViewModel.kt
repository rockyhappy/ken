package com.devrachit.ken.presentation.screens.dashboard.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.domain.models.DailyChallenge
import com.devrachit.ken.domain.usecases.getCurrentTime.GetCurrentTime
import com.devrachit.ken.domain.usecases.getDailyCodingChallenge.GetDailyCodingChallengeUseCase
import com.devrachit.ken.utility.NetworkUtility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getCurrentTime: GetCurrentTime,
    private val getDailyCodingChallengeUseCase: GetDailyCodingChallengeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        fetchCurrentTime()
    }

    private fun fetchCurrentTime() {
        viewModelScope.launch {
            getCurrentTime().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        result.data?.data?.currentTimestamp?.let { timestamp ->
                            val currentDate = Instant.ofEpochSecond(timestamp.toLong())
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                            
                            val yearMonth = YearMonth.of(currentDate.year, currentDate.month)
                            
                            _uiState.value = _uiState.value.copy(
                                currentDate = currentDate,
                                displayedMonth = yearMonth,
                                isLoading = false,
                                error = null
                            )
                            
                            // Fetch challenges for current month
                            fetchDailyChallenges(yearMonth)
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.message ?: "Failed to fetch current time"
                        )
                    }
                }
            }
        }
    }

    private fun fetchDailyChallenges(yearMonth: YearMonth) {
        viewModelScope.launch {
            getDailyCodingChallengeUseCase(yearMonth.year, yearMonth.monthValue).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { response ->
                            val dateQuestions = mutableMapOf<LocalDate, DailyChallenge>()
                            response.data.dailyCodingChallengeV2.challenges.forEach { challenge ->
                                val date = LocalDate.parse(challenge.date)
                                dateQuestions[date] = challenge
                            }
                            // Count difficulties
                            val easyCount = dateQuestions.values.count { it.question.difficulty.equals("easy", ignoreCase = true) }
                            val mediumCount = dateQuestions.values.count { it.question.difficulty.equals("medium", ignoreCase = true) }
                            val hardCount = dateQuestions.values.count { it.question.difficulty.equals("hard", ignoreCase = true) }
                            // Completion stats
                            val completedCount = dateQuestions.values.count { it.userStatus.equals("Finish", ignoreCase = true) }
                            val totalCount = dateQuestions.size
                            _uiState.value = _uiState.value.copy(
                                dailyChallenges = dateQuestions,
                                easyCount = easyCount,
                                mediumCount = mediumCount,
                                hardCount = hardCount,
                                completedCount = completedCount,
                                totalCount = totalCount
                            )
                        }
                    }
                    is Resource.Error -> {
                        // Silently fail, calendar will just not show questions
                    }
                    is Resource.Loading -> {
                        // Already loaded, no need to show loading again
                    }
                }
            }
        }
    }

    fun navigateToMonth(yearMonth: YearMonth) {
        _uiState.value = _uiState.value.copy(displayedMonth = yearMonth)
        fetchDailyChallenges(yearMonth)
    }

    fun navigateToPreviousMonth() {
        val current = _uiState.value.displayedMonth
        val newMonth = current.minusMonths(1)
        _uiState.value = _uiState.value.copy(displayedMonth = newMonth)
        fetchDailyChallenges(newMonth)
    }

    fun navigateToNextMonth() {
        val current = _uiState.value.displayedMonth
        val newMonth = current.plusMonths(1)
        _uiState.value = _uiState.value.copy(displayedMonth = newMonth)
        fetchDailyChallenges(newMonth)
    }

    fun refresh() {
        fetchCurrentTime()
    }
}

data class CalendarUiState(
    val currentDate: LocalDate = LocalDate.now(),
    val displayedMonth: YearMonth = YearMonth.now(),
    val dailyChallenges: Map<LocalDate, DailyChallenge> = emptyMap(), // Map of date to daily challenge
    val isLoading: Boolean = false,
    val error: String? = null,
    val easyCount: Int = 0,
    val mediumCount: Int = 0,
    val hardCount: Int = 0,
    val completedCount: Int = 0,
    val totalCount: Int = 0
)
