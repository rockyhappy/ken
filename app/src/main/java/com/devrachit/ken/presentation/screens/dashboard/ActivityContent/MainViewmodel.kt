package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import android.content.Context
import android.util.Log
import androidx.compose.material.Snackbar
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoUseCase
import com.devrachit.ken.domain.usecases.getUserQuestionStatus.GetUserQuestionStatusUseCase
import com.devrachit.ken.utility.NetworkUtility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


data class States(
    val userName : String?= null,
    val isLoadingUserInfo: Boolean = false,
    val leetCodeUserInfo: LeetCodeUserInfo,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val dataStoreRepository: DataStoreRepository,
    private val getUserQuestionStatusUseCase: GetUserQuestionStatusUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _userValues = MutableStateFlow(States(leetCodeUserInfo = LeetCodeUserInfo()))
    val userValues: StateFlow<States> = _userValues.asStateFlow()

    fun loadUserDetails() {


        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val username = dataStoreRepository.readPrimaryUsername()
            if (!username.isNullOrEmpty()) {
                fetchUserInfo(username)
                fetchUserQuestionStatus(username)
            }
            _isLoading.value = false
        }
    }

    fun reloadUserDetails() {
        if (_isLoading.value) {
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val username = dataStoreRepository.readPrimaryUsername()
            if (!username.isNullOrEmpty()) {
                fetchUserInfoIfNeeded(username)
                fetchUserQuestionStatus(username)
            }
            _isLoading.value = false
        }
    }

    private suspend fun fetchUserQuestionStatus(username: String) {
        getUserQuestionStatusUseCase(username).collectLatest {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {

                }
                is Resource.Error -> {

                }
            }
        }
    }

    private suspend fun fetchUserInfoIfNeeded(username: String) {
        getUserInfoUseCase(username, forceRefresh = false)
            .collectLatest { result ->
                when (result) {
                    is Resource.Loading -> handleLoadingState()
                    is Resource.Success -> handleSuccessState(result.data as LeetCodeUserInfo)
                    is Resource.Error -> handleErrorState(result.message)
                }
            }
    }

    private suspend fun fetchUserInfo(username: String) {
        getUserInfoUseCase(username, forceRefresh = true)
            .collectLatest { result ->
                when (result) {
                    is Resource.Loading -> handleLoadingState()
                    is Resource.Success -> handleSuccessState(result.data as LeetCodeUserInfo)
                    is Resource.Error -> handleErrorState(result.message)
                }
            }
    }
    private fun handleLoadingState() {
        _userValues.value = _userValues.value.copy(isLoadingUserInfo = true)
    }

    private fun handleSuccessState(userData: LeetCodeUserInfo) {
        if (userData.username != null) {
            _userValues.value = _userValues.value.copy(
                isLoadingUserInfo = false,
                leetCodeUserInfo = userData
            )
            viewModelScope.launch(Dispatchers.IO) {
                dataStoreRepository.savePrimaryUsername(userData.username)
            }
        } else {
            _userValues.value = _userValues.value.copy(
                isLoadingUserInfo = false,
                error = "No user data found"
            )
        }
    }

    private fun handleErrorState(errorMessage: String?) {
        val isUserNotFound = errorMessage?.contains("not found", ignoreCase = true) == true
        val displayMessage = if (isUserNotFound) {
            "User not found on Leetcode"
        } else {
            "Error checking username: $errorMessage"
        }

        _userValues.value = _userValues.value.copy(
            isLoadingUserInfo = false,
            error = displayMessage
        )
        Timber.e("Error checking username: $errorMessage")
    }
}