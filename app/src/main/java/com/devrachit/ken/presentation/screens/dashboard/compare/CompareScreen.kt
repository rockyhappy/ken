package com.devrachit.ken.presentation.screens.dashboard.compare

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.devrachit.ken.presentation.screens.dashboard.Widgets.SearchWidget
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw700
import com.devrachit.ken.utility.composeUtility.HomeScreenShimmer
import com.devrachit.ken.utility.composeUtility.sdp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CompareScreen(
    uiState: CompareUiStates,
    loadingStates: LoadingStates,
    onFirstLoad: () -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
    onSuggestionClick: (String, com.devrachit.ken.domain.models.LeetCodeUserInfo) -> Unit = { _, _ -> },
    onNavigateToUserDetails: (String) -> Unit = {}
) {
    val (hasInitiallyLoaded, setHasInitiallyLoaded) = remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = {
            onFirstLoad.invoke()
        }
    )

    LaunchedEffect(true) {
        if (!hasInitiallyLoaded) {
            onFirstLoad.invoke()
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
                .padding(top = 60.sdp), // Add top padding to account for floating search widget
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                uiState.currentTimestamp != null -> {
                    CompareList(
                        modifier = Modifier.padding(top = 8.sdp),
                        uiState = uiState
                    )
                }

                else -> HomeScreenShimmer()
            }

        }

        SearchWidget(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.sdp, end= 16.sdp, top= 18.sdp),
            placeholder = "Search users to compare...",
            searchText = uiState.searchQuery,
            suggestions = uiState.searchResults,
            showSuggestions = uiState.showSearchSuggestions,
            onSearchTextChange = { newText ->
                onSearchTextChange.invoke(newText)
            },
            onSuggestionClick = { username, userInfo ->
                onSuggestionClick.invoke(username, userInfo)
            },
            enableNavigation = true,
            onNavigateToUserDetails = { username ->
                onNavigateToUserDetails.invoke(username)
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
