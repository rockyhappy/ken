package com.devrachit.ken.presentation.screens.dashboard.compare.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.compare.CompareUiStates
import com.devrachit.ken.ui.theme.TextStyleInter14Lh16Fw600
import com.devrachit.ken.ui.theme.TextStyleInter18Lh24Fw700
import com.devrachit.ken.ui.theme.TextStyleInter20Lh32Fw700
import com.devrachit.ken.utility.composeUtility.sdp
import kotlinx.coroutines.delay

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
            .heightIn(min = 110.sdp, max = 350.sdp)
            .padding(horizontal = 16.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        itemsIndexed(usernames) { index, username ->
            val userInfo = uiState.friendsDetails?.get(username)
            var isVisible by remember { mutableStateOf(false) }

            LaunchedEffect(username) {
//                delay(index * 50L) // Stagger the animations
                isVisible = true
            }

            if (userInfo != null) {
//                AnimatedVisibility(
//                    visible = isVisible,
//                    enter = slideInVertically(
//                        initialOffsetY = { it / 2 },
//                        animationSpec = tween(300)
//                    ) + fadeIn(animationSpec = tween(300))
//                ) {
                    FriendListItemWidget(
                        username = username,
                        realName = userInfo.profile?.realName ?: "",
                        profilePic = userInfo.profile?.userAvatar?: "",
                        onViewProfile = onViewProfile,
                        onCompareWith = onCompareWith,
                        onRemoveUser = onRemoveUser,
                        onRefreshUser = onRefreshUser
                    )
//                }
            }
        }
    }
}

@Composable
private fun FriendListItemWidget(
    username: String,
    realName: String,
    profilePic: String,
    onViewProfile: (String) -> Unit = {},
    onCompareWith: (String) -> Unit = {},
    onRemoveUser: (String) -> Unit = {},
    onRefreshUser: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
//            .background(colorResource(R.color.card_elevated).copy(0.5f))
            .clickable { onViewProfile(username) },
        colors = CardDefaults.cardColors(
//            containerColor = colorResource(R.color.card_elevated).copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.sdp)
    ) {
        Row(
            modifier = Modifier
                .background(colorResource(R.color.bg_neutral).copy(alpha = 0.7f))
                .fillMaxWidth()
                .padding(16.sdp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = profilePic,
                placeholder = painterResource(R.drawable.logo),
                contentDescription = "Example image for demonstration purposes",
                modifier = Modifier
//                    .padding(top = 30.sdp, start = 10.sdp)
                    .size(40.sdp)
                    .clip(RoundedCornerShape(8.sdp))
            )
            Column(
                modifier = Modifier.padding(start=16.sdp).weight(1f).background(Color.Transparent),
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
                        style = TextStyleInter14Lh16Fw600(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            val expanded = remember { mutableStateOf(false) }
            Box {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.sdp)
                        .clickable { expanded.value = true }
                        .padding(4.sdp)
                        .background(colorResource(R.color.bg_neutral).copy(alpha = 0.3f)),
                )

                // Dropdown menu
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false },
                    offset = DpOffset(x = (-16).dp, y = 4.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.sdp))
                        .background(colorResource(R.color.bg_neutral))
                        .border(
                            shape = RoundedCornerShape(12.sdp),
                            border = BorderStroke(
                                width = 2.sdp,
                                color = colorResource(R.color.white).copy(alpha = 0.3f)
                            ),
                        )
                        .padding(horizontal = 12.sdp, vertical = 8.sdp)
                ) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.profile_placeholder),
                                tint = Color.White,
                                contentDescription = null,
                                modifier = Modifier.size(22.sdp)
                            )
                        },
                        text = { Text("View Profile", color = Color.White) },
                        onClick = {
                            expanded.value = false
                            onViewProfile(username)
                        }
                    )
                    Divider(
                        color = colorResource(R.color.white).copy(alpha = 0.1f)
                    )
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_compare_outlined),
                                tint = Color.White,
                                contentDescription = null,
                                modifier = Modifier.size(24.sdp)
                            )
                        },
                        text = { Text("Compare with", color = Color.White) },
                        onClick = {
                            expanded.value = false
                            onCompareWith(username)
                        }
                    )
                    Divider(
                        color = colorResource(R.color.white).copy(alpha = 0.1f)
                    )
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete_outline),
                                tint = Color.White,
                                contentDescription = null,
                                modifier = Modifier.size(24.sdp)
                            )
                        },
                        text = { Text("Remove", color = Color.White) },
                        onClick = {
                            onRemoveUser(username)
                            expanded.value = false
                        }
                    )
                    Divider(
                        color = colorResource(R.color.white).copy(alpha = 0.1f)
                    )
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = "Refresh",
                                tint = Color.White,
                                modifier = Modifier.size(24.sdp)
                            )
                        },
                        text = { Text("Refresh", color = Color.White) },
                        onClick = {
                            onRefreshUser(username)
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
}
