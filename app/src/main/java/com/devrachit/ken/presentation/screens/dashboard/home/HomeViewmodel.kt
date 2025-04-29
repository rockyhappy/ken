package com.devrachit.ken.presentation.screens.dashboard.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.models.toQuestionProgressUiState
import com.devrachit.ken.domain.usecases.getCurrentTime.GetCurrentTime
import com.devrachit.ken.domain.usecases.getUserProfileCalender.GetUserProfileCalenderUseCase
import com.devrachit.ken.domain.usecases.getUserQuestionStatus.GetUserQuestionStatusUseCase
import com.devrachit.ken.utility.NetworkUtility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val getUserQuestionStatusUseCase: GetUserQuestionStatusUseCase,
    private val getCurrentTime: GetCurrentTime,
    private val getUserProfileCalenderUseCase: GetUserProfileCalenderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiStates())
    val uiState: StateFlow<HomeUiStates> = _uiState.asStateFlow()


    fun loadUserDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            val username = dataStoreRepository.readPrimaryUsername()
            if (!username.isNullOrEmpty()) {
                fetchUserProfileCalender(username)
                fetchCurrentTime()
                fetchUserQuestionStatus(username)
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
                    
                }
            }
        }
    }

}