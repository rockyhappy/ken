package com.devrachit.ken.presentation.screens.dashboard.questions.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.Question
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionsEnhancedSearchWidget(
    modifier: Modifier = Modifier,
    placeholder: String = "Search questions...",
    searchText: String = "",
    searchResults: List<Question> = emptyList(),
    showSuggestions: Boolean = false,
    isSearching: Boolean = false,
    onSearchTextChange: (String) -> Unit = {},
    onQuestionClick: (Question) -> Unit = {},
    onClearSearch: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.sdp),
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
                containerColor = colorResource(id = R.color.card_elevated)
            )
        ) {
            Row(
                modifier = Modifier
                    .background(colorResource(R.color.card_elevated))
                    .fillMaxWidth()
                    .padding(horizontal = 16.sdp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search",
                    tint = colorResource(R.color.grayblue_normal_600),
                    modifier = Modifier.size(20.sdp)
                )

                Spacer(modifier = Modifier.width(12.sdp))

                BasicTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        color = colorResource(id = R.color.white),
                        fontSize = 16.sp
                    ),
                    cursorBrush = SolidColor(colorResource(id = R.color.white)),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = { /* Handle search action if needed */ }
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

                // Clear button - only show when there's text
                if (searchText.isNotEmpty()) {
                    IconButton(
                        onClick = onClearSearch,
                        modifier = Modifier.size(24.sdp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cross),
                            contentDescription = "Clear search",
                            tint = colorResource(R.color.grayblue_normal_600),
                            modifier = Modifier.size(16.sdp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.sdp))

        // Search Results - Expandable Column
        AnimatedVisibility(
            visible = showSuggestions && (searchResults.isNotEmpty() || isSearching),
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
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
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.sdp, 12.sdp, 16.sdp, 8.sdp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSearching) "Searching..." else "Search Results (${searchResults.size})",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    
                    if (isSearching) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.sdp),
                            color = colorResource(R.color.blue_normal_500),
                            strokeWidth = 2.dp
                        )
                    }
                }

                // Results List or Loading state
                if (isSearching) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.sdp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(R.color.blue_normal_500)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 400.sdp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        items(
                            items = searchResults,
                            key = { question -> question.id }
                        ) { question ->
                            QuestionSearchResultItem(
                                question = question,
                                onClick = { onQuestionClick(question) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.sdp))
            }
        }
    }
}

@Composable
private fun QuestionSearchResultItem(
    question: Question,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.sdp, vertical = 12.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Question ID
        Card(
            modifier = Modifier.size(32.sdp),
            shape = RoundedCornerShape(6.sdp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.card_elevated_twice)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = question.id.toString(),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(12.sdp))

        // Question details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = question.title,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question.difficulty,
                    style = TextStyle(
                        color = when (question.difficulty.lowercase()) {
                            "easy" -> colorResource(R.color.easy_filled_blue)
                            "medium" -> colorResource(R.color.medium_filled_yellow)
                            "hard" -> colorResource(R.color.hard_filled_red)
                            else -> Color.Gray
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                )

                if (question.topicTags.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.sdp))
                    Text(
                        text = "• ${question.topicTags.first().name}",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 12.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Arrow icon
        Icon(
            painter = painterResource(id = R.drawable.ic_cross),
            contentDescription = "Go to question",
            tint = colorResource(R.color.grayblue_normal_600),
            modifier = Modifier.size(16.sdp)
        )
    }
}
