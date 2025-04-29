package com.devrachit.ken.presentation.screens.dashboard.Widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.home.QuestionProgressUiState
import com.devrachit.ken.utility.composeUtility.SegmentedProgressIndicator2
import com.devrachit.ken.utility.composeUtility.sdp

/**
 * A widget that displays question progress statistics with a segmented progress indicator
 * and detail stats for each difficulty level.
 *
 * @param questionProgress The data containing all progress statistics
 * @param modifier Optional modifier for the card
 */
@Composable
fun QuestionProgressCard(
    questionProgress: QuestionProgressUiState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(200.sdp)
            .clip(RoundedCornerShape(10.sdp))
            .border(
                border = BorderStroke(
                    width = 2.sdp,
                    color = colorResource(R.color.card_elevated)
                ),
                shape = RoundedCornerShape(36.sdp),
            )
            .padding(top = 20.sdp, start = 20.sdp, end = 5.sdp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Progress indicator on the left
        SegmentedProgressIndicator2(
            solved = questionProgress.solved,
            attempting = questionProgress.attempting,
            total = questionProgress.total,
            easyTotalCount = questionProgress.easyTotalCount,
            easySolvedCount = questionProgress.easySolvedCount,
            mediumTotalCount = questionProgress.mediumTotalCount,
            mediumSolvedCount = questionProgress.mediumSolvedCount,
            hardTotalCount = questionProgress.hardTotalCount,
            hardSolvedCount = questionProgress.hardSolvedCount,
            modifier = Modifier.size(180.sdp)
        )
        
        // Difficulty statistics on the right
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 20.sdp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            // Easy problems
            KinkWidget(
                colorResourceId = R.color.easy_filled_blue,
                tag = "Easy",
                totalCount = questionProgress.easyTotalCount,
                attemptedCount = questionProgress.easySolvedCount,
            )
            
            // Medium problems
            KinkWidget(
                colorResourceId = R.color.medium_filled_yellow,
                tag = "Med.",
                totalCount = questionProgress.mediumTotalCount,
                attemptedCount = questionProgress.mediumSolvedCount,
            )
            
            // Hard problems
            KinkWidget(
                colorResourceId = R.color.hard_filled_red,
                tag = "Hard",
                totalCount = questionProgress.hardTotalCount,
                attemptedCount = questionProgress.hardSolvedCount,
            )
        }
    }
}