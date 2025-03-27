package com.devrachit.ken.presentation.screens.auth.login

data class States(
    val username: String? = null,

)
sealed class LoginNavigationState {
    object Idle : LoginNavigationState()
    object NavigateToOnboarding : LoginNavigationState()
    object NavigateToMainActivity : LoginNavigationState()
    data class Error(val message: String) : LoginNavigationState()
}
