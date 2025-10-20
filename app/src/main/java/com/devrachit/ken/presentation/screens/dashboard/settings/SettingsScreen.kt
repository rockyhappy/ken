package com.devrachit.ken.presentation.screens.dashboard.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.settings.components.BadgeDisplayModeSelector
import com.devrachit.ken.presentation.screens.dashboard.settings.components.DisplayTypeSelector
import com.devrachit.ken.presentation.screens.dashboard.settings.components.QuestionDetailsViewModeSelector
import com.devrachit.ken.presentation.screens.dashboard.settings.components.RecentSubmissionLimitSelector
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun SettingsScreen() {
    val viewmodel = hiltViewModel<SettingsViewmodel>()
    val currentDisplayType by viewmodel.displayType.collectAsState()
    val currentBadgeDisplayMode by viewmodel.badgeDisplayMode.collectAsState()
    val currentSubmissionLimit by viewmodel.recentSubmissionLimit.collectAsState()
    val currentQuestionDetailsViewMode by viewmodel.questionDetailsViewMode.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.bg_neutral))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.sdp, vertical = 24.sdp)
    ) {
        Text(
            text = "Settings",
            color = colorResource(R.color.white),
            style = TextStyleInter16Lh24Fw700(),
            modifier = Modifier.padding(bottom = 16.sdp)
        )
        
        DisplayTypeSelector(
            currentDisplayType = currentDisplayType,
            onDisplayTypeChanged = { displayType ->
                viewmodel.updateDisplayType(displayType)
            }
        )
        
        Spacer(modifier = Modifier.height(12.sdp))
        
        BadgeDisplayModeSelector(
            currentBadgeDisplayMode = currentBadgeDisplayMode,
            onBadgeDisplayModeChanged = { displayMode ->
                viewmodel.updateBadgeDisplayMode(displayMode)
            }
        )
        
        Spacer(modifier = Modifier.height(12.sdp))
        
        RecentSubmissionLimitSelector(
            currentLimit = currentSubmissionLimit,
            onLimitChanged = { limit ->
                viewmodel.updateRecentSubmissionLimit(limit)
            }
        )
        
        Spacer(modifier = Modifier.height(12.sdp))
        
        QuestionDetailsViewModeSelector(
            currentViewMode = currentQuestionDetailsViewMode,
            onViewModeChanged = { viewMode ->
                viewmodel.updateQuestionDetailsViewMode(viewMode)
            }
        )
        Spacer(modifier = Modifier.height(100.sdp))
    }
}