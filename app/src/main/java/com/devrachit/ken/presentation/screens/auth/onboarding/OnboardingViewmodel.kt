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


data class User(
    val userName: String? = "rockyhappy",
    val isUserNameValid: Boolean? = true,
    val isChecking: Boolean = false,
    val errorMessage: String? = null
)


@HiltViewModel
class OnboardingViewmodel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {
    private var _userValues = MutableStateFlow(User())
    val userValues: StateFlow<User> = _userValues.asStateFlow()

    private val client = OkHttpClient.Builder()
        .addInterceptor(ChuckerInterceptor.Builder(context).build())
        .build()
    private val json = Json { ignoreUnknownKeys = true }

    fun updateUserName(userName: String) {
        _userValues.value = _userValues.value.copy(userName = userName)
    }

    // Check user exists function
    fun checkUserExists() {
        val username = _userValues.value.userName ?: return
        _userValues.value = _userValues.value.copy(isChecking = true, errorMessage = null)
        
        viewModelScope.launch {
            getUserInfoUseCase(username)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            // Keep showing the loading state
                            _userValues.value = _userValues.value.copy(isChecking = true)
                        }
                        
                        is Resource.Success -> {
                            // User found, update the state
                            _userValues.value = _userValues.value.copy(
                                isChecking = false,
                                isUserNameValid = true,
                                errorMessage = null
                            )
                            Log.d("OnboardingViewModel", "User $username exists: ${result.data}")
                        }
                        
                        is Resource.Error -> {
                            // Handle error case
                            val errorMessage = if (result.message?.contains("not found", ignoreCase = true) == true) {
                                "User not found on LeetCode"
                            } else {
                                "Error checking username: ${result.message}"
                            }
                            
                            _userValues.value = _userValues.value.copy(
                                isChecking = false,
                                isUserNameValid = result.message?.contains("not found", ignoreCase = true) != true,
                                errorMessage = errorMessage
                            )
                            Log.e("OnboardingViewModel", "Error checking username: ${result.message}")
                        }

                    }
                }
        }
    }



}