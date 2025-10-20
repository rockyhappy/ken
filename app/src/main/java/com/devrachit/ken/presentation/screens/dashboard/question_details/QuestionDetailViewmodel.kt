package com.devrachit.ken.presentation.screens.dashboard.question_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.domain.models.QuestionDetails
import com.devrachit.ken.domain.usecases.getQuestionDetails.GetQuestionDetailsUseCase
import com.devrachit.ken.utility.NetworkUtility.Resource
import com.devrachit.ken.utility.QuestionHtmlParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
                            val questionDetails = parseHtmlToQuestionDetails(htmlContent)
                            _uiState.value = _uiState.value.copy(
                                questionDetails = questionDetails,
                                htmlContent = htmlContent,
                                isLoading = false,
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
    val htmlContent: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)