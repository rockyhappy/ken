package com.devrachit.ken.presentation.screens.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginNavigationState {
    object Idle : LoginNavigationState()
    object NavigateToOnboarding : LoginNavigationState()
    object NavigateToMainActivity : LoginNavigationState()
    data class Error(val message: String) : LoginNavigationState()
}

@HiltViewModel
class LoginViewmodel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    private val _navigationState = MutableStateFlow<LoginNavigationState>(LoginNavigationState.Idle)
    val navigationState: StateFlow<LoginNavigationState> = _navigationState.asStateFlow()

    init {
//        checkUserCache()
    }

//    private fun checkUserCache() {
//        viewModelScope.launch {
//            try {
//                val userExists = getUserInfoUseCase.isUserCached()
//
//                if (userExists) {
//                    // User exists in cache, navigate to main activity regardless of cache expiration
//                    _navigationState.value = LoginNavigationState.NavigateToMainActivity
//                } else {
//                    // No user in cache, navigate to onboarding
//                    _navigationState.value = LoginNavigationState.NavigateToOnboarding
//                }
//            } catch (e: Exception) {
//                _navigationState.value = LoginNavigationState.Error("Failed to check user cache: ${e.message}")
//            }
//        }
//    }
    
    // Call this function to reset navigation state after navigation is handled
    fun resetNavigationState() {
        _navigationState.value = LoginNavigationState.Idle
    }
}