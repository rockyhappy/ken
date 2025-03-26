package com.devrachit.ken.presentation.screens.auth.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoUseCase
import com.devrachit.ken.utility.NetworkUtility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewmodel @Inject constructor(
    @ApplicationContext
    private val context: Context,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val userPreferencesRepository: DataStoreRepository
) : ViewModel() {

    private val _navigationState = MutableStateFlow<LoginNavigationState>(LoginNavigationState.Idle)
    val navigationState: StateFlow<LoginNavigationState> = _navigationState.asStateFlow()

    init {
        checkUserCache()
    }

    private fun checkUserCache() {
        viewModelScope.launch {
            try {
                // First check if we have a primary username saved in DataStore
                val savedUsername = userPreferencesRepository.readPrimaryUsername()
                
                if (savedUsername != null) {
                    // If we have a username, try to get the user info from cache
                    val userInfoResource = getUserInfoUseCase.getUserInfo(savedUsername)
                    
                    if (userInfoResource is Resource.Success) {
                        // User exists in cache, navigate to main activity
                        _navigationState.value = LoginNavigationState.NavigateToMainActivity
                    } else {
                        // Username exists but not in cache, we'll try to fetch it from API later
                        _navigationState.value = LoginNavigationState.NavigateToMainActivity
                    }
                } else {
                    // No saved username, navigate to onboarding
                    _navigationState.value = LoginNavigationState.NavigateToOnboarding
                }
            } catch (e: Exception) {
                _navigationState.value = LoginNavigationState.NavigateToOnboarding
            }
        }
    }
    
    // Function to save username after successful login or registration
    fun saveUsername(username: String) {
        viewModelScope.launch {
            userPreferencesRepository.savePrimaryUsername(username)
//            _navigationState.value = LoginNavigationState.NavigateToMainActivity(username)
        }
    }
    
    // Function to clear username on logout
    fun logout() {
        viewModelScope.launch {
            userPreferencesRepository.clearPrimaryUsername()
        }
    }
    
    // Call this function to reset navigation state after navigation is handled
    fun resetNavigationState() {
        _navigationState.value = LoginNavigationState.Idle
    }
}