package com.devrachit.ken.presentation.screens.dashboard.compare.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.compare.CompareUiStates
import com.devrachit.ken.utility.composeUtility.SegmentedProgressIndicator2
import com.devrachit.ken.utility.composeUtility.sdp
import kotlinx.coroutines.delay


@Composable
fun FriendsGridView(
    usernames: List<String>,
    uiState: CompareUiStates,
    onViewProfile: (String) -> Unit,
    onCompareWith: (String) -> Unit,
    onRemoveUser: (String) -> Unit,
    onRefreshUser: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(400.sdp)
            .padding(horizontal = 16.sdp),
        horizontalArrangement = Arrangement.spacedBy(8.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        itemsIndexed(usernames) { index, username ->
            val userInfo = uiState.friendsDetails?.get(username)
            val questionProgress = uiState.friendsQuestionProgressInfo?.get(username)
            var isVisible by remember { mutableStateOf(false) }

            LaunchedEffect(username) {
                delay(index * 120L) // Stagger the animations slightly more for grid
                isVisible = true
            }

            if (userInfo != null && questionProgress != null) {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = scaleIn(
                        initialScale = 0.8f,
                        animationSpec = tween(350)
                    ) + fadeIn(animationSpec = tween(350))
                ) {
                    FriendGridItemWidget(
                        modifier = Modifier,
                        username = username,
                        realName = userInfo.profile?.realName ?: "",
                        totalSolved = questionProgress.solved,
                        totalQuestions = questionProgress.total,
                        easySolved = questionProgress.easySolvedCount,
                        easyTotal = questionProgress.easyTotalCount,
                        mediumSolved = questionProgress.mediumSolvedCount,
                        mediumTotal = questionProgress.mediumTotalCount,
                        hardSolved = questionProgress.hardSolvedCount,
                        hardTotal = questionProgress.hardTotalCount,
                        onViewProfile = onViewProfile,
                        onRemoveUser = onRemoveUser
                    )
                }
            }
        }
    }
}

@Composable
private fun FriendGridItemWidget(
    modifier: Modifier = Modifier,
    username: String,
    realName: String,
    totalSolved: Int,
    totalQuestions: Int,
    easySolved: Int,
    easyTotal: Int,
    mediumSolved: Int,
    mediumTotal: Int,
    hardSolved: Int,
    hardTotal: Int,
    onViewProfile: (String) -> Unit,
    onRemoveUser: (String) -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onViewProfile(username) },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.card_elevated)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.sdp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with name and options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = realName.takeIf { it.isNotBlank() } ?: username,
                        color = colorResource(R.color.white),
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (realName.isNotBlank()) {
                        Text(
                            text = "@$username",
                            color = colorResource(R.color.white).copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = colorResource(R.color.white),
                    modifier = Modifier
                        .size(16.sdp)
                        .clickable { onRemoveUser(username) }
                )
            }

            Spacer(modifier = Modifier.height(8.sdp))

            // Progress indicator
            SegmentedProgressIndicator2(
                solved = totalSolved,
                attempting = 0, // We don't have attempting data in the current structure
                total = totalQuestions,
                easyTotalCount = easyTotal,
                easySolvedCount = easySolved,
                mediumTotalCount = mediumTotal,
                mediumSolvedCount = mediumSolved,
                hardTotalCount = hardTotal,
                hardSolvedCount = hardSolved,
                strokeWidth = 12f
            )
        }
    }
}
