package com.devrachit.ken.presentation.screens.dashboard.questions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.devrachit.ken.domain.models.Question
import com.devrachit.ken.domain.models.QuestionSearchRequest
import com.devrachit.ken.domain.usecases.questions.GetQuestionsUseCase
import com.devrachit.ken.domain.usecases.questions.SearchQuestionsUseCase
import com.devrachit.ken.domain.usecases.questions.UpdateQuestionStatusUseCase
import com.devrachit.ken.domain.usecases.questions.GetQuestionFiltersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val searchQuestionsRemoteUseCase: com.devrachit.ken.domain.usecases.questions.SearchQuestionsRemoteUseCase,
    private val updateQuestionStatusUseCase: UpdateQuestionStatusUseCase,
    private val getQuestionFiltersUseCase: GetQuestionFiltersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestionsUiState())
    val uiState: StateFlow<QuestionsUiState> = _uiState.asStateFlow()

    // Separate paging request (NOT affected by search)
    private val _pagingRequest = MutableStateFlow(QuestionSearchRequest())

    // Search query flow (separate from paging)
    private val _searchQuery = MutableStateFlow("")

    private val _searchResults = MutableStateFlow<List<Question>>(emptyList())
    val searchResults: StateFlow<List<Question>> = _searchResults.asStateFlow()

    private val _availableFilters = MutableStateFlow(QuestionFilters())
    val availableFilters: StateFlow<QuestionFilters> = _availableFilters.asStateFlow()

    // Cache to avoid redundant API calls
    private val searchCache = mutableMapOf<String, List<Question>>()
    private var currentSearchJob: kotlinx.coroutines.Job? = null

    // Paging data flow (INDEPENDENT of search)
    val questionsFlow = _pagingRequest
        .flatMapLatest { request ->
            getQuestionsUseCase(request)
        }
        .cachedIn(viewModelScope)

    init {
        loadAvailableFilters()

        // Setup search with debouncing and caching
        viewModelScope.launch {
            _searchQuery
                .debounce(500) // Increased debounce for better UX
                .distinctUntilChanged()
                .collect { query ->
                    if (query.length >= 2) {
                        performSearch(query)
                    } else {
                        _searchResults.value = emptyList()
                        _uiState.value = _uiState.value.copy(
                            showSearchSuggestions = false,
                            isSearching = false
                        )
                    }
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(
            searchText = query,
            showSearchSuggestions = query.isNotEmpty()
        )
        _searchQuery.value = query
    }

    fun hideSearchSuggestions() {
        _uiState.value = _uiState.value.copy(showSearchSuggestions = false)
    }

    fun updateQuestionStatus(questionId: Int, status: String) {
        viewModelScope.launch {
            try {
                updateQuestionStatusUseCase(questionId, status)
            } catch (_: Exception) {
                // Handle error - could emit to UI state if needed
            }
        }
    }

    private fun loadAvailableFilters() {
        viewModelScope.launch {
            try {
                val difficulties = getQuestionFiltersUseCase.getDifficulties()
                val topicTags = getQuestionFiltersUseCase.getTopicTags()

                _availableFilters.value = QuestionFilters(
                    difficulties = difficulties,
                    topicTags = topicTags,
                    statuses = listOf("Solved", "Attempted", "Not Started")
                )
            } catch (_: Exception) {
                // Handle error loading filters
            }
        }
    }

    private fun performSearch(query: String) {
        // Cancel previous search job
        currentSearchJob?.cancel()

        // Check cache first
        searchCache[query]?.let { cachedResults ->
            _searchResults.value = cachedResults
            _uiState.value = _uiState.value.copy(
                showSearchSuggestions = true,
                isSearching = false
            )
            return
        }

        // Perform new search
        currentSearchJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true)
            try {
                val results = searchQuestionsRemoteUseCase(query, limit = 50)
                
                // Cache the results
                searchCache[query] = results
                
                // Limit cache size to prevent memory issues
                if (searchCache.size > 20) {
                    searchCache.remove(searchCache.keys.first())
                }
                
                _searchResults.value = results
                _uiState.value = _uiState.value.copy(
                    showSearchSuggestions = true,
                    isSearching = false
                )
            } catch (e: Exception) {
                _searchResults.value = emptyList()
                _uiState.value = _uiState.value.copy(
                    isSearching = false,
                    error = "Search failed: ${e.message}"
                )
            }
        }
    }
}

data class QuestionsUiState(
    val isLoading: Boolean = false,
    val searchText: String = "",
    val showSearchSuggestions: Boolean = false,
    val isSearching: Boolean = false,
    val showFilters: Boolean = false,
    val selectedDifficulty: String? = null,
    val selectedStatus: String? = null,
    val selectedTags: List<String> = emptyList(),
    val error: String? = null
)

data class QuestionFilters(
    val difficulties: List<String> = emptyList(),
    val topicTags: List<String> = emptyList(),
    val statuses: List<String> = emptyList()
)
