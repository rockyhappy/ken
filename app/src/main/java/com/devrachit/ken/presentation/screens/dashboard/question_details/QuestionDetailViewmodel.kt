package com.devrachit.ken.presentation.screens.dashboard.question_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.domain.models.QuestionDetails
import com.devrachit.ken.domain.usecases.getQuestionDetails.GetQuestionDetailsUseCase
import com.devrachit.ken.presentation.screens.dashboard.question_details.components.QuestionSection
import com.devrachit.ken.presentation.screens.dashboard.question_details.components.parseSections
import com.devrachit.ken.utility.NetworkUtility.Resource
import com.devrachit.ken.utility.QuestionHtmlParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuestionDetailViewmodel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getQuestionDetailsUseCase: GetQuestionDetailsUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(QuestionDetailsUiState())
    val uiState: StateFlow<QuestionDetailsUiState> = _uiState.asStateFlow()

    private val questionSlug: String = savedStateHandle.get<String>("questionSlug") ?: ""

    init {
        if (questionSlug.isNotEmpty()) {
            fetchQuestionDetails()
        }
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
    val error: String? = null
)