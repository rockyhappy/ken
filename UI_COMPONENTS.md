# Saved Questions Feature - UI Component Examples

## Complete Example: Sheets Screen with Save Dialog

```kotlin
@Composable
fun SheetsScreenComplete(
    viewModel: SheetsScreenViewModel = hiltViewModel()
) {
    val folders by viewModel.folders.collectAsState()
    val selectedFolder by viewModel.selectedFolder.collectAsState()
    val selectedFolderQuestions by viewModel.selectedFolderQuestions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var showCreateFolderDialog by remember { mutableStateOf(false) }
    var showDeleteFolderDialog by remember { mutableStateOf(false) }
    var folderNameInput by remember { mutableStateOf("") }
    var folderDescriptionInput by remember { mutableStateOf("") }
    var folderToDelete by remember { mutableStateOf<QuestionFolder?>(null) }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            viewModel.clearErrorMessage()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.bg_neutral))
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "My Sheets",
            style = TextStyleInter24Lh36Fw700(),
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Folders Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Folders (${folders.size})",
                style = TextStyleInter14Lh18Fw400(),
                color = Color.White
            )
            Button(
                onClick = { showCreateFolderDialog = true },
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Create folder",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Folder List
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else if (folders.isEmpty()) {
            EmptyStateView(
                icon = R.drawable.ic_folder,
                title = "No Folders",
                message = "Create a folder to start saving questions",
                actionText = "Create Folder",
                onAction = { showCreateFolderDialog = true }
            )
        } else {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(folders) { folder ->
                    FolderCard(
                        folder = folder,
                        isSelected = selectedFolder?.folderId == folder.folderId,
                        questionsCount = folder.questionCount,
                        onSelect = { viewModel.selectFolder(folder) },
                        onDelete = {
                            folderToDelete = folder
                            showDeleteFolderDialog = true
                        }
                    )
                }
            }
        }

        // Questions Section
        if (selectedFolder != null) {
            Text(
                text = "Questions in \"${selectedFolder?.folderName}\"",
                style = TextStyleInter14Lh18Fw400(),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            if (selectedFolderQuestions.isEmpty()) {
                Text(
                    text = "No questions saved yet",
                    style = TextStyleInter12Lh16Fw400(),
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedFolderQuestions) { question ->
                        QuestionItemCard(
                            question = question,
                            onMarkSolved = { 
                                viewModel.markQuestionSolved(
                                    question.questionId,
                                    !question.isSolved
                                )
                            },
                            onDelete = { 
                                viewModel.deleteQuestion(question.questionId) 
                            },
                            onNotesUpdate = { notes ->
                                viewModel.updateQuestionNotes(
                                    question.questionId,
                                    notes
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    // Create Folder Dialog
    if (showCreateFolderDialog) {
        CreateFolderDialog(
            onConfirm = { name, description ->
                viewModel.createFolder(name, description)
                folderNameInput = ""
                folderDescriptionInput = ""
                showCreateFolderDialog = false
            },
            onCancel = {
                showCreateFolderDialog = false
                folderNameInput = ""
                folderDescriptionInput = ""
            }
        )
    }

    // Delete Folder Confirmation
    if (showDeleteFolderDialog && folderToDelete != null) {
        ConfirmDeleteDialog(
            title = "Delete Folder",
            message = "Are you sure you want to delete \"${folderToDelete?.folderName}\"? All questions in this folder will be deleted.",
            onConfirm = {
                viewModel.deleteFolder(folderToDelete!!.folderId)
                showDeleteFolderDialog = false
                folderToDelete = null
            },
            onCancel = {
                showDeleteFolderDialog = false
                folderToDelete = null
            }
        )
    }
}
```

## Component 1: Folder Card

```kotlin
@Composable
fun FolderCard(
    folder: QuestionFolder,
    isSelected: Boolean,
    questionsCount: Int,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onSelect)
            .border(
                width = 2.dp,
                color = if (isSelected) colorResource(R.color.primary) else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.card_elevated)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_folder),
                contentDescription = "Folder",
                tint = colorResource(R.color.primary),
                modifier = Modifier.size(32.dp)
            )
            
            Text(
                text = folder.folderName,
                style = TextStyleInter12Lh16Fw400(),
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Text(
                text = "$questionsCount questions",
                style = TextStyleInter10Lh14Fw400(),
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )

            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .align(Alignment.End)
                    .size(24.dp)
                    .padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red.copy(alpha = 0.7f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
```

## Component 2: Question Item Card

```kotlin
@Composable
fun QuestionItemCard(
    question: SavedQuestion,
    onMarkSolved: () -> Unit,
    onDelete: () -> Unit,
    onNotesUpdate: (String) -> Unit
) {
    var showNotesDialog by remember { mutableStateOf(false) }
    var notes by remember { mutableStateOf(question.notes) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.card_elevated).copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Checkbox for solved status
                Checkbox(
                    checked = question.isSolved,
                    onCheckedChange = { onMarkSolved() },
                    modifier = Modifier.size(20.dp)
                )

                // Question title
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = question.questionTitle,
                        style = TextStyleInter14Lh20Fw400(),
                        color = Color.White,
                        textDecoration = if (question.isSolved) 
                            TextDecoration.LineThrough else TextDecoration.None
                    )
                    
                    Text(
                        text = "${question.questionDifficulty} • ${question.questionSlug}",
                        style = TextStyleInter12Lh16Fw400(),
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Action buttons
                IconButton(onClick = { showNotesDialog = true }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_notes),
                        contentDescription = "Notes",
                        tint = colorResource(R.color.primary),
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Notes preview
            if (question.notes.isNotEmpty()) {
                Text(
                    text = "Note: ${question.notes}",
                    style = TextStyleInter10Lh14Fw400(),
                    color = Color.Yellow.copy(alpha = 0.8f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    // Notes dialog
    if (showNotesDialog) {
        AlertDialog(
            onDismissRequest = { showNotesDialog = false },
            title = { Text("Update Notes") },
            text = {
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    placeholder = { Text("Add your notes...") },
                    maxLines = 5
                )
            },
            confirmButton = {
                Button(onClick = {
                    onNotesUpdate(notes)
                    showNotesDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNotesDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
```

## Component 3: Create Folder Dialog

```kotlin
@Composable
fun CreateFolderDialog(
    onConfirm: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var folderName by remember { mutableStateOf("") }
    var folderDescription by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { 
            Text(
                "Create New Folder",
                style = TextStyleInter16Lh24Fw700()
            ) 
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = folderName,
                    onValueChange = { folderName = it },
                    label = { Text("Folder Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                OutlinedTextField(
                    value = folderDescription,
                    onValueChange = { folderDescription = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (folderName.isNotBlank()) {
                        onConfirm(folderName, folderDescription)
                    }
                },
                enabled = folderName.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}
```

## Component 4: Save to Sheets Dialog (for Question Details)

```kotlin
@Composable
fun SaveToSheetsDialog(
    folders: List<QuestionFolder>,
    onSave: (Int, String) -> Unit,
    onCancel: () -> Unit
) {
    var selectedFolderId by remember { mutableStateOf(folders.firstOrNull()?.folderId ?: 0) }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Save to Sheets") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Select a folder:", style = TextStyleInter12Lh16Fw400())
                
                LazyColumn(
                    modifier = Modifier
                        .heightIn(max = 150.dp)
                        .padding(vertical = 12.dp)
                ) {
                    items(folders) { folder ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedFolderId = folder.folderId }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedFolderId == folder.folderId,
                                onClick = { selectedFolderId = folder.folderId }
                            )
                            Column(modifier = Modifier.padding(start = 12.dp)) {
                                Text(folder.folderName, color = Color.White)
                                Text(
                                    folder.folderDescription,
                                    color = Color.White.copy(alpha = 0.7f),
                                    style = TextStyleInter10Lh14Fw400()
                                )
                            }
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                Text("Add notes (optional):", style = TextStyleInter12Lh16Fw400())
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp),
                    placeholder = { Text("Your notes about this question...") },
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(selectedFolderId, notes) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}
```

## Component 5: Empty State View

```kotlin
@Composable
fun EmptyStateView(
    @DrawableRes icon: Int,
    title: String,
    message: String,
    actionText: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = title,
            tint = Color.White.copy(alpha = 0.5f),
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = title,
            style = TextStyleInter16Lh24Fw700(),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = message,
            style = TextStyleInter12Lh16Fw400(),
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (actionText != null && onAction != null) {
            Button(onClick = onAction) {
                Text(actionText)
            }
        }
    }
}
```

These components are production-ready and follow the design patterns used in your Ken app!
