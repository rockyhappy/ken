package com.devrachit.ken.presentation.screens.dashboard.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.models.toQuestionProgressUiState
import com.devrachit.ken.domain.usecases.getCurrentTime.GetCurrentTime
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

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val getUserQuestionStatusUseCase: GetUserQuestionStatusUseCase,
    private val getCurrentTime: GetCurrentTime,
    private val getUserProfileCalenderUseCase: GetUserProfileCalenderUseCase,
    private val getUserRecentSubmissionUseCase: GetUserRecentSubmissionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiStates())
    val uiState: StateFlow<HomeUiStates> = _uiState.asStateFlow()



    fun loadUserDetails() {
        viewModelScope.launch {
            // Read username on IO thread
            val username = withContext(Dispatchers.IO) {
                dataStoreRepository.readPrimaryUsername()
            }

            if (!username.isNullOrEmpty()) {
                // Run all API calls concurrently on IO threads
                coroutineScope {
                    launch(Dispatchers.IO) { fetchUserProfileCalender(username) }
                    launch(Dispatchers.IO) { fetchCurrentTime() }
                    launch(Dispatchers.IO) { fetchUserQuestionStatus(username) }
                    launch(Dispatchers.IO) { fetchUserRecentSubmission(username, 15) }
                }
            }
        }
    }

    private suspend fun fetchUserQuestionStatus(username: String) {
        getUserQuestionStatusUseCase(username, forceRefresh = true).collectLatest {
            when (it) {
                is Resource.Loading -> {

                }

                is Resource.Success -> {
                    val data = it.data
                    if (data != null)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            questionProgress = data.toQuestionProgressUiState()
                        )
                }

                is Resource.Error -> {
                        fetchUserQuestionStatus(username)
                }
            }
        }
    }

    private suspend fun fetchCurrentTime() {
        getCurrentTime().collectLatest {
            when(it){
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        currentTimestamp = it.data?.data?.currentTimestamp)
                    Log.d("HomeViewModel", "Current Time: ${it.data?.data?.currentTimestamp}")
                }
                is Resource.Error -> {
                    fetchCurrentTime()
                }
            }
        }
    }

    private suspend fun fetchUserProfileCalender(username: String) {
        getUserProfileCalenderUseCase(username,forceRefresh = true).collectLatest{
            when(it){
                is Resource.Loading -> {
                    
                }
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        userProfileCalender = it.data)
                }
                is Resource.Error -> {
                    fetchUserProfileCalender(username)
                }
            }
        }
    }

    private suspend fun fetchUserRecentSubmission(username: String, limit : Int? =15) {
        getUserRecentSubmissionUseCase(username = username, limit = limit, forceRefresh = true).collectLatest {
            when(it){
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        recentSubmissions = it.data
                    )
                }
                is Resource.Error -> {
                    fetchUserRecentSubmission(username, limit)
                }
            }
        }
    }

}