package com.devrachit.ken.presentation.screens.dashboard.sheets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.data.local.entity.SheetEntity
import com.devrachit.ken.data.local.entity.SheetQuestionCrossRef
import com.devrachit.ken.data.local.entity.SheetWithQuestions
import com.devrachit.ken.domain.usecases.sheets.SheetUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SheetsViewModel @Inject constructor(
    private val sheetUseCases: SheetUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(SheetsUiState())
    val uiState: StateFlow<SheetsUiState> = _uiState.asStateFlow()

    init {
        ensureFavoritesSheetExists()
        loadSheets()
    }
    
    private fun ensureFavoritesSheetExists() {
        viewModelScope.launch {
            val existingFavorites = sheetUseCases.getAllSheetsWithQuestions.once()
                .find { it.sheet.name == FAVORITES_SHEET_NAME }
            
            if (existingFavorites == null) {
                sheetUseCases.createSheet(FAVORITES_SHEET_NAME)
            }
        }
    }

    private fun loadSheets() {
        viewModelScope.launch {
            sheetUseCases.getAllSheetsWithQuestions().collectLatest { sheets ->
                // Sort sheets: Favorites first, then by name alphabetically
                val sortedSheets = sheets.sortedWith(compareBy(
                    { it.sheet.name != FAVORITES_SHEET_NAME },
                    { it.sheet.name.lowercase() }
                ))
                
                _uiState.update { it.copy(
                    sheets = sortedSheets,
                    isLoading = false
                ) }
            }
        }
    }

    fun createSheet(name: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isCreating = true, error = null) }
            
            sheetUseCases.createSheet(name).fold(
                onSuccess = {
                    _uiState.update { it.copy(
                        isCreating = false,
                        newSheetName = ""
                    ) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(
                        isCreating = false,
                        error = error.message
                    ) }
                }
            )
        }
    }

    fun deleteSheet(sheetId: Long) {
        viewModelScope.launch {
            sheetUseCases.deleteSheet(sheetId).fold(
                onSuccess = {
                    // Sheet will be removed from list automatically via Flow
                },
                onFailure = { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            )
        }
    }

    fun updateNewSheetName(name: String) {
        _uiState.update { it.copy(newSheetName = name) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun selectSheet(sheetId: Long) {
        viewModelScope.launch {
            val sheetWithQuestions = sheetUseCases.getSheetWithQuestions(sheetId)
            _uiState.update { it.copy(
                selectedSheet = sheetWithQuestions,
                isSheetDetailVisible = true
            ) }
        }
    }

    fun closeSheetDetail() {
        _uiState.update { it.copy(
            selectedSheet = null,
            isSheetDetailVisible = false
        ) }
    }

    fun removeQuestionFromSheet(sheetId: Long, questionSlug: String) {
        viewModelScope.launch {
            sheetUseCases.removeQuestionFromSheet(sheetId, questionSlug).fold(
                onSuccess = {
                    // Refresh selected sheet
                    val updatedSheet = sheetUseCases.getSheetWithQuestions(sheetId)
                    _uiState.update { it.copy(selectedSheet = updatedSheet) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
            )
        }
    }
}

data class SheetsUiState(
    val sheets: List<SheetWithQuestions> = emptyList(),
    val selectedSheet: SheetWithQuestions? = null,
    val isSheetDetailVisible: Boolean = false,
    val isLoading: Boolean = true,
    val isCreating: Boolean = false,
    val newSheetName: String = "",
    val error: String? = null
)
