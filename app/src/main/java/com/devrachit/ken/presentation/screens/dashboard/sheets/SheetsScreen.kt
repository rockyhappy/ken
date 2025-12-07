package com.devrachit.ken.presentation.screens.dashboard.sheets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.devrachit.ken.R
import com.devrachit.ken.data.local.entity.SheetQuestionCrossRef
import com.devrachit.ken.data.local.entity.SheetWithQuestions
import com.devrachit.ken.presentation.screens.dashboard.sheets.components.DeleteSheetDialog
import com.devrachit.ken.ui.theme.*
import com.devrachit.ken.utility.composeUtility.sdp

const val FAVORITES_SHEET_NAME = "Favorites"

@Composable
fun SheetsScreen(
    viewModel: SheetsViewModel = hiltViewModel(),
    onQuestionClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.bg_neutral))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Add Sheet Section
            AddSheetSection(
                newSheetName = uiState.newSheetName,
                isCreating = uiState.isCreating,
                onNameChange = viewModel::updateNewSheetName,
                onAddClick = { viewModel.createSheet(uiState.newSheetName) }
            )

            // Sheets List or Empty State
            if (uiState.sheets.isEmpty() && !uiState.isLoading) {
                EmptySheetState(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            } else {
                SheetsList(
                    sheets = uiState.sheets,
                    onSheetClick = viewModel::selectSheet,
                    onDeleteClick = viewModel::deleteSheet,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            }
        }

        // Sheet Detail Overlay
        AnimatedVisibility(
            visible = uiState.isSheetDetailVisible && uiState.selectedSheet != null,
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            uiState.selectedSheet?.let { sheet ->
                SheetDetailScreen(
                    sheetWithQuestions = sheet,
                    onBackClick = viewModel::closeSheetDetail,
                    onQuestionClick = onQuestionClick,
                    onRemoveQuestion = { questionSlug ->
                        viewModel.removeQuestionFromSheet(sheet.sheet.id, questionSlug)
                    }
                )
            }
        }

        // Error Snackbar
        uiState.error?.let { error ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.sdp),
                action = {
                    TextButton(onClick = viewModel::clearError) {
                        Text("Dismiss", color = Color.White)
                    }
                }
            ) {
                Text(error)
            }
        }
    }
}

@Composable
private fun AddSheetSection(
    newSheetName: String,
    isCreating: Boolean,
    onNameChange: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.sdp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.sdp)
    ) {
        OutlinedTextField(
            value = newSheetName,
            onValueChange = onNameChange,
            placeholder = {
                Text(
                    "New Sheet Name",
                    color = Color.White.copy(alpha = 0.5f),
                    style = TextStyleInter14Lh20Fw400()
                )
            },
            modifier = Modifier.weight(1f),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = colorResource(R.color.blue_normal_500),
                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                cursorColor = colorResource(R.color.blue_normal_500)
            ),
            shape = RoundedCornerShape(12.sdp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { if (newSheetName.isNotBlank()) onAddClick() })
        )

        Button(
            onClick = onAddClick,
            enabled = newSheetName.isNotBlank() && !isCreating,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.blue_normal_500),
                disabledContainerColor = colorResource(R.color.blue_normal_500).copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(12.sdp),
            modifier = Modifier.height(56.sdp)
        ) {
            if (isCreating) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.sdp),
                    color = Color.White,
                    strokeWidth = 2.sdp
                )
            } else {
                Text("Add", style = TextStyleInter14Lh20Fw400(), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun EmptySheetState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_sheets_outlined),
            contentDescription = "No Sheets",
            tint = Color.White.copy(alpha = 0.5f),
            modifier = Modifier.size(80.sdp)
        )

        Spacer(modifier = Modifier.height(16.sdp))

        Text(
            text = "No Sheets",
            style = TextStyleInter24Lh36Fw700(),
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.sdp))

        Text(
            text = "Create a sheet to organize\nyour questions",
            style = TextStyleInter14Lh18Fw400(),
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SheetsList(
    sheets: List<SheetWithQuestions>,
    onSheetClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.sdp, vertical = 8.sdp),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        items(sheets, key = { it.sheet.id }) { sheetWithQuestions ->
            SheetItem(
                sheetWithQuestions = sheetWithQuestions,
                onClick = { onSheetClick(sheetWithQuestions.sheet.id) },
                onDeleteClick = { onDeleteClick(sheetWithQuestions.sheet.id) }
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(100.sdp))
        }
    }
}

@Composable
private fun SheetItem(
    sheetWithQuestions: SheetWithQuestions,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val isFavorites = sheetWithQuestions.sheet.name == FAVORITES_SHEET_NAME

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.sdp))
            .background(colorResource(R.color.card_elevated))
            .clickable(onClick = onClick)
            .padding(16.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Favorites icon
        if (isFavorites) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorites",
                tint = colorResource(R.color.hard_filled_red),
                modifier = Modifier.size(24.sdp)
            )
            Spacer(modifier = Modifier.width(8.sdp))
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = sheetWithQuestions.sheet.name,
                style = TextStyleInter16Lh24Fw500(),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${sheetWithQuestions.questions.size} questions",
                style = TextStyleInter12Lh16Fw400(),
                color = Color.White.copy(alpha = 0.6f)
            )
        }

        // Only show delete button if not Favorites
        if (!isFavorites) {
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White.copy(alpha = 0.6f)
                )
            }
        }

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Open",
            tint = Color.White.copy(alpha = 0.6f)
        )
    }

    DeleteSheetDialog(
        showDialog = showDeleteDialog,
        sheetName = sheetWithQuestions.sheet.name,
        onDismissRequest = { showDeleteDialog = false },
        onConfirmDelete = {
            onDeleteClick()
            showDeleteDialog = false
        }
    )
}

@Composable
private fun SheetDetailScreen(
    sheetWithQuestions: SheetWithQuestions,
    onBackClick: () -> Unit,
    onQuestionClick: (String) -> Unit,
    onRemoveQuestion: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bg_neutral))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sheetWithQuestions.sheet.name,
                    style = TextStyleInter16Lh24Fw700(),
                    color = Color.White
                )
                Text(
                    text = "${sheetWithQuestions.questions.size} questions",
                    style = TextStyleInter12Lh16Fw400(),
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
        }

        // Questions List
        if (sheetWithQuestions.questions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No questions in this sheet yet.\nAdd questions from the question details page.",
                    style = TextStyleInter14Lh18Fw400(),
                    color = Color.White.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.sdp, vertical = 8.sdp),
                verticalArrangement = Arrangement.spacedBy(8.sdp)
            ) {
                items(sheetWithQuestions.questions, key = { it.questionSlug }) { question ->
                    QuestionInSheetItem(
                        question = question,
                        onClick = { onQuestionClick(question.questionSlug) },
                        onRemoveClick = { onRemoveQuestion(question.questionSlug) }
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(100.sdp))
                }
            }
        }
    }
}

@Composable
private fun QuestionInSheetItem(
    question: SheetQuestionCrossRef,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    val difficultyColor = when (question.questionDifficulty.lowercase()) {
        "easy" -> colorResource(R.color.easy_filled_blue)
        "medium" -> colorResource(R.color.medium_filled_yellow)
        "hard" -> colorResource(R.color.hard_filled_red)
        else -> Color.White.copy(alpha = 0.6f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.sdp))
            .background(colorResource(R.color.card_elevated))
            .clickable(onClick = onClick)
            .padding(16.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.sdp)
            ) {
                Text(
                    text = "#${question.questionFrontendId}",
                    style = TextStyleInter12Lh16Fw400(),
                    color = colorResource(R.color.blue_normal_500),
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.sdp))
                        .background(difficultyColor.copy(alpha = 0.2f))
                        .padding(horizontal = 6.sdp, vertical = 2.sdp)
                ) {
                    Text(
                        text = question.questionDifficulty,
                        style = TextStyleInter10Lh12Fw400(),
                        color = difficultyColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.sdp))
            
            Text(
                text = question.questionTitle,
                style = TextStyleInter14Lh20Fw400(),
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        IconButton(onClick = onRemoveClick) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove",
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(20.sdp)
            )
        }
    }
}
