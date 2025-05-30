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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw700
import com.devrachit.ken.utility.composeUtility.CompletePreviews
import com.devrachit.ken.utility.composeUtility.sdp
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchWidget(
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    searchText: String = "",
    suggestions: Map<String, LeetCodeUserInfo> = emptyMap(),
    showSuggestions: Boolean = false,
    onSearchTextChange: (String) -> Unit = {},
    onSuggestionClick: (String, LeetCodeUserInfo) -> Unit = { _, _ -> },
    enableNavigation: Boolean = false,
    onNavigateToUserDetails: (String) -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                    keyboardActions = KeyboardActions (
                        onSearch = {
                            // Trigger search when user presses search key
                            if (searchText.isNotBlank() && enableNavigation && suggestions.isNotEmpty()) {
                                val firstSuggestion = suggestions.values.first()
                                onNavigateToUserDetails(firstSuggestion.username ?: searchText)
                            }
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
                        onClick = { onSearchTextChange("") },
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
            if (showSuggestions && suggestions.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.sdp),
                    horizontalAlignment = Alignment.Start
                ) {
                    items(
                        suggestions.size,
                        key = { index -> suggestions.keys.toList()[index] }
                    ) { index ->
                        val key = suggestions.keys.toList()[index]
                        val userInfo = suggestions[key]!!

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {
//                                        onSuggestionClick(key, userInfo)
                                        if (enableNavigation) {
                                            onNavigateToUserDetails(userInfo.username ?: key)
                                        }
                                              },
                                    onLongClick = {
                                        if (enableNavigation) {
                                            onNavigateToUserDetails(userInfo.username ?: key)
                                        }
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
                        }
                    }
                }
            } else if (showSuggestions && suggestions.isEmpty() && searchText.isNotEmpty()) {
                Text(
                    text = "No results found",
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

@CompletePreviews
@Composable
fun SearchWidgetPreview() {
    val sampleSuggestions = listOf(
        "Apple Music",
        "Apple Store",
        "Application Development",
        "Applied Mathematics",
        "App Store Connect",
        "Architecture",
        "Art & Design",
        "Artificial Intelligence"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bg_neutral))
            .padding(16.sdp),
        verticalArrangement = Arrangement.spacedBy(24.sdp)
    ) {
        Text(
            text = "Search Widget Variants",
            style = TextStyleInter12Lh16Fw700(),
            color = colorResource(id = R.color.white),
            modifier = Modifier.padding(bottom = 8.sdp)
        )

        // Empty state
        SearchWidget(
            placeholder = "Search for apps...",
            searchText = "",
            showSuggestions = false
        )

        // With search text and suggestions
        SearchWidget(
            placeholder = "Search for apps...",
            searchText = "App",
            suggestions = sampleSuggestions.associateWith { LeetCodeUserInfo() },
            showSuggestions = true
        )

        // No results state
        SearchWidget(
            placeholder = "Search for apps...",
            searchText = "XYZ",
            suggestions = emptyMap(),
            showSuggestions = true
        )
    }
}
