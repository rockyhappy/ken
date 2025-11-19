# Saved Questions Feature - Quick Reference

## Quick Start Code Snippets

### 1. Save a Question from Question Details Screen

```kotlin
// In your ViewModel or Composable
val sheetsViewModel: SheetsScreenViewModel = hiltViewModel()

// Save question
sheetsViewModel.saveQuestion(
    questionTitle = "Two Sum",
    questionSlug = "two-sum",
    folderId = 1,  // ID of the folder
    questionDifficulty = "Easy",
    questionUrl = "https://leetcode.com/problems/two-sum/"
)
```

### 2. Create a New Folder

```kotlin
val sheetsViewModel: SheetsScreenViewModel = hiltViewModel()

// Create folder
sheetsViewModel.createFolder(
    folderName = "Linked List Problems",
    folderDescription = "Problems related to linked lists"
)
```

### 3. Get All Folders

```kotlin
val sheetsViewModel: SheetsScreenViewModel = hiltViewModel()
val folders by sheetsViewModel.folders.collectAsState()

// Use folders in your UI
LazyColumn {
    items(folders) { folder ->
        FolderItem(folder)
    }
}
```

### 4. Get Questions in a Folder

```kotlin
val sheetsViewModel: SheetsScreenViewModel = hiltViewModel()

// Select folder
sheetsViewModel.selectFolder(selectedFolder)

// Questions are automatically loaded
val questions by sheetsViewModel.selectedFolderQuestions.collectAsState()
```

### 5. Mark Question as Solved

```kotlin
sheetsViewModel.markQuestionSolved(
    questionId = 1,
    isSolved = true
)
```

### 6. Update Question Notes

```kotlin
sheetsViewModel.updateQuestionNotes(
    questionId = 1,
    notes = "Used HashMap for O(n) solution"
)
```

### 7. Delete a Question

```kotlin
sheetsViewModel.deleteQuestion(questionId = 1)
```

### 8. Delete a Folder

```kotlin
sheetsViewModel.deleteFolder(folderId = 1)
// All questions in this folder will be deleted automatically
```

### 9. Handle Errors

```kotlin
val errorMessage by sheetsViewModel.errorMessage.collectAsState()

LaunchedEffect(errorMessage) {
    errorMessage?.let { error ->
        // Show error to user
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        sheetsViewModel.clearErrorMessage()
    }
}
```

### 10. Loading State

```kotlin
val isLoading by sheetsViewModel.isLoading.collectAsState()

if (isLoading) {
    CircularProgressIndicator()
} else {
    // Show content
}
```

## Entity/Model Conversions

### SavedQuestionEntity ↔ SavedQuestion

```kotlin
// Entity to Domain
val domainQuestion = entity.toDomainModel()

// Domain to Entity
val entity = SavedQuestionEntity.fromDomainModel(domainQuestion)
```

### QuestionFolderEntity ↔ QuestionFolder

```kotlin
// Entity to Domain
val domainFolder = entity.toDomainModel()

// Domain to Entity
val entity = QuestionFolderEntity.fromDomainModel(domainFolder)
```

## Database Queries (Synchronous)

```kotlin
// If you need to use the repository directly
@Inject
lateinit var repository: SavedQuestionRepository

// Create folder
val folderId = repository.createFolder("My Folder")

// Save question
val questionId = repository.saveQuestion(savedQuestion)

// Get all folders
val folders = repository.getAllFoldersSync()

// Get questions in a folder
val questions = repository.getQuestionsByFolderSync(folderId)

// Search
val results = repository.searchQuestions("Two Sum")

// Delete
repository.deleteQuestion(questionId)
```

## Data Classes

### SavedQuestion
```kotlin
data class SavedQuestion(
    val questionId: Int = 0,
    val questionTitle: String,
    val questionSlug: String,
    val questionDifficulty: String = "",
    val questionUrl: String = "",
    val folderId: Int,
    val savedAt: Long = System.currentTimeMillis(),
    val notes: String = "",
    val isSolved: Boolean = false,
    val solvedAt: Long? = null
)
```

### QuestionFolder
```kotlin
data class QuestionFolder(
    val folderId: Int = 0,
    val folderName: String,
    val folderDescription: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val questionCount: Int = 0
)
```

## ViewModel State Properties

| Property | Type | Description |
|----------|------|-------------|
| `folders` | StateFlow<List<QuestionFolder>> | All user folders |
| `selectedFolder` | StateFlow<QuestionFolder?> | Currently selected folder |
| `selectedFolderQuestions` | StateFlow<List<SavedQuestion>> | Questions in selected folder |
| `isLoading` | StateFlow<Boolean> | Loading indicator |
| `errorMessage` | StateFlow<String?> | Error messages |

## Common Patterns

### Reactive UI Update
```kotlin
@Composable
fun MyScreen() {
    val viewModel: SheetsScreenViewModel = hiltViewModel()
    val questions by viewModel.selectedFolderQuestions.collectAsState()
    
    // UI automatically updates when questions change
    LazyColumn {
        items(questions) { question ->
            QuestionCard(question)
        }
    }
}
```

### Handle Multiple Operations
```kotlin
fun saveAndMarkSolved(question: SavedQuestion, folderId: Int) {
    viewModelScope.launch {
        try {
            // Save
            val id = saveQuestionUseCase(question.copy(folderId = folderId))
            // Mark solved
            markQuestionSolvedUseCase(id.toInt(), isSolved = true)
        } catch (e: Exception) {
            _errorMessage.value = e.message
        }
    }
}
```

### Conditional Saving
```kotlin
fun saveIfNotExists(question: SavedQuestion) {
    viewModelScope.launch {
        val existing = repository.getQuestionBySlug(question.questionSlug)
        if (existing == null) {
            saveQuestionUseCase(question)
        } else {
            _errorMessage.value = "Question already saved"
        }
    }
}
```

## Directory Reference

| Component | Path |
|-----------|------|
| Entities | `data/local/entity/` |
| DAOs | `data/local/dao/` |
| Database | `data/local/databases/` |
| Repository Interface | `domain/repository/local/` |
| Repository Impl | `data/repository/local/` |
| Use Cases | `domain/usecases/savedQuestions/` |
| Domain Models | `domain/models/` |
| ViewModel | `presentation/screens/dashboard/sheets/` |

## Validation Rules (Use Cases)

### CreateFolderUseCase
- ✓ Folder name cannot be empty

### SaveQuestionUseCase
- ✓ Question title cannot be empty
- ✓ Question slug cannot be empty
- ✓ Folder ID must be > 0

### DeleteFolderUseCase / DeleteQuestionUseCase
- ✓ ID must be > 0

### MarkQuestionSolvedUseCase / UpdateQuestionUseCase
- ✓ Question ID must be > 0
- ✓ Question must exist (UpdateQuestionUseCase)

## Performance Tips

1. **Use Flow instead of repeated queries**
   - Subscribe once and update automatically
   
2. **Batch operations**
   - Create folder then save multiple questions
   
3. **Cache folder list in UI**
   - Don't reload on every action
   
4. **Index frequently queried columns**
   - Folder ID is already indexed
   
5. **Use coroutines properly**
   - All database operations in viewModelScope

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Questions not showing | Check if folder was selected and folderId is valid |
| Delete doesn't work | Verify question exists and ID is correct |
| UI not updating | Ensure collecting StateFlow with `.collectAsState()` |
| Crash on save | Verify folder exists before saving question |
| Database error | Check database version is 7+ |

## Testing Checklist

- [ ] Create folder
- [ ] Save question to folder
- [ ] View questions in folder
- [ ] Update question notes
- [ ] Mark question solved
- [ ] Mark question unsolved
- [ ] Delete question
- [ ] Delete folder (cascade)
- [ ] Handle errors gracefully
- [ ] UI updates reactively
