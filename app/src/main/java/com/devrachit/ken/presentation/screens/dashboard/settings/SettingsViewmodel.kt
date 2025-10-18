package com.devrachit.ken.presentation.screens.dashboard.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.domain.usecases.displayType.GetDisplayTypeUseCase
import com.devrachit.ken.domain.usecases.displayType.SaveDisplayTypeUseCase
import com.devrachit.ken.domain.usecases.getCurrentTime.GetCurrentTime
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
    private val saveDisplayTypeUseCase: SaveDisplayTypeUseCase
): ViewModel() {
    
    // Display Type State
    private val _displayType = MutableStateFlow("LIST")
    val displayType: StateFlow<String> = _displayType.asStateFlow()

    init {
        // Continuously observe display type changes from DataStore
        viewModelScope.launch(Dispatchers.IO) {
            getDisplayTypeUseCase().collect { displayType ->
                displayType?.let {
                    _displayType.value = it
                }
            }
        }
    }

    fun updateDisplayType(displayType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _displayType.value = displayType
            saveDisplayTypeUseCase(displayType)
        }
    }
}