package com.devrachit.ken.presentation.screens.auth.onboarding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import android.util.Log
import org.json.JSONObject
import com.devrachit.ken.domain.models.UserInfoResponse
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoUseCase
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber


@HiltViewModel
class OnboardingViewmodel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {
    private var _userValues = MutableStateFlow(User())
    val userValues: StateFlow<User> = _userValues.asStateFlow()


    fun updateUserName(userName: String) {
        _userValues.value = _userValues.value.copy(userName = userName)
    }

    // Check user exists function
//    fun checkUserExists() {
//        if (_userValues.value.userName != null && _userValues.value.userName!!.isNotEmpty()) {
//            val username = _userValues.value.userName ?: return
//            viewModelScope.launch(Dispatchers.IO) {
//                getUserInfoUseCase(username)
//                    .collectLatest { result ->
//                        when (result) {
//                            is Resource.Loading -> {
//                                // Keep showing the loading state
//                                _userValues.value = _userValues.value.copy(isLoadingUsername = true)
//                            }
//
//                            is Resource.Success -> {
//                                println("logcat")
//                                val data = result.data as LeetCodeUserInfo
//                                if(data.username != null)
//                                _userValues.value = _userValues.value.copy(
//                                    isLoadingUsername = false,
//                                    isUserNameValid = true,
//                                    errorMessage = null,
//                                    isUserNameVerified = true
//                                )
//                                else
//                                    _userValues.value = _userValues.value.copy(
//                                        isLoadingUsername = false,
//                                        isUserNameValid = false,
//                                        errorMessage = "No user data found")
//                                Log.d("OnboardingViewModel", "User $username exists: ${result.data}")
//                            }
//
//                            is Resource.Error -> {
//                                // Handle error case
//                                val errorMessage = if (result.message?.contains(
//                                        "not found",
//                                        ignoreCase = true
//                                    ) == true
//                                ) {
//                                    "User not found on Leetcode"
//                                } else {
//                                    "Error checking username: ${result.message}"
//                                }
//
//                                _userValues.value = _userValues.value.copy(
//                                    isLoadingUsername = false,
//                                    isUserNameValid = result.message?.contains(
//                                        "not found",
//                                        ignoreCase = true
//                                    ) != true,
//                                    errorMessage = errorMessage
//                                )
//                                Timber.tag("OnboardingViewModel")
//                                    .e("Error checking username: ${result.message}")
//                            }
//
//                        }
//                    }
//            }
//        }
//    }
    fun checkUserExists() {
        val username = _userValues.value.userName

        // Early return for empty username
        if (username.isNullOrEmpty()) {
            _userValues.value = _userValues.value.copy(
                isUserNameValid = false,
                errorMessage = "Username cannot be empty"
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            fetchUserInfo(username)
        }
    }

    private suspend fun fetchUserInfo(username: String) {
        getUserInfoUseCase(username)
            .collectLatest { result ->
                when (result) {
                    is Resource.Loading -> handleLoadingState()
                    is Resource.Success -> handleSuccessState(result.data as LeetCodeUserInfo)
                    is Resource.Error -> handleErrorState(result.message)
                }
            }
    }

    private fun handleLoadingState() {
        _userValues.value = _userValues.value.copy(isLoadingUsername = true)
    }

    private fun handleSuccessState(userData: LeetCodeUserInfo) {
        if (userData.username != null) {
            _userValues.value = _userValues.value.copy(
                isLoadingUsername = false,
                isUserNameValid = true,
                errorMessage = null,
                isUserNameVerified = true
            )
            Timber.d("User ${userData.username} exists")
        } else {
            _userValues.value = _userValues.value.copy(
                isLoadingUsername = false,
                isUserNameValid = false,
                errorMessage = "No user data found"
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
            isLoadingUsername = false,
            isUserNameValid = !isUserNotFound,
            errorMessage = displayMessage
        )
        Timber.e("Error checking username: $errorMessage")
    }
}