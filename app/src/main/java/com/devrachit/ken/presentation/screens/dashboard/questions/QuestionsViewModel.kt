package com.devrachit.ken.presentation.screens.dashboard.questions

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

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(QuestionUiState())
    val uiState: StateFlow<QuestionUiState> = _uiState.asStateFlow()

    init {
        fetchQuestions(100)
    }

    private fun fetchQuestions(limit: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getQuestionsUseCase(limit).collect { it ->
                when (it) {

                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                    }

                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            questionList = it.data?.mapNotNull { it } ?: emptyList(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }

                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(errorMessage = it.message, isLoading = false)
                    }
                }

            }
        }
    }

}