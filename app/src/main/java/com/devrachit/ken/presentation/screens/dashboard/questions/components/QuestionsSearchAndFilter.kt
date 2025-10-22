package com.devrachit.ken.presentation.screens.dashboard.questions.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.devrachit.ken.presentation.screens.dashboard.questions.QuestionFilters
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw700
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionsSearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    searchSuggestions: List<Question>,
    showSuggestions: Boolean,
    onSuggestionClick: (Question) -> Unit,
    onClearSearch: () -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.sdp),
            elevation = CardDefaults.cardElevation(4.sdp),
            shape = RoundedCornerShape(12.sdp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.card_elevated)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.sdp),
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
                        onSearch = { onSearchFocusChanged(false) }
                    ),
                    decorationBox = { innerTextField ->
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Search questions...",
                                style = TextStyle(
                                    color = colorResource(id = R.color.grayblue_normal_600),
                                    fontSize = 16.sp
                                )
                            )
                        }
                        innerTextField()
                    }
                )

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

        // Search suggestions dropdown
        AnimatedVisibility(
            visible = showSuggestions && searchSuggestions.isNotEmpty(),
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.sdp),
                elevation = CardDefaults.cardElevation(8.sdp),
                shape = RoundedCornerShape(bottomStart = 12.sdp, bottomEnd = 12.sdp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.card_elevated)
                )
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.sdp)
                ) {
                    items(searchSuggestions) { question ->
                        SearchSuggestionItem(
                            question = question,
                            onClick = { onSuggestionClick(question) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionsFilters(
    filters: QuestionFilters,
    selectedDifficulty: String?,
    selectedStatus: String?,
    selectedTags: List<String>,
    onDifficultySelected: (String?) -> Unit,
    onStatusSelected: (String?) -> Unit,
    onTagToggled: (String) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.sdp),
        elevation = CardDefaults.cardElevation(4.sdp),
        shape = RoundedCornerShape(12.sdp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.card_elevated)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.sdp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filters",
                    style = TextStyleInter16Lh24Fw700(),
                    color = colorResource(R.color.white),
                    fontWeight = FontWeight.Bold
                )

                TextButton(
                    onClick = onClearFilters,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = colorResource(R.color.blue_normal_500)
                    )
                ) {
                    Text("Clear All", style = TextStyleInter12Lh16Fw700())
                }
            }

            Spacer(modifier = Modifier.height(12.sdp))

            // Difficulty filter
            FilterSection(
                title = "Difficulty",
                options = filters.difficulties,
                selectedOption = selectedDifficulty,
                onOptionSelected = onDifficultySelected
            )

            Spacer(modifier = Modifier.height(16.sdp))

            // Status filter
            FilterSection(
                title = "Status",
                options = filters.statuses,
                selectedOption = selectedStatus,
                onOptionSelected = onStatusSelected
            )

            // Topic tags filter (show only if there are tags)
            if (filters.topicTags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.sdp))

                Text(
                    text = "Topic Tags",
                    style = TextStyleInter14Lh20Fw400(),
                    color = colorResource(R.color.white),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.sdp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.sdp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filters.topicTags.take(10)) { tag ->
                        FilterChip(
                            label = tag,
                            selected = selectedTags.contains(tag),
                            onClick = { onTagToggled(tag) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit
) {
    Text(
        text = title,
        style = TextStyleInter14Lh20Fw400(),
        color = colorResource(R.color.white),
        fontWeight = FontWeight.Medium
    )

    Spacer(modifier = Modifier.height(8.sdp))

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        items(options) { option ->
            FilterChip(
                label = option,
                selected = selectedOption == option,
                onClick = {
                    onOptionSelected(if (selectedOption == option) null else option)
                }
            )
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) {
        colorResource(R.color.blue_normal_500)
    } else {
        colorResource(R.color.card_elevated_twice)
    }

    val textColor = if (selected) {
        Color.White
    } else {
        colorResource(R.color.grayblue_normal_600)
    }

    Text(
        text = label,
        style = TextStyleInter12Lh16Fw700(),
        color = textColor,
        modifier = Modifier
            .clickable { onClick() }
            .background(backgroundColor, RoundedCornerShape(16.sdp))
            .border(
                width = 1.dp,
                color = if (selected) colorResource(R.color.blue_normal_500) else colorResource(R.color.grayblue_normal_600).copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.sdp)
            )
            .padding(horizontal = 12.sdp, vertical = 6.sdp)
    )
}

@Composable
private fun SearchSuggestionItem(
    question: Question,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${question.frontendQuestionId}. ${question.title}",
                style = TextStyleInter14Lh20Fw400(),
                color = colorResource(R.color.white),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (question.topicTags.isNotEmpty()) {
                Text(
                    text = question.topicTags.take(3).joinToString(", "),
                    style = TextStyleInter12Lh16Fw700(),
                    color = colorResource(R.color.grayblue_normal_600),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Text(
            text = question.difficulty,
            style = TextStyleInter12Lh16Fw700(),
            color = when (question.difficulty.lowercase()) {
                "easy" -> colorResource(R.color.green_normal_500)
                "medium" -> colorResource(R.color.yellow_normal_500)
                "hard" -> colorResource(R.color.red_normal_500)
                else -> colorResource(R.color.grayblue_normal_600)
            }
        )
    }
}
