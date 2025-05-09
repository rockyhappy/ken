package com.devrachit.ken.presentation.screens.dashboard.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.models.toQuestionProgressUiState
import com.devrachit.ken.domain.usecases.getContestRankingHistogram.GetContestRankingHistogramUseCase
import com.devrachit.ken.domain.usecases.getCurrentTime.GetCurrentTime
import com.devrachit.ken.domain.usecases.getUserBadges.GetUserBadgesUseCase
import com.devrachit.ken.domain.usecases.getUserContestRanking.GetUserContestRankingUseCase
import com.devrachit.ken.domain.usecases.getUserProfileCalender.GetUserProfileCalenderUseCase
import com.devrachit.ken.domain.usecases.getUserQuestionStatus.GetUserQuestionStatusUseCase
import com.devrachit.ken.domain.usecases.getUserRecentSubmission.GetUserRecentSubmissionUseCase
import com.devrachit.ken.utility.NetworkUtility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val getUserQuestionStatusUseCase: GetUserQuestionStatusUseCase,
    private val getCurrentTime: GetCurrentTime,
    private val getUserProfileCalenderUseCase: GetUserProfileCalenderUseCase,
    private val getUserRecentSubmissionUseCase: GetUserRecentSubmissionUseCase,
    private val getUserBadgesUseCase: GetUserBadgesUseCase,
    private val getUserContestRankingUseCase: GetUserContestRankingUseCase,
    private val getContestRankingHistogramUseCase: GetContestRankingHistogramUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiStates())
    val uiState: StateFlow<HomeUiStates> = _uiState.asStateFlow()

    private val _loadingState = MutableStateFlow(LoadingStates())
    val loadingState: StateFlow<LoadingStates> = _loadingState.asStateFlow()

    private suspend fun updateLoadingState() {
        _uiState.value =
            _uiState.value.copy(
                isLoading = _loadingState.value.questionStatusLoading ||
                        _loadingState.value.currentTimeLoading ||
                        _loadingState.value.calendarLoading ||
                        _loadingState.value.submissionsLoading ||
                        _loadingState.value.badgesLoading ||
                        _loadingState.value.contestRankingLoading||
                        _loadingState.value.contestRankingHistogramLoading)

        Log.d("LoadingState", "isLoading: ${_uiState.value.isLoading}, " +
                "questionStatus: ${_loadingState.value.questionStatusLoading}, " +
                "currentTime: ${_loadingState.value.currentTimeLoading}, " +
                "calendar: ${_loadingState.value.calendarLoading}, " +
                "submissions: ${_loadingState.value.submissionsLoading}, " +
                "badges: ${_loadingState.value.badgesLoading}, " +
                "contestRanking: ${_loadingState.value.contestRankingLoading}, " +
                "contestHistogram: ${_loadingState.value.contestRankingHistogramLoading}")
    }

    fun loadUserDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            _loadingState.value.pullToRefreshLoading = true

            val username = withContext(Dispatchers.IO) {
                dataStoreRepository.readPrimaryUsername()
            }

            if (!username.isNullOrEmpty()) {
                coroutineScope {
                    launch(Dispatchers.IO) { fetchUserProfileCalender(username) }
                    launch(Dispatchers.IO) { fetchCurrentTime() }
                    launch(Dispatchers.IO) { fetchUserQuestionStatus(username) }
                    launch(Dispatchers.IO) { fetchUserRecentSubmission(username, 15) }
//                    launch(Dispatchers.IO) { fetchUserBadges(username) }
                    launch(Dispatchers.IO) { fetchUserContestRanking(username) }
                    launch(Dispatchers.IO) { fetchContestRankingHistogram() }
                }
            }
        }
    }

    private suspend fun fetchUserQuestionStatus(username: String) {
        _loadingState.value.questionStatusLoading = true
        updateLoadingState()

        getUserQuestionStatusUseCase(username, forceRefresh = true).collectLatest {
            when (it) {
                is Resource.Loading -> {
                    _loadingState.value.questionStatusLoading = true
                    updateLoadingState()
                }

                is Resource.Success -> {
                    val data = it.data
                    if (data != null) {
                        _uiState.value = _uiState.value.copy(
                            questionProgress = data.toQuestionProgressUiState()
                        )
                    }
                    _loadingState.value.questionStatusLoading = false
                    updateLoadingState()
                }

                is Resource.Error -> {
                    _loadingState.value.questionStatusLoading = false
                    updateLoadingState()
                    fetchUserQuestionStatus(username)
                }
            }
        }
    }

    private suspend fun fetchCurrentTime() {
        _loadingState.value.currentTimeLoading = true
        updateLoadingState()

        getCurrentTime().collectLatest {
            when (it) {
                is Resource.Loading -> {
                    _loadingState.value.currentTimeLoading = true
                    updateLoadingState()
                }

                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        currentTimestamp = it.data?.data?.currentTimestamp
                    )
                    _loadingState.value.currentTimeLoading = false
                    updateLoadingState()
                }

                is Resource.Error -> {
                    _loadingState.value.currentTimeLoading = false
                    updateLoadingState()
                    fetchCurrentTime()
                }
            }
        }
    }

    private suspend fun fetchUserProfileCalender(username: String) {
        _loadingState.value.calendarLoading = true
        updateLoadingState()

        getUserProfileCalenderUseCase(username, forceRefresh = true).collectLatest {
            when (it) {
                is Resource.Loading -> {
                    _loadingState.value.calendarLoading = true
                    updateLoadingState()
                }

                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        userProfileCalender = it.data
                    )
                    _loadingState.value.calendarLoading = false
                    updateLoadingState()
                }

                is Resource.Error -> {
                    _loadingState.value.calendarLoading = false
                    updateLoadingState()
                    fetchUserProfileCalender(username)
                }
            }
        }
    }

    private suspend fun fetchUserRecentSubmission(username: String, limit: Int? = 15) {
        _loadingState.value.submissionsLoading = true
        updateLoadingState()

        getUserRecentSubmissionUseCase(
            username = username,
            limit = limit,
            forceRefresh = true
        ).collectLatest {
            when (it) {
                is Resource.Loading -> {
                    _loadingState.value.submissionsLoading = true
                    updateLoadingState()
                }

                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        recentSubmissions = it.data
                    )
                    _loadingState.value.submissionsLoading = false
                    updateLoadingState()
                }

                is Resource.Error -> {
                    _loadingState.value.submissionsLoading = false
                    updateLoadingState()
                    fetchUserRecentSubmission(username, limit)
                }
            }
        }
    }

    private suspend fun fetchUserBadges(username: String) {
        _loadingState.value.badgesLoading = true
        updateLoadingState()

        getUserBadgesUseCase(username).collectLatest {
            when (it) {
                is Resource.Loading -> {
                    _loadingState.value.badgesLoading = true
                    updateLoadingState()
                }

                is Resource.Success -> {
                    _loadingState.value.badgesLoading = false
                    updateLoadingState()
                }

                is Resource.Error -> {
                    _loadingState.value.badgesLoading = false
                    updateLoadingState()
                    fetchUserBadges(username)
                }
            }
        }
    }

    private suspend fun fetchUserContestRanking(username: String) {
        _loadingState.value.contestRankingLoading = true
        updateLoadingState()

        getUserContestRankingUseCase(username = username, forceRefresh = true).collectLatest {
            when(it) {
                is Resource.Loading -> {
                    _loadingState.value.contestRankingLoading = true
                    updateLoadingState()
                }

                is Resource.Success -> {
                    _uiState.value=_uiState.value.copy(userContestRankingResponse = it.data)
                    _loadingState.value.contestRankingLoading = false
                    updateLoadingState()
                }

                is Resource.Error -> {
                    _loadingState.value.contestRankingLoading = false
                    updateLoadingState()
                    fetchUserContestRanking(username)
                }
            }
        }
    }

    private suspend fun fetchContestRankingHistogram() {
        _loadingState.value.contestRankingHistogramLoading = true
        updateLoadingState()

        getContestRankingHistogramUseCase().collectLatest {
            when (it) {
                is Resource.Loading -> {
                    _loadingState.value.contestRankingHistogramLoading = true
                    updateLoadingState()
                }

                is Resource.Success -> {
                    _uiState.value= _uiState.value.copy(contestRatingHistogramResponse = it.data)
                    _loadingState.value.contestRankingHistogramLoading = false
                    updateLoadingState()
                }

                is Resource.Error -> {
                    _loadingState.value.contestRankingHistogramLoading = false
                    updateLoadingState()
                    fetchContestRankingHistogram()
                }
            }
        }
    }
}