package com.devrachit.ken.presentation.screens.dashboard.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.domain.usecases.badgeDisplayMode.GetBadgeDisplayModeUseCase
import com.devrachit.ken.domain.usecases.badgeDisplayMode.SaveBadgeDisplayModeUseCase
import com.devrachit.ken.domain.usecases.displayType.GetDisplayTypeUseCase
import com.devrachit.ken.domain.usecases.displayType.SaveDisplayTypeUseCase
import com.devrachit.ken.domain.usecases.getCurrentTime.GetCurrentTime
import com.devrachit.ken.domain.usecases.questionDetailsViewMode.GetQuestionDetailsViewModeUseCase
import com.devrachit.ken.domain.usecases.questionDetailsViewMode.SaveQuestionDetailsViewModeUseCase
import com.devrachit.ken.domain.usecases.recentSubmissionLimit.GetRecentSubmissionLimitUseCase
import com.devrachit.ken.domain.usecases.recentSubmissionLimit.SaveRecentSubmissionLimitUseCase
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.data.remote.firebase.FirebaseRemoteConfigManager
import com.devrachit.ken.domain.models.NavigationItems
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewmodel@Inject constructor(
    private val getCurrentTime: GetCurrentTime,
    private val getDisplayTypeUseCase: GetDisplayTypeUseCase,
    private val saveDisplayTypeUseCase: SaveDisplayTypeUseCase,
    private val getBadgeDisplayModeUseCase: GetBadgeDisplayModeUseCase,
    private val saveBadgeDisplayModeUseCase: SaveBadgeDisplayModeUseCase,
    private val getRecentSubmissionLimitUseCase: GetRecentSubmissionLimitUseCase,
    private val saveRecentSubmissionLimitUseCase: SaveRecentSubmissionLimitUseCase,
    private val getQuestionDetailsViewModeUseCase: GetQuestionDetailsViewModeUseCase,
    private val saveQuestionDetailsViewModeUseCase: SaveQuestionDetailsViewModeUseCase,
    private val dataStoreRepository: DataStoreRepository,
    private val firebaseRemoteConfigManager: FirebaseRemoteConfigManager
): ViewModel() {
    
    // Display Type State
    private val _displayType = MutableStateFlow("LIST")
    val displayType: StateFlow<String> = _displayType.asStateFlow()

    // Badge Display Mode State
    private val _badgeDisplayMode = MutableStateFlow("DIALOG")
    val badgeDisplayMode: StateFlow<String> = _badgeDisplayMode.asStateFlow()

    // Recent Submission Limit State
    private val _recentSubmissionLimit = MutableStateFlow(15)
    val recentSubmissionLimit: StateFlow<Int> = _recentSubmissionLimit.asStateFlow()

    // Question Details View Mode State
    private val _questionDetailsViewMode = MutableStateFlow("SIMPLE")
    val questionDetailsViewMode: StateFlow<String> = _questionDetailsViewMode.asStateFlow()

    // Navigation Preferences State
    private val _bottomNavItems = MutableStateFlow(NavigationItems.DEFAULT_BOTTOM_NAV)
    val bottomNavItems: StateFlow<List<String>> = _bottomNavItems.asStateFlow()

    private val _sideNavItems = MutableStateFlow(NavigationItems.DEFAULT_SIDE_NAV)
    val sideNavItems: StateFlow<List<String>> = _sideNavItems.asStateFlow()

    // Developer Message Visibility State
    private val _showDeveloperMessage = MutableStateFlow(true)
    val showDeveloperMessage: StateFlow<Boolean> = _showDeveloperMessage.asStateFlow()

    private val _isUpdateAvailable = MutableStateFlow(false)
    val isUpdateAvailable: StateFlow<Boolean> = _isUpdateAvailable.asStateFlow()

    private val _updateUrl = MutableStateFlow("")
    val updateUrl: StateFlow<String> = _updateUrl.asStateFlow()

    init {
        // Continuously observe display type changes from DataStore
        viewModelScope.launch(Dispatchers.IO) {
            getDisplayTypeUseCase().collect { displayType ->
                displayType?.let {
                    _displayType.value = it
                }
            }
        }

        // Continuously observe badge display mode changes from DataStore
        viewModelScope.launch(Dispatchers.IO) {
            getBadgeDisplayModeUseCase().collect { badgeMode ->
                badgeMode?.let {
                    _badgeDisplayMode.value = it
                }
            }
        }

        // Continuously observe recent submission limit changes from DataStore
        viewModelScope.launch(Dispatchers.IO) {
            getRecentSubmissionLimitUseCase().collect { limit ->
                _recentSubmissionLimit.value = limit
            }
        }

        // Continuously observe question details view mode changes from DataStore
        viewModelScope.launch(Dispatchers.IO) {
            getQuestionDetailsViewModeUseCase().collect { viewMode ->
                viewMode?.let {
                    _questionDetailsViewMode.value = it
                }
            }
        }

        // Continuously observe bottom nav items changes from DataStore
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.bottomNavItems.collect { items ->
                _bottomNavItems.value = items
            }
        }

        // Continuously observe side nav items changes from DataStore
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.sideNavItems.collect { items ->
                _sideNavItems.value = items
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                firebaseRemoteConfigManager.fetchConfig()
                val shouldShowMessage = firebaseRemoteConfigManager.getBoolean(
                    FirebaseRemoteConfigManager.SHOW_DEVELOPER_MESSAGE,
                    true // Default to true
                )
                _showDeveloperMessage.value = shouldShowMessage

                val currentVersion = firebaseRemoteConfigManager.getCurrentAppVersion()
                val latestVersion = firebaseRemoteConfigManager.getLatestVersion()
                val updateUrl = firebaseRemoteConfigManager.getPlayStoreUrl()
                val isUpdateAvailable = firebaseRemoteConfigManager.compareVersions(currentVersion, latestVersion) < 0
                _isUpdateAvailable.value = isUpdateAvailable
                _updateUrl.value = updateUrl

            } catch (_: Exception) {
                _showDeveloperMessage.value = true
                _isUpdateAvailable.value = false
            }
        }
    }

    fun updateDisplayType(displayType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _displayType.value = displayType
            saveDisplayTypeUseCase(displayType)
        }
    }

    fun updateBadgeDisplayMode(displayMode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _badgeDisplayMode.value = displayMode
            saveBadgeDisplayModeUseCase(displayMode)
        }
    }

    fun updateRecentSubmissionLimit(limit: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _recentSubmissionLimit.value = limit
            saveRecentSubmissionLimitUseCase(limit)
        }
    }

    fun updateQuestionDetailsViewMode(viewMode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _questionDetailsViewMode.value = viewMode
            saveQuestionDetailsViewModeUseCase(viewMode)
        }
    }

    fun updateBottomNavItems(items: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _bottomNavItems.value = items
            dataStoreRepository.saveBottomNavItems(items)
        }
    }

    fun updateSideNavItems(items: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _sideNavItems.value = items
            dataStoreRepository.saveSideNavItems(items)
        }
    }

    fun getUpdateUrl(): String {
        return _updateUrl.value
    }
}