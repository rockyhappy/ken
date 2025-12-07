package com.devrachit.ken.presentation.screens.dashboard.question_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.data.local.entity.SheetEntity
import com.devrachit.ken.data.local.entity.SheetWithQuestions
import com.devrachit.ken.domain.models.QuestionDetails
import com.devrachit.ken.domain.usecases.getQuestionDetails.GetQuestionDetailsUseCase
import com.devrachit.ken.domain.usecases.sheets.SheetUseCases
import com.devrachit.ken.presentation.screens.dashboard.question_details.components.QuestionSection
import com.devrachit.ken.presentation.screens.dashboard.question_details.components.parseSections
import com.devrachit.ken.presentation.screens.dashboard.sheets.FAVORITES_SHEET_NAME
import com.devrachit.ken.utility.NetworkUtility.Resource
import com.devrachit.ken.utility.QuestionHtmlParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuestionDetailViewmodel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getQuestionDetailsUseCase: GetQuestionDetailsUseCase,
    private val sheetUseCases: SheetUseCases
): ViewModel() {

    private val _uiState = MutableStateFlow(QuestionDetailsUiState())
    val uiState: StateFlow<QuestionDetailsUiState> = _uiState.asStateFlow()

    private val questionSlug: String = savedStateHandle.get<String>("questionSlug") ?: ""

    init {
        if (questionSlug.isNotEmpty()) {
            fetchQuestionDetails()
            loadSheets()
        }
    }
    
    private fun loadSheets() {
        viewModelScope.launch {
            // Ensure Favorites sheet exists
            val existingSheets = sheetUseCases.getAllSheetsWithQuestions.once()
            if (existingSheets.none { it.sheet.name == FAVORITES_SHEET_NAME }) {
                sheetUseCases.createSheet(FAVORITES_SHEET_NAME)
            }
            
            sheetUseCases.getAllSheetsWithQuestions().collectLatest { sheets ->
                // Sort sheets: Favorites first, then alphabetically
                val sortedSheets = sheets.sortedWith(compareBy(
                    { it.sheet.name != FAVORITES_SHEET_NAME },
                    { it.sheet.name.lowercase() }
                ))
                
                val questionSheetIds = sheetUseCases.getSheetsForQuestion(questionSlug)
                    .map { it.id }
                    .toSet()
                
                _uiState.update { it.copy(
                    allSheets = sortedSheets,
                    questionSheetIds = questionSheetIds
                ) }
            }
        }
    }
    
    fun toggleFabExpanded() {
        _uiState.update { it.copy(isFabExpanded = !it.isFabExpanded) }
    }
    
    fun closeFab() {
        _uiState.update { it.copy(isFabExpanded = false) }
    }
    
    fun showAddToSheetBottomSheet() {
        _uiState.update { it.copy(
            isAddToSheetBottomSheetVisible = true,
            isFabExpanded = false
        ) }
    }
    
    fun hideAddToSheetBottomSheet() {
        _uiState.update { it.copy(isAddToSheetBottomSheetVisible = false) }
    }
    
    fun updateNewSheetName(name: String) {
        _uiState.update { it.copy(newSheetName = name) }
    }
    
    fun createSheet() {
        val name = _uiState.value.newSheetName
        if (name.isBlank()) return
        
        viewModelScope.launch {
            _uiState.update { it.copy(isCreatingSheet = true) }
            
            sheetUseCases.createSheet(name).fold(
                onSuccess = { sheetId ->
                    // Automatically add the current question to the new sheet
                    _uiState.value.questionDetails?.let { details ->
                        sheetUseCases.addQuestionToSheet(
                            sheetId = sheetId,
                            questionSlug = questionSlug,
                            questionTitle = details.title,
                            questionDifficulty = details.difficulty,
                            questionFrontendId = details.questionId
                        )
                    }
                    
                    _uiState.update { it.copy(
                        isCreatingSheet = false,
                        newSheetName = ""
                    ) }
                    
                    // Refresh sheets
                    refreshQuestionSheets()
                },
                onFailure = { error ->
                    _uiState.update { it.copy(
                        isCreatingSheet = false,
                        sheetError = error.message
                    ) }
                }
            )
        }
    }
    
    fun addToSheet(sheetId: Long) {
        viewModelScope.launch {
            _uiState.value.questionDetails?.let { details ->
                sheetUseCases.addQuestionToSheet(
                    sheetId = sheetId,
                    questionSlug = questionSlug,
                    questionTitle = details.title,
                    questionDifficulty = details.difficulty,
                    questionFrontendId = details.questionId
                ).fold(
                    onSuccess = {
                        refreshQuestionSheets()
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(sheetError = error.message) }
                    }
                )
            }
        }
    }
    
    fun removeFromSheet(sheetId: Long) {
        viewModelScope.launch {
            sheetUseCases.removeQuestionFromSheet(sheetId, questionSlug).fold(
                onSuccess = {
                    refreshQuestionSheets()
                },
                onFailure = { error ->
                    _uiState.update { it.copy(sheetError = error.message) }
                }
            )
        }
    }
    
    private suspend fun refreshQuestionSheets() {
        val questionSheetIds = sheetUseCases.getSheetsForQuestion(questionSlug)
            .map { it.id }
            .toSet()
        
        _uiState.update { it.copy(questionSheetIds = questionSheetIds) }
    }
    
    fun clearSheetError() {
        _uiState.update { it.copy(sheetError = null) }
    }
    
    fun toggleFavorite() {
        viewModelScope.launch {
            // Find the Favorites sheet
            val favoritesSheet = _uiState.value.allSheets.find { it.sheet.name == FAVORITES_SHEET_NAME }
            
            if (favoritesSheet != null) {
                val isInFavorites = _uiState.value.questionSheetIds.contains(favoritesSheet.sheet.id)
                
                if (isInFavorites) {
                    // Remove from favorites
                    removeFromSheet(favoritesSheet.sheet.id)
                } else {
                    // Add to favorites
                    addToSheet(favoritesSheet.sheet.id)
                }
            }
            
            // Close FAB
            _uiState.update { it.copy(isFabExpanded = false) }
        }
    }
    
    fun isInFavorites(): Boolean {
        val favoritesSheet = _uiState.value.allSheets.find { it.sheet.name == FAVORITES_SHEET_NAME }
        return favoritesSheet != null && _uiState.value.questionSheetIds.contains(favoritesSheet.sheet.id)
    }

    fun fetchQuestionDetails() {
        viewModelScope.launch {
            getQuestionDetailsUseCase(questionSlug).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                    }
                    is Resource.Success -> {
                        result.data?.let { htmlContent ->
                            // Parse HTML on Default dispatcher
                            val questionDetails = withContext(Dispatchers.Default) {
                                parseHtmlToQuestionDetails(htmlContent)
                            }
                            
                            // Parse sections on Default dispatcher (heavy operation)
                            val parsedSections = withContext(Dispatchers.Default) {
                                parseSections(questionDetails.description)
                            }
                            
                            _uiState.value = _uiState.value.copy(
                                questionDetails = questionDetails,
                                parsedSections = parsedSections,
                                htmlContent = htmlContent,
                                isLoading = false,
                                isParsing = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.message ?: "Unknown error"
                        )
                    }
                }
            }
        }
    }

    private fun parseHtmlToQuestionDetails(html: String): QuestionDetails {
        return QuestionHtmlParser.parseQuestionDetails(html)
    }
}

data class QuestionDetailsUiState(
    val questionDetails: QuestionDetails? = null,
    val parsedSections: List<QuestionSection> = emptyList(),
    val htmlContent: String = "",
    val isLoading: Boolean = false,
    val isParsing: Boolean = false,
    val error: String? = null,
    // Sheet-related state
    val isFabExpanded: Boolean = false,
    val isAddToSheetBottomSheetVisible: Boolean = false,
    val allSheets: List<SheetWithQuestions> = emptyList(),
    val questionSheetIds: Set<Long> = emptySet(),
    val newSheetName: String = "",
    val isCreatingSheet: Boolean = false,
    val sheetError: String? = null
)