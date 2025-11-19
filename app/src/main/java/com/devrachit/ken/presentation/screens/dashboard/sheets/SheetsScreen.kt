package com.devrachit.ken.presentation.screens.dashboard.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.QuestionFolder
import com.devrachit.ken.domain.models.SavedQuestion
import com.devrachit.ken.ui.theme.TextStyleInter14Lh18Fw400
import com.devrachit.ken.ui.theme.TextStyleInter24Lh36Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun SheetsScreen(
    viewModel: SheetsScreenViewModel = hiltViewModel(),
    onQuestionDetailsClick: (String) -> Unit = {}
) {
    val folders by viewModel.folders.collectAsState()
    val selectedFolder by viewModel.selectedFolder.collectAsState()
    val selectedFolderQuestions by viewModel.selectedFolderQuestions.collectAsState()
    val navigationLevel by viewModel.navigationLevel.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var showCreateFolderDialog by remember { mutableStateOf(false) }
    var showDeleteFolderDialog by remember { mutableStateOf(false) }
    var folderNameInput by remember { mutableStateOf("") }
    var folderDescriptionInput by remember { mutableStateOf("") }
    var folderToDelete by remember { mutableStateOf<QuestionFolder?>(null) }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            android.util.Log.e("SheetsScreen", "Error: $errorMessage")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.bg_neutral))
    ) {
        // Header with navigation
        SheetsScreenHeader(
            navigationLevel = navigationLevel,
            currentFolderName = selectedFolder?.folderName,
            onAddClick = {
                if (navigationLevel == "folders") {
                    showCreateFolderDialog = true
                }
            },
            onBackClick = { viewModel.goBack() }
        )

        // Content based on navigation level
        when (navigationLevel) {
            "folders" -> {
                FoldersListScreen(
                    folders = folders,
                    isLoading = isLoading,
                    onFolderClick = { folder ->
                        viewModel.goToQuestions(folder)
                    },
                    onFolderDelete = {
                        folderToDelete = it
                        showDeleteFolderDialog = true
                    }
                )
            }
            "questions" -> {
                if (selectedFolder != null) {
                    QuestionsListScreen(
                        folder = selectedFolder!!,
                        questions = selectedFolderQuestions,
                        onQuestionClick = { question ->
                            // Navigate to the main question details screen
                            onQuestionDetailsClick(question.questionSlug)
                        },
                        onQuestionDelete = { questionId ->
                            viewModel.deleteQuestion(questionId)
                        }
                    )
                }
            }
        }
    }

    // Create Folder Dialog
    if (showCreateFolderDialog) {
        AlertDialog(
            onDismissRequest = { showCreateFolderDialog = false },
            title = {
                Text(
                    "Create New Folder",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = folderNameInput,
                        onValueChange = { folderNameInput = it },
                        label = { Text("Folder Name", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.material3.LocalTextStyle.current.copy(color = Color.White)
                    )
                    OutlinedTextField(
                        value = folderDescriptionInput,
                        onValueChange = { folderDescriptionInput = it },
                        label = { Text("Description", color = Color.White) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        textStyle = androidx.compose.material3.LocalTextStyle.current.copy(color = Color.White)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (folderNameInput.isNotBlank()) {
                            viewModel.createFolder(folderNameInput, folderDescriptionInput)
                            folderNameInput = ""
                            folderDescriptionInput = ""
                            showCreateFolderDialog = false
                        }
                    }
                ) {
                    Text("Create", color = colorResource(R.color.primary_color))
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateFolderDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = colorResource(R.color.bg_neutral).copy(alpha = 0.95f)
        )
    }

    // Delete Folder Dialog
    if (showDeleteFolderDialog && folderToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteFolderDialog = false },
            title = {
                Text(
                    "Delete Folder?",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${folderToDelete!!.folderName}\"?\nAll questions in this folder will be deleted.",
                    color = Color.White.copy(alpha = 0.8f)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        folderToDelete?.let { viewModel.deleteFolder(it.folderId) }
                        showDeleteFolderDialog = false
                        folderToDelete = null
                    }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteFolderDialog = false
                    folderToDelete = null
                }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            containerColor = colorResource(R.color.bg_neutral).copy(alpha = 0.95f)
        )
    }
}

@Composable
private fun SheetsScreenHeader(
    navigationLevel: String,
    currentFolderName: String?,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.sdp)
            .background(color = colorResource(R.color.bg_neutral)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (navigationLevel != "folders") {
                Button(
                    onClick = onBackClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.primary_color)
                    ),
                    modifier = Modifier.size(40.sdp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.sdp)
                    )
                }
            }

            Text(
                text = when (navigationLevel) {
                    "folders" -> "My Sheets"
                    "questions" -> currentFolderName ?: "Questions"
                    else -> "My Sheets"
                },
                style = TextStyleInter24Lh36Fw700(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
        }

        if (navigationLevel == "folders") {
            Button(
                onClick = onAddClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.primary_color)
                ),
                modifier = Modifier.size(40.sdp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Folder",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun FoldersListScreen(
    folders: List<QuestionFolder>,
    isLoading: Boolean,
    onFolderClick: (QuestionFolder) -> Unit,
    onFolderDelete: (QuestionFolder) -> Unit
) {
    if (isLoading && folders.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
             ,
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = colorResource(R.color.primary_color))
        }
    } else if (folders.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
             ,
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_sheets_outlined),
                    contentDescription = "No Folders",
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(80.sdp)
                )
                Spacer(modifier = Modifier.size(16.sdp))
                Text(
                    text = "No Folders Yet",
                    style = TextStyleInter24Lh36Fw700(),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(8.sdp))
                Text(
                    text = "Create a folder to save your questions",
                    style = TextStyleInter14Lh18Fw400(),
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.sdp, vertical = 8.sdp),
            verticalArrangement = Arrangement.spacedBy(8.sdp)
        ) {
            items(folders) { folder ->
                FolderItem(
                    folder = folder,
                    onClick = { onFolderClick(folder) },
                    onDeleteClick = { onFolderDelete(folder) }
                )
            }
        }
    }
}

@Composable
private fun QuestionsListScreen(
    folder: QuestionFolder,
    questions: List<SavedQuestion>,
    onQuestionClick: (SavedQuestion) -> Unit,
    onQuestionDelete: (Int) -> Unit
) {
    if (questions.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                ,
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_sheets_outlined),
                    contentDescription = "No Questions",
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(80.sdp)
                )
                Spacer(modifier = Modifier.size(16.sdp))
                Text(
                    text = "No Questions Yet",
                    style = TextStyleInter24Lh36Fw700(),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(8.sdp))
                Text(
                    text = "No questions saved in this folder",
                    style = TextStyleInter14Lh18Fw400(),
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.sdp, vertical = 8.sdp),
            verticalArrangement = Arrangement.spacedBy(8.sdp)
        ) {
            items(questions) { question ->
                QuestionItemCard(
                    question = question,
                    onClick = { onQuestionClick(question) },
                    onDeleteClick = { onQuestionDelete(question.questionId) }
                )
            }
        }
    }
}

@Composable
fun FolderItem(
    folder: QuestionFolder,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Gray.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .padding(12.sdp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Folder",
                tint = colorResource(R.color.primary_color),
                modifier = Modifier.size(24.sdp)
            )
            Spacer(modifier = Modifier.width(12.sdp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = folder.folderName,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    text = "${folder.questionCount} questions",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            tint = Color.Red.copy(alpha = 0.7f),
            modifier = Modifier
                .size(20.sdp)
                .clickable(enabled = true) {
                    onDeleteClick()
                }
        )
    }
}

@Composable
fun QuestionItemCard(
    question: SavedQuestion,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Gray.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(12.sdp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = question.questionTitle,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.size(4.sdp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.sdp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = question.questionDifficulty,
                        color = getDifficultyColor(question.questionDifficulty),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (question.isSolved) {
                        Text(
                            text = "✓ Solved",
                            color = Color.Green,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color.Red.copy(alpha = 0.7f),
                modifier = Modifier
                    .size(20.sdp)
                    .clickable(enabled = true) {
                        onDeleteClick()
                    }
            )
        }
    }
}

@Composable
fun getDifficultyColor(difficulty: String): Color {
    return when (difficulty.lowercase()) {
        "easy" -> Color.Green.copy(alpha = 0.8f)
        "medium" -> Color.Yellow.copy(alpha = 0.8f)
        "hard" -> Color.Red.copy(alpha = 0.8f)
        else -> Color.White.copy(alpha = 0.7f)
    }
}
