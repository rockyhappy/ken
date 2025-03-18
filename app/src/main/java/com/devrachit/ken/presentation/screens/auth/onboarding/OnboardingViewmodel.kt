package com.devrachit.ken.presentation.screens.auth.onboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


data class User(
    val userName: String?="",
    val isUserNameValid: Boolean?= true
)
@HiltViewModel
class OnboardingViewmodel @Inject constructor() : ViewModel() {
    private var _userValues = MutableStateFlow(User())
    val userValues: StateFlow<User> = _userValues.asStateFlow()

    fun updateUserName(userName: String) {
        _userValues.value = _userValues.value.copy(userName = userName)
    }
}