package com.devrachit.ken.presentation.screens.dashboard.questions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems

import com.devrachit.ken.R
import com.devrachit.ken.domain.models.Question
import com.devrachit.ken.presentation.screens.dashboard.questions.components.QuestionItemCard
import com.devrachit.ken.presentation.screens.dashboard.questions.components.QuestionsEnhancedSearchWidget
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.utility.composeUtility.sdp
import com.devrachit.ken.utility.composeUtility.QuestionItemShimmer

@Composable
fun QuestionsScreen(
    onQuestionClick: (String) -> Unit = {},
    questionsViewModel: QuestionsViewModel = hiltViewModel()
) {
    val uiState by questionsViewModel.uiState.collectAsState()
    val searchResults by questionsViewModel.searchResults.collectAsState()
    val availableFilters by questionsViewModel.availableFilters.collectAsState()
    val lazyPagingItems = questionsViewModel.questionsFlow.collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content with questions list
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.bg_neutral))
                .statusBarsPadding()
                .padding(top = 0.sdp) // Add top padding for floating search widget
        ) {
//                    filters = availableFilters,
//                    selectedDifficulty = uiState.selectedDifficulty,
//                    selectedStatus = uiState.selectedStatus,
//                    selectedTags = uiState.selectedTags,
//                    onDifficultySelected = questionsViewModel::updateDifficultyFilter,
//                    onStatusSelected = questionsViewModel::updateStatusFilter,
//                    onTagToggled = { tag ->
//                        val currentTags = uiState.selectedTags.toMutableList()
//                        if (currentTags.contains(tag)) {
//                            currentTags.remove(tag)
//                        } else {
//                            currentTags.add(tag)
//                        }
//                        questionsViewModel.updateTagsFilter(currentTags)
//                    },
//                    onClearFilters = questionsViewModel::clearAllFilters
//                )
//
//                Spacer(modifier = Modifier.height(16.sdp))
//            }
//        }

            // Questions list
            QuestionsListContent(
                lazyPagingItems = lazyPagingItems,
                onQuestionClick = { question ->
                    onQuestionClick(question.titleSlug)
                },
                onStatusUpdate = questionsViewModel::updateQuestionStatus
            )
        }

        // Floating Enhanced Search Widget
//        QuestionsEnhancedSearchWidget(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 16.sdp, end = 16.sdp, top = 18.sdp),
//            placeholder = "Search questions by title, topic, or difficulty...",
//            searchText = uiState.searchText,
//            searchResults = searchResults,
//            showSuggestions = uiState.showSearchSuggestions,
//            isSearching = uiState.isSearching,
//            onSearchTextChange = { newText ->
//                questionsViewModel.updateSearchQuery(newText)
//            },
//            onQuestionClick = { question ->
//                questionsViewModel.hideSearchSuggestions()
//                onQuestionClick(question.titleSlug)
//            },
//            onClearSearch = {
//                questionsViewModel.updateSearchQuery("")
//                questionsViewModel.hideSearchSuggestions()
//            }
//        )
    }
}

@Composable
private fun QuestionsListContent(
    lazyPagingItems: LazyPagingItems<Question>,
    onQuestionClick: (Question) -> Unit,
    onStatusUpdate: (Int, String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        items(
            count = lazyPagingItems.itemCount,
            key = { index ->
                lazyPagingItems[index]?.id ?: index
            }
        ) { index ->
            val question = lazyPagingItems[index]
            if (question != null) {
                QuestionItemCard(
                    question = question,
                    onQuestionClick = onQuestionClick,
                    onStatusUpdate = onStatusUpdate
                )
            } else {
                QuestionItemShimmer()
            }
        }

        lazyPagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    items(10) {
                        QuestionItemShimmer()
                    }
                }
                loadState.append is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.sdp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = colorResource(R.color.white),
                                modifier = Modifier.size(24.sdp)
                            )
                        }
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    item {
                        ErrorItem(
                            message = (loadState.refresh as LoadState.Error).error.message ?: "Unknown error",
                            onRetry = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    item {
                        ErrorItem(
                            message = (loadState.append as LoadState.Error).error.message ?: "Unknown error",
                            onRetry = { retry() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorItem(
    message: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.sdp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.card_elevated)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error loading questions",
                style = TextStyleInter16Lh24Fw700(),
                color = colorResource(R.color.white),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.sdp))

            Text(
                text = message,
                style = TextStyleInter14Lh20Fw400(),
                color = colorResource(R.color.grayblue_normal_600)
            )

            Spacer(modifier = Modifier.height(16.sdp))

            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.blue_normal_500)
                )
            ) {
                Text("Retry", color = colorResource(R.color.white))
            }
        }
    }
}