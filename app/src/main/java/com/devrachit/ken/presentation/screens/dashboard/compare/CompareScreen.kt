package com.devrachit.ken.presentation.screens.dashboard.compare

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.Widgets.HeatmapCard
import com.devrachit.ken.presentation.screens.dashboard.compare.components.CompareList
import com.devrachit.ken.presentation.screens.dashboard.compare.components.CompareSinglePersonWidget
import com.devrachit.ken.presentation.screens.dashboard.compare.components.QuestionProgressGraphs
import com.devrachit.ken.presentation.screens.dashboard.compare.components.StreakActivityGraphs
import com.devrachit.ken.presentation.screens.dashboard.Widgets.EnhancedSearchWidget
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw700
import com.devrachit.ken.utility.composeUtility.HomeScreenShimmer
import com.devrachit.ken.utility.composeUtility.sdp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompareScreen(
    uiState: CompareUiStates,
    loadingStates: LoadingStates,
    onFirstLoad: () -> Unit = {},
    onRefreshAllData: () -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
    onSuggestionClick: (String, com.devrachit.ken.domain.models.LeetCodeUserInfo) -> Unit = { _, _ -> },
    onNavigateToUserDetails: (String) -> Unit = {},
    onNavigateToCompareUsers: (String) -> Unit = {},
    onPlatformSearch: () -> Unit = {},
    onHidePlatformResult: () -> Unit = {},
    onRemoveUser: (String) -> Unit = {},
    onRefreshUser: (String) -> Unit = {},
    getEasyGraphData: () -> List<com.devrachit.ken.presentation.screens.dashboard.compare.QuestionGraphData> = { emptyList() },
    getMediumGraphData: () -> List<com.devrachit.ken.presentation.screens.dashboard.compare.QuestionGraphData> = { emptyList() },
    getHardGraphData: () -> List<com.devrachit.ken.presentation.screens.dashboard.compare.QuestionGraphData> = { emptyList() }
) {
    val (hasInitiallyLoaded, setHasInitiallyLoaded) = remember { mutableStateOf(false) }
    val firebaseAnalytics = Firebase.analytics

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = {
            firebaseAnalytics.logEvent("compare_screen_refresh") {
                param("users_count", uiState.friendsDetails?.size?.toLong() ?: 0L)
            }
            onFirstLoad.invoke()
        }
    )

    LaunchedEffect(true) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "compare_screen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "CompareScreen")
        }
        onFirstLoad.invoke()
        if (!hasInitiallyLoaded) {
            setHasInitiallyLoaded(true)
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .background(color = colorResource(R.color.bg_neutral))
                .verticalScroll(rememberScrollState())
                .padding(top = 60.sdp, bottom=120.sdp), // Add top padding to account for floating search widget
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                uiState.currentTimestamp != null -> {
                    CompareList(
                        modifier = Modifier.padding(top = 8.sdp),
                        uiState = uiState,
                        onRemoveUser = { username ->
                            firebaseAnalytics.logEvent("compare_user_removed") {
                                param("username", username)
                                param("remaining_users", (uiState.friendsDetails?.size ?: 1) - 1L)
                            }
                            onRemoveUser(username)
                        },
                        onRefreshUser = { username ->
                            firebaseAnalytics.logEvent("compare_user_refreshed") {
                                param("username", username)
                            }
                            onRefreshUser(username)
                        },
                        onViewProfile = { username ->
                            firebaseAnalytics.logEvent("compare_view_profile") {
                                param("username", username)
                                param("source", "compare_list")
                            }
                            onNavigateToUserDetails(username)
                        },
                        onCompareWith = { username ->
                            firebaseAnalytics.logEvent("compare_navigate_to_compare_users") {
                                param("username", username)
                                param("source", "compare_list")
                            }
                            onNavigateToCompareUsers.invoke(username)
                        }
                    )
                    
                    // Show graphs only when data is available and not loading
                    if (!uiState.isLoading && !uiState.friendsQuestionProgressInfo.isNullOrEmpty()) {
                        QuestionProgressGraphs(
                            modifier = Modifier.padding(horizontal = 16.sdp, vertical = 16.sdp),
                            easyData = getEasyGraphData(),
                            mediumData = getMediumGraphData(),
                            hardData = getHardGraphData()
                        )
                    }
                    
                    // Streak Activity Graphs
                    if (!uiState.isLoading && !uiState.userProfileCalender.isNullOrEmpty()) {
                        StreakActivityGraphs(
                            modifier = Modifier.padding(horizontal = 16.sdp, vertical = 16.sdp),
                            userCalendarData = uiState.userProfileCalender ?: emptyMap(),
                            userDetails = uiState.friendsDetails ?: emptyMap()
                        )
                    }
                }

                else ->
                {
                    Spacer(modifier = Modifier.height(26.sdp))
                    HomeScreenShimmer()
                }

            }
        }

        EnhancedSearchWidget(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.sdp, end= 16.sdp, top= 18.sdp),
            placeholder = "Search users to compare...",
            searchText = uiState.searchQuery,
            localResults = uiState.searchResults,
            showSuggestions = uiState.showSearchSuggestions,
            platformResult = uiState.platformSearchResult,
            platformError = uiState.platformSearchError,
            isPlatformSearching = uiState.isPlatformSearching,
            showPlatformResult = uiState.showPlatformResult,
            onSearchTextChange = { newText ->
                if (newText.isNotEmpty()) {
                    firebaseAnalytics.logEvent("compare_search_query") {
                        param("query_length", newText.length.toLong())
                        param("has_results", (uiState.searchResults?.isNotEmpty() == true).toString())
                    }
                }
                onSearchTextChange.invoke(newText)
            },
            onLocalResultClick = { username, userInfo ->
                firebaseAnalytics.logEvent("compare_user_selected") {
                    param("username", username)
                    param("source", "local_search")
                }
                onSuggestionClick.invoke(username, userInfo)
            },
            onPlatformSearch = {
                firebaseAnalytics.logEvent("compare_platform_search") {
                    param("query", uiState.searchQuery)
                }
                onPlatformSearch.invoke()
            },
            onNavigateToUserDetails = { username ->
                firebaseAnalytics.logEvent("compare_view_profile") {
                    param("username", username)
                    param("source", "search_widget")
                }
                onNavigateToUserDetails.invoke(username)
            },
            onHidePlatformResult = {
                firebaseAnalytics.logEvent("compare_hide_platform_result") {}
                onHidePlatformResult.invoke()
            }
        )

        PullRefreshIndicator(
            refreshing = uiState.isLoading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            backgroundColor = colorResource(id = R.color.card_elevated),
            contentColor = colorResource(id = R.color.white)
        )
    }
}
