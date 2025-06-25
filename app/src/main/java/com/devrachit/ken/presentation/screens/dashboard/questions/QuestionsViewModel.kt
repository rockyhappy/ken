package com.devrachit.ken.presentation.screens.dashboard.questions

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.data.local.dao.LeetCodeUserBadgesDao
import com.devrachit.ken.domain.usecases.getQuestionsUseCase.GetQuestionsUseCase
import com.devrachit.ken.utility.NetworkUtility.Resource
import com.github.mikephil.charting.utils.Utils.init
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
private const val TAG = "QUESTION_VM"
@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(QuestionUiState())
    val uiState: StateFlow<QuestionUiState> = _uiState.asStateFlow()

    init {
        fetchQuestions()
    }

    fun onEvent(event: QuestionEvent) {
        when (event) {
            is QuestionEvent.LoadQuestions -> {
                fetchQuestions()
            }
        }
    }

    private fun fetchQuestions() {
        viewModelScope.launch(Dispatchers.IO) {
            getQuestionsUseCase(skip = uiState.value.skip, limit = uiState.value.limit).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Log.d(TAG, "fetchQuestions: Loading")
                        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                    }

                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            questionList = _uiState.value.questionList + (resource.data?.mapNotNull { it } ?: emptyList()),
                            isLoading = false,
                            errorMessage = null,
                            page = _uiState.value.page + 1
                        )
                    }

                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(errorMessage = resource.message, isLoading = false)
                    }
                }

            }
        }
    }

}