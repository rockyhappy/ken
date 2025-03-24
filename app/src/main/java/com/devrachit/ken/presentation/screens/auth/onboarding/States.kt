package com.devrachit.ken.presentation.screens.auth.onboarding

data class User(
    val userName: String? = null,
    val isUserNameValid: Boolean= true,
    val userNameErrorMessage : String? = null,
    val errorMessage: String? = null,
    val isLoadingUsername: Boolean = false,
    val isLoadingGuestUser: Boolean = false,
    val isUserNameVerified: Boolean = false
)