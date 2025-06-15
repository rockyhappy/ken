package com.devrachit.ken.presentation.screens.auth.login

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoUseCase
import com.devrachit.ken.utility.NetworkUtility.Resource
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
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

    private val _uiState = MutableStateFlow<States>(States())
    val uiState: StateFlow<States> = _uiState.asStateFlow()

    private lateinit var remoteConfig: FirebaseRemoteConfig

    init {
        checkUserCache()
    }

    private fun checkUserCache() {
        viewModelScope.launch {
            try {
                val savedUsername = userPreferencesRepository.readPrimaryUsername()

                if (savedUsername != null) {
                    val userInfoResource = getUserInfoUseCase.getUserInfo(savedUsername)
                    Log.d("LoginScreenViewModel", "User info resource: $userInfoResource")

                    if (userInfoResource is Resource.Success) {
                        _navigationState.value =
                            LoginNavigationState.NavigateToMainActivity(savedUsername)
                    } else {
                        _navigationState.value =
                            LoginNavigationState.NavigateToMainActivity(savedUsername)
                    }
                } else {
                    _navigationState.value = LoginNavigationState.NavigateToOnboarding
                }
            } catch (e: Exception) {
                _navigationState.value = LoginNavigationState.NavigateToOnboarding
            }
        }
    }


    fun saveUsername(username: String) {
        viewModelScope.launch {
            userPreferencesRepository.savePrimaryUsername(username)
//            _navigationState.value = LoginNavigationState.NavigateToMainActivity(username)
        }
    }


    fun logout() {
        viewModelScope.launch {
            userPreferencesRepository.clearPrimaryUsername()
        }
    }
    fun setUpdateConfig(
        config: UpdateConfig,
        getPresentVersion: ()-> String = { "1.0.0" }
    ) {
        _uiState.value=_uiState.value.copy(updateConfig = config)
        if(config.minimumRequiredVersion==getPresentVersion()){
            _uiState.value=_uiState.value.copy(updateStatus = UpdateStatus.NoNeedToUpdate)
            navigateForward()
        }else if(config.forcePlaystoreUpdate){
            _uiState.value=_uiState.value.copy(updateStatus = UpdateStatus.ForceUpdate)
        }
        else{
            _uiState.value=_uiState.value.copy(updateStatus = UpdateStatus.NoForceUpdate)
        }
    }

    fun resetNavigationState() {
        _navigationState.value = LoginNavigationState.Idle
    }
    fun navigateForward(){
        _uiState.value=_uiState.value.copy(
            navigateToScreen = true
        )
    }

}