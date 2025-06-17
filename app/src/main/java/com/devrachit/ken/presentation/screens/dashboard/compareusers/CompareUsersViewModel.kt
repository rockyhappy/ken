package com.devrachit.ken.presentation.screens.dashboard.compareusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserCalendar
import com.devrachit.ken.domain.models.toQuestionProgressUiState
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetAllUsersUsecase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoUseCase
import com.devrachit.ken.domain.usecases.getUserQuestionStatus.GetUserQuestionStatusUseCase
import com.devrachit.ken.domain.usecases.getUserProfileCalender.GetUserProfileCalenderUseCase
import com.devrachit.ken.presentation.screens.dashboard.home.QuestionProgressUiState
import com.devrachit.ken.utility.NetworkUtility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

data class CompareUsersUiState(
    val isLoading: Boolean = false,
    val username1: String? = null,
    val username2: String? = null,
    val availableUsers: List<String> = emptyList(),
    val user1Data: UserComparisonData? = null,
    val user2Data: UserComparisonData? = null,
    val error: String? = null
)

data class UserComparisonData(
    val userInfo: LeetCodeUserInfo? = null,
    val questionProgress: QuestionProgressUiState = QuestionProgressUiState(),
    val calendarData: UserCalendar? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class CompareUsersViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getUserQuestionStatusUseCase: GetUserQuestionStatusUseCase,
    private val getUserProfileCalenderUseCase: GetUserProfileCalenderUseCase,
    private val getAllUsersUsecase: GetAllUsersUsecase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompareUsersUiState())
    val uiState: StateFlow<CompareUsersUiState> = _uiState.asStateFlow()

    fun initializeComparison(username1: String?, username2: String?, availableUsers: List<String>) {
        _uiState.value = _uiState.value.copy(
            username1 = username1,
            username2 = username2,
            availableUsers = availableUsers
        )
        
        // Load available users if list is empty
        if (availableUsers.isEmpty()) {
            loadAvailableUsers()
        }
        
        username1?.let { loadUserData(it, isUser1 = true) }
        username2?.let { loadUserData(it, isUser1 = false) }
    }

    fun selectUser1(username: String) {
        _uiState.value = _uiState.value.copy(username1 = username)
        loadUserData(username, isUser1 = true)
    }

    fun selectUser2(username: String) {
        _uiState.value = _uiState.value.copy(username2 = username)
        loadUserData(username, isUser1 = false)
    }

    fun loadAvailableUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            getAllUsersUsecase().collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        val usernames = result.data?.mapNotNull { it.username } ?: emptyList()
                        _uiState.value = _uiState.value.copy(availableUsers = usernames)
                    }
                    is Resource.Error -> {
                        // Handle error silently or show error message
                    }
                    is Resource.Loading -> {
                        // Loading state handled elsewhere
                    }
                }
            }
        }
    }

    private fun loadUserData(username: String, isUser1: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            // Set loading state
            if (isUser1) {
                _uiState.value = _uiState.value.copy(
                    user1Data = _uiState.value.user1Data?.copy(isLoading = true) 
                        ?: UserComparisonData(isLoading = true)
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    user2Data = _uiState.value.user2Data?.copy(isLoading = true) 
                        ?: UserComparisonData(isLoading = true)
                )
            }

            // Load user info
            getUserInfoUseCase(username, forceRefresh = false).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        val userInfo = result.data as? LeetCodeUserInfo
                        updateUserData(username, isUser1, userInfo = userInfo)
                        
                        // Load question status and calendar data
                        loadQuestionStatus(username, isUser1)
                        loadCalendarData(username, isUser1)
                    }
                    is Resource.Error -> {
                        updateUserData(username, isUser1, error = result.message)
                    }
                    is Resource.Loading -> {
                        // Already handled above
                    }
                }
            }
        }
    }

    private fun loadQuestionStatus(username: String, isUser1: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserQuestionStatusUseCase(username).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        val questionProgress = result.data?.toQuestionProgressUiState() ?: QuestionProgressUiState()
                        updateUserData(username, isUser1, questionProgress = questionProgress)
                    }
                    is Resource.Error -> {
                        updateUserData(username, isUser1, error = result.message)
                    }
                    is Resource.Loading -> {
                        // Loading state already set
                    }
                }
            }
        }
    }

    private fun loadCalendarData(username: String, isUser1: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserProfileCalenderUseCase(username).collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        val calendarData = result.data
                        updateUserData(username, isUser1, calendarData = calendarData)
                    }
                    is Resource.Error -> {
                        updateUserData(username, isUser1, error = result.message)
                    }
                    is Resource.Loading -> {
                        // Loading state already set
                    }
                }
            }
        }
    }

    private fun updateUserData(
        username: String,
        isUser1: Boolean,
        userInfo: LeetCodeUserInfo? = null,
        questionProgress: QuestionProgressUiState? = null,
        calendarData: UserCalendar? = null,
        error: String? = null
    ) {
        val currentData = if (isUser1) _uiState.value.user1Data else _uiState.value.user2Data
        val updatedData = (currentData ?: UserComparisonData()).copy(
            userInfo = userInfo ?: currentData?.userInfo,
            questionProgress = questionProgress ?: currentData?.questionProgress ?: QuestionProgressUiState(),
            calendarData = calendarData ?: currentData?.calendarData,
            isLoading = false
        )

        _uiState.value = if (isUser1) {
            _uiState.value.copy(user1Data = updatedData, error = error)
        } else {
            _uiState.value.copy(user2Data = updatedData, error = error)
        }
    }
}
