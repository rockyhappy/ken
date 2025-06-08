package com.devrachit.ken.presentation.screens.dashboard.Widgets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.utility.composeUtility.sdp
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EnhancedSearchWidget(
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    searchText: String = "",
    localResults: Map<String, LeetCodeUserInfo> = emptyMap(),
    showSuggestions: Boolean = false,
    platformResult: LeetCodeUserInfo? = null,
    platformError: String? = null,
    isPlatformSearching: Boolean = false,
    showPlatformResult: Boolean = false,
    onSearchTextChange: (String) -> Unit = {},
    onLocalResultClick: (String, LeetCodeUserInfo) -> Unit = { _, _ -> },
    onPlatformSearch: () -> Unit = {},
    onNavigateToUserDetails: (String) -> Unit = {},
    onHidePlatformResult: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.sdp, end = 16.sdp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Search Input Card
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(14.sdp))
                .background(colorResource(R.color.card_elevated))
                .border(
                    border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(14.sdp)
                )
                .fillMaxWidth()
                .height(56.sdp)
                .shadow(8.sdp, RoundedCornerShape(14.sdp)),
            shape = RoundedCornerShape(14.sdp),
            elevation = CardDefaults.cardElevation(8.sdp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.white)
            )
        ) {
            Row(
                modifier = Modifier
                    .background(colorResource(R.color.card_elevated))
            ) {
                Button(
                    onClick = { /* Handle search button click */ },
                    modifier = Modifier
                        .width(56.sdp)
                        .fillMaxHeight()
                        .background(colorResource(R.color.card_elevated))
                        .border(
                            border = BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(14.sdp)
                        ),
                    shape = RoundedCornerShape(14.sdp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.card_elevated_twice),
                    ),
                    contentPadding = PaddingValues(0.dp),
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            tint = Color.White,
                            contentDescription = null,
                            modifier = Modifier.size(24.sdp)
                        )
                    }
                )

                BasicTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .background(Color.Transparent)
                        .padding(horizontal = 16.sdp)
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        color = colorResource(id = R.color.white),
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(colorResource(id = R.color.white)),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            onPlatformSearch()
                        }
                    ),
                    decorationBox = { innerTextField ->
                        if (searchText.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = TextStyle(
                                    color = colorResource(id = R.color.grayblue_normal_600),
                                    fontSize = 16.sp
                                )
                            )
                        }
                        innerTextField()
                    }
                )

                // Cross icon that appears only when there's text
                if (searchText.isNotEmpty()) {
                    IconButton(
                        onClick = { 
                            onSearchTextChange("")
                            onHidePlatformResult()
                        },
                        modifier = Modifier
                            .size(48.sdp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cross),
                            contentDescription = "Clear text",
                            tint = Color.White,
                            modifier = Modifier.size(20.sdp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.sdp))

        // Results Container - Only show when there's content to display
        if (showSuggestions || showPlatformResult || searchText.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.sdp, RoundedCornerShape(8.sdp))
                    .background(
                        color = colorResource(id = R.color.card_elevated),
                        shape = RoundedCornerShape(8.sdp)
                    )
                    .animateContentSize()
            ) {
                // Local Results Section (Added Friends)
                if (showSuggestions && localResults.isNotEmpty()) {
                    Text(
                        text = "Added Friends",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(16.sdp, 12.sdp, 16.sdp, 8.sdp)
                    )
                    
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.sdp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        items(
                            localResults.size,
                            key = { index -> "local_${localResults.keys.toList()[index]}" }
                        ) { index ->
                            val key = localResults.keys.toList()[index]
                            val userInfo = localResults[key]!!
                            val displayName = userInfo.profile?.realName?.takeIf { it.isNotBlank() }
                                ?: userInfo.username
                                ?: "Unknown User"

                            if (displayName == "Unknown User") {
                                // Show "no user found" message for unknown users
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.sdp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_cross),
                                        contentDescription = null,
                                        tint = Color.Red,
                                        modifier = Modifier.size(20.sdp)
                                    )
                                    Spacer(modifier = Modifier.width(8.sdp))
                                    Text(
                                        text = "No user found",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 14.sp
                                    )
                                }
                            } else {
                                UserResultItem(
                                    userInfo = userInfo,
                                    onClick = { onLocalResultClick(key, userInfo) },
                                    onNavigate = {
                                        onNavigateToUserDetails(userInfo.username ?: key)
                                        onSearchTextChange("")
                                        onHidePlatformResult()
                                    },
                                    showAddButton = false,
                                    isClickable = true
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.sdp))
                }
                
                // Search Platform Button (only show when there's search text)
                if (searchText.isNotEmpty()) {
                    Button(
                        onClick = onPlatformSearch,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.sdp, 8.sdp),
                        enabled = !isPlatformSearching,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.card_elevated_twice),
                            disabledContainerColor = colorResource(id = R.color.card_elevated_twice).copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(8.sdp)
                    ) {
                        if (isPlatformSearching) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.sdp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.sdp))
                            Text(
                                text = "Searching...",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.sdp)
                            )
                            Spacer(modifier = Modifier.width(8.sdp))
                            Text(
                                text = "Search the Platform",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                
                // Platform Search Result
                if (showPlatformResult) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.sdp, 8.sdp)
                    ) {
                        Text(
                            text = "Platform Search Result",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(bottom = 8.sdp)
                        )

                        when {
                            platformResult != null -> {
                                val displayName =
                                    platformResult.profile?.realName?.takeIf { it.isNotBlank() }
                                        ?: platformResult.username
                                        ?: "Unknown User"

                                if (displayName == "Unknown User") {
                                    // Show "no user found" message for unknown users
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = colorResource(id = R.color.card_elevated_twice).copy(
                                                    alpha = 0.3f
                                                ),
                                                shape = RoundedCornerShape(8.sdp)
                                            )
                                            .padding(12.sdp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_cross),
                                            contentDescription = null,
                                            tint = Color.Red,
                                            modifier = Modifier.size(20.sdp)
                                        )
                                        Spacer(modifier = Modifier.width(8.sdp))
                                        Text(
                                            text = "No user found",
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontSize = 14.sp
                                        )
                                    }
                                } else {
                                    UserResultItem(
                                        userInfo = platformResult,
                                        onClick = { },
                                        onNavigate = {
                                            onNavigateToUserDetails(
                                                platformResult.username ?: searchText
                                            )
                                            onSearchTextChange("")
                                            onHidePlatformResult()
                                        },
                                        showAddButton = true,
                                        onAddClick = {
                                            onNavigateToUserDetails(
                                                platformResult.username ?: searchText
                                            )
                                            onSearchTextChange("")
                                            onHidePlatformResult()
                                        },
                                        isClickable = true
                                    )
                                }
                            }
                            platformError != null -> {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = colorResource(id = R.color.card_elevated_twice).copy(
                                                alpha = 0.3f
                                            ),
                                            shape = RoundedCornerShape(8.sdp)
                                        )
                                        .padding(12.sdp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_cross),
                                        contentDescription = null,
                                        tint = Color.Red,
                                        modifier = Modifier.size(20.sdp)
                                    )
                                    Spacer(modifier = Modifier.width(8.sdp))
                                    Text(
                                        text = platformError,
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
                
                // No local results message
                if (showSuggestions && localResults.isEmpty() && searchText.isNotEmpty() && !showPlatformResult) {
                    Text(
                        text = "No friends found matching your search",
                        style = TextStyle(
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.padding(16.sdp)
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun UserResultItem(
    userInfo: LeetCodeUserInfo,
    onClick: () -> Unit,
    onNavigate: () -> Unit,
    showAddButton: Boolean = false,
    onAddClick: () -> Unit = {},
    isClickable: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isClickable) {
                    Modifier.combinedClickable(
                        onClick = onNavigate,
                        onLongClick = onNavigate
                    )
                } else {
                    Modifier
                }
            )
            .padding(12.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = userInfo.profile?.userAvatar,
            placeholder = painterResource(R.drawable.profile_placeholder),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(40.sdp)
                .clip(RoundedCornerShape(8.sdp))
        )
        
        Column(
            modifier = Modifier
                .padding(start = 12.sdp)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            // Primary text (Real name or username)
            Text(
                text = userInfo.profile?.realName?.takeIf { it.isNotBlank() }
                    ?: userInfo.username
                    ?: "Unknown User",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            )

            // Secondary text (Username if real name exists, or company)
            val secondaryText = when {
                userInfo.profile?.realName?.isNotBlank() == true && userInfo.username?.isNotBlank() == true ->
                    "@${userInfo.username}"

                userInfo.profile?.company?.isNotBlank() == true ->
                    userInfo.profile.company

                else -> null
            }

            secondaryText?.let { text ->
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.padding(top = 2.sdp)
                )
            }
        }

        if (showAddButton) {
            IconButton(
                onClick = onAddClick,
                modifier = Modifier.size(24.sdp)
            ) {
                Text(
                    text = "+",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
