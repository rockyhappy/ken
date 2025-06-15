package com.devrachit.ken.presentation.screens.auth.login

data class States(
    val username: String? = null,
    val updateConfig: UpdateConfig? = null,
    val updateStatus : UpdateStatus ? = null,
    val navigateToScreen: Boolean=false

)
enum class UpdateStatus
{
    NoNeedToUpdate,
    NoForceUpdate,
    ForceUpdate,
    Error
}
sealed class LoginNavigationState {
    object Idle : LoginNavigationState()
    object NavigateToOnboarding : LoginNavigationState()
    data class NavigateToMainActivity(val username : String) : LoginNavigationState()
    data class Error(val message: String) : LoginNavigationState()
}
data class UpdateConfig(
    val forcePlaystoreUpdate: Boolean,
    val minimumRequiredVersion: String,
    val playstoreUpdateMessage: String,
    val playstoreUpdateUrl: String
)