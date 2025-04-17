package com.devrachit.ken.presentation.screens.dashboard.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.models.toQuestionProgressUiState
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
): ViewModel(){

    private val _uiState = MutableStateFlow(HomeUiStates())
    val uiState  : StateFlow<HomeUiStates> = _uiState.asStateFlow()


    fun loadUserDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            val username = dataStoreRepository.readPrimaryUsername()
            if (!username.isNullOrEmpty()) {
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
                    if(data != null)
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



}