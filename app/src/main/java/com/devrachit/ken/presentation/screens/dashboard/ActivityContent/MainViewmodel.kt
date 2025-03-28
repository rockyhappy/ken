package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoUseCase
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
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {


    private val _userValues = MutableStateFlow(States(leetCodeUserInfo = LeetCodeUserInfo()))
    val userValues = _userValues.asStateFlow()

    fun loadUserDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            val username = dataStoreRepository.readPrimaryUsername()
            if(!username.isNullOrEmpty()) {
                fetchUserInfo(username)
            }
        }
    }

    private suspend fun fetchUserInfo(username: String) {
        getUserInfoUseCase(username, forceRefresh = true) // Force refresh on initial verification
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
            Timber.d("User ${userData.username} exists")
            Log.d("MainViewModel", "Successfully loaded user data for ${_userValues.value.leetCodeUserInfo.toString()}")
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