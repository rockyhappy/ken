# Integration Guide: Save Question from Question Details Screen

## How to Add "Save Question" Button to Question Details Screen

### Step 1: Update QuestionDetailsSettings.kt

Add a "Save to Sheets" option in the settings menu:

```kotlin
@Composable
fun QuestionDetailsSettings(
    questionSlug: String,
    questionTitle: String,
    onSaveToSheets: (String, String) -> Unit // Add this callback
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }
    
    // ... existing code ...
    
    SettingsMenuItem(
        icon = R.drawable.ic_bookmark, // Use your bookmark icon
        text = "Save to Sheets",
        onClick = {
            onSaveToSheets(questionTitle, questionSlug)
            showMenu = false
        }
    )
}
```

### Step 2: Update QuestionDetailsContent.kt

Pass the callback from the content component:

```kotlin
@Composable
fun QuestionDetailsContent(
    uiState: QuestionDetailsUiState,
    questionSlug: String,
    onBackClick: () -> Unit,
    onSaveToSheets: (String, String) -> Unit // Add callback
) {
    // ... existing code ...
    
    QuestionDetailsSettings(
        questionSlug = questionSlug,
        questionTitle = uiState.questionDetails?.title ?: "",
        onSaveToSheets = onSaveToSheets
    )
}
```

### Step 3: Update QuestionsDetailsScreen.kt

Implement the save logic:

```kotlin
@Composable
fun QuestionsDetailsScreen(
    questionSlug: String,
    onBackClick: () -> Unit = {}
) {
    val viewModel: QuestionDetailViewmodel = hiltViewModel()
    val sheetsViewModel: SheetsScreenViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val folders by sheetsViewModel.folders.collectAsState()
    
    var showSaveDialog by remember { mutableStateOf(false) }
    var selectedFolderId by remember { mutableStateOf<Int?>(null) }
    
    Column {
        QuestionDetailsContent(
            uiState = uiState,
            questionSlug = questionSlug,
            onBackClick = onBackClick,
            onSaveToSheets = { title, slug ->
                if (folders.isEmpty()) {
                    // Show toast: "Please create a folder first"
                    Toast.makeText(context, "Please create a folder first", Toast.LENGTH_SHORT).show()
                } else {
                    showSaveDialog = true
                }
            }
        )
    }
    
    // Save dialog
    if (showSaveDialog && folders.isNotEmpty()) {
        SaveToSheetsDialog(
            folders = folders,
            onSave = { folderId ->
                sheetsViewModel.saveQuestion(
                    questionTitle = uiState.questionDetails?.title ?: "",
                    questionSlug = questionSlug,
                    folderId = folderId,
                    questionDifficulty = uiState.questionDetails?.difficulty ?: "",
                    questionUrl = "https://leetcode.com/problems/$questionSlug/"
                )
                showSaveDialog = false
                Toast.makeText(context, "Question saved!", Toast.LENGTH_SHORT).show()
            },
            onCancel = { showSaveDialog = false }
        )
    }
}

@Composable
fun SaveToSheetsDialog(
    folders: List<QuestionFolder>,
    onSave: (Int) -> Unit,
    onCancel: () -> Unit
) {
    var selectedFolder by remember { mutableStateOf(folders.firstOrNull()) }
    var notes by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Save to Sheets") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Select a folder:")
                LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {
                    items(folders) { folder ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedFolder = folder }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedFolder?.folderId == folder.folderId,
                                onClick = { selectedFolder = folder }
                            )
                            Text(folder.folderName, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Add notes (optional):")
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 100.dp),
                    placeholder = { Text("Your notes...") },
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    selectedFolder?.let { onSave(it.folderId) }
                }
            ) {
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

### Step 4: Update Navigation

Ensure the navigation properly passes callbacks through the composable hierarchy.

## Complete Integration Flow

```
QuestionDetailsScreen
├── QuestionsDetailsScreen
│   ├── QuestionDetailsContent
│   │   ├── QuestionDetailsHeader
│   │   ├── QuestionDetailsSettings
│   │   │   └── "Save to Sheets" MenuItem ← SAVE TRIGGER
│   │   └── Various View Modes (Pager, Simple, WebView)
│   └── SaveToSheetsDialog ← FOLDER SELECTION
└── SheetsScreenViewModel.saveQuestion() ← SAVE TO DB
```

## Error Handling

The ViewModel handles errors via `errorMessage` StateFlow:

```kotlin
val errorMessage by sheetsViewModel.errorMessage.collectAsState()

LaunchedEffect(errorMessage) {
    errorMessage?.let { error ->
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        sheetsViewModel.clearErrorMessage()
    }
}
```

## State Management

The `SheetsScreenViewModel` automatically:
1. Reloads folders when created
2. Reloads questions when folder changes
3. Updates UI reactively via Flow/StateFlow
4. Handles loading and error states

## Tips

- Always validate folder exists before saving
- Show user confirmation when saving
- Handle network issues gracefully
- Consider caching folder list in UI
- Provide feedback for successful saves
