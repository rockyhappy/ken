package com.devrachit.ken.presentation.screens.dashboard.compare.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.compare.CompareUiStates
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun FriendsListView(
    usernames: List<String>,
    uiState: CompareUiStates,
    onViewProfile: (String) -> Unit,
    onCompareWith: (String) -> Unit,
    onRemoveUser: (String) -> Unit,
    onRefreshUser: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.sdp)
            .padding(horizontal = 16.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        items(usernames) { username ->
            val userInfo = uiState.friendsDetails?.get(username)

            if (userInfo != null) {
                FriendListItemWidget(
                    username = username,
                    realName = userInfo.profile?.realName ?: "",
                    onViewProfile = onViewProfile,
                    onRemoveUser = onRemoveUser
                )
            }
        }
    }
}

@Composable
private fun FriendListItemWidget(
    username: String,
    realName: String,
    onViewProfile: (String) -> Unit,
    onRemoveUser: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onViewProfile(username) },
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.card_elevated)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.sdp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.sdp),
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
                    .size(20.sdp)
                    .clickable { onRemoveUser(username) }
            )
        }
    }
}
