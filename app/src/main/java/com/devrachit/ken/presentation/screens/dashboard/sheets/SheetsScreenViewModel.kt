package com.devrachit.ken.presentation.screens.dashboard.sheets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.domain.models.QuestionFolder
import com.devrachit.ken.domain.models.SavedQuestion
import com.devrachit.ken.domain.usecases.savedQuestions.CreateFolderUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.DeleteFolderUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.DeleteQuestionUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.GetAllFoldersUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.GetQuestionsByFolderUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.SaveQuestionUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.MarkQuestionSolvedUseCase
import com.devrachit.ken.domain.usecases.savedQuestions.UpdateQuestionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SheetsScreenViewModel @Inject constructor(
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val getQuestionsByFolderUseCase: GetQuestionsByFolderUseCase,
    private val createFolderUseCase: CreateFolderUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val deleteQuestionUseCase: DeleteQuestionUseCase,
    private val saveQuestionUseCase: SaveQuestionUseCase,
    private val markQuestionSolvedUseCase: MarkQuestionSolvedUseCase,
    private val updateQuestionUseCase: UpdateQuestionUseCase
) : ViewModel() {

    private val _folders = MutableStateFlow<List<QuestionFolder>>(emptyList())
    val folders: StateFlow<List<QuestionFolder>> = _folders.asStateFlow()

    private val _selectedFolder = MutableStateFlow<QuestionFolder?>(null)
    val selectedFolder: StateFlow<QuestionFolder?> = _selectedFolder.asStateFlow()

    private val _selectedFolderQuestions = MutableStateFlow<List<SavedQuestion>>(emptyList())
    val selectedFolderQuestions: StateFlow<List<SavedQuestion>> = _selectedFolderQuestions.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Navigation levels: "folders" -> "questions"
    private val _navigationLevel = MutableStateFlow<String>("folders")
    val navigationLevel: StateFlow<String> = _navigationLevel.asStateFlow()

    init {
        android.util.Log.d("SheetsScreenViewModel", "init: ViewModel initialized")
        // Start listening to folders updates
        observeFolders()

        // Listen to folder changes and load questions
        viewModelScope.launch {
            selectedFolder.collect { folder ->
                if (folder != null) {
                    android.util.Log.d("SheetsScreenViewModel", "init: Selected folder changed to ${folder.folderName}")
                    loadQuestionsForFolder(folder.folderId)
                }
            }
        }
    }

    private fun observeFolders() {
        viewModelScope.launch {
            try {
                android.util.Log.d("SheetsScreenViewModel", "observeFolders: Starting to collect folders")
                getAllFoldersUseCase().collect { folderList ->
                    android.util.Log.d("SheetsScreenViewModel", "observeFolders: Received ${folderList.size} folders")
                    folderList.forEach { folder ->
                        android.util.Log.d("SheetsScreenViewModel", "  - Folder: ${folder.folderName} (ID: ${folder.folderId})")
                    }
                    _folders.value = folderList
                    // Auto-select first folder if none selected
                    if (folderList.isNotEmpty() && _selectedFolder.value == null) {
                        _selectedFolder.value = folderList.first()
                    }
                    // Always set loading to false when data is received
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                android.util.Log.e("SheetsScreenViewModel", "observeFolders: Error - ${e.message}", e)
                _errorMessage.value = "Error loading folders: ${e.message}"
                _isLoading.value = false
            }
        }
        _isLoading.value = true
        android.util.Log.d("SheetsScreenViewModel", "observeFolders: Initial loading state set to true")
    }

    private fun loadQuestionsForFolder(folderId: Int) {
        viewModelScope.launch {
            try {
                getQuestionsByFolderUseCase(folderId).collect { questions ->
                    _selectedFolderQuestions.value = questions
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading questions: ${e.message}"
            }
        }
    }

    fun selectFolder(folder: QuestionFolder) {
        _selectedFolder.value = folder
    }

    fun createFolder(folderName: String, folderDescription: String = "") {
        viewModelScope.launch {
            try {
                android.util.Log.d("SheetsScreenViewModel", "createFolder: Creating folder '$folderName'")
                _isLoading.value = true
                val folderId = createFolderUseCase(folderName, folderDescription)
                android.util.Log.d("SheetsScreenViewModel", "createFolder: Folder created with ID $folderId")
                _isLoading.value = false
            } catch (e: Exception) {
                android.util.Log.e("SheetsScreenViewModel", "createFolder: Error - ${e.message}", e)
                _errorMessage.value = "Error creating folder: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun deleteFolder(folderId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                deleteFolderUseCase(folderId)
                _selectedFolder.value = null
                _selectedFolderQuestions.value = emptyList()
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Error deleting folder: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun saveQuestion(
        questionTitle: String,
        questionSlug: String,
        folderId: Int,
        questionDifficulty: String = "",
        questionUrl: String = ""
    ) {
        viewModelScope.launch {
            try {
                val question = SavedQuestion(
                    questionTitle = questionTitle,
                    questionSlug = questionSlug,
                    folderId = folderId,
                    questionDifficulty = questionDifficulty,
                    questionUrl = questionUrl
                )
                saveQuestionUseCase(question)
            } catch (e: Exception) {
                _errorMessage.value = "Error saving question: ${e.message}"
            }
        }
    }

    fun deleteQuestion(questionId: Int) {
        viewModelScope.launch {
            try {
                deleteQuestionUseCase(questionId)
            } catch (e: Exception) {
                _errorMessage.value = "Error deleting question: ${e.message}"
            }
        }
    }

    fun markQuestionSolved(questionId: Int, isSolved: Boolean) {
        viewModelScope.launch {
            try {
                markQuestionSolvedUseCase(questionId, isSolved)
            } catch (e: Exception) {
                _errorMessage.value = "Error updating question: ${e.message}"
            }
        }
    }

    fun updateQuestionNotes(questionId: Int, notes: String) {
        viewModelScope.launch {
            try {
                updateQuestionUseCase(questionId, notes)
            } catch (e: Exception) {
                _errorMessage.value = "Error updating question: ${e.message}"
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    // Navigation methods
    fun goToQuestions(folder: QuestionFolder) {
        _selectedFolder.value = folder
        _navigationLevel.value = "questions"
    }

    fun goBack() {
        when (_navigationLevel.value) {
            "questions" -> {
                _navigationLevel.value = "folders"
                _selectedFolder.value = null
                _selectedFolderQuestions.value = emptyList()
            }
            else -> {
                // Already at folders level
            }
        }
    }
}

