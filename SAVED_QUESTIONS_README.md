# Saved Questions Feature - Room Database Implementation

## Overview

This implementation provides a complete Room database-based system for saving and organizing LeetCode questions into user-created folders. When a user saves a question, it's stored in the database and organized by folders, displayed in the Sheets screen.

## Architecture

### Data Layer

#### Entities
- **SavedQuestionEntity** (`data/local/entity/SavedQuestionEntity.kt`)
  - Represents a question saved by the user
  - Fields:
    - `questionId`: Auto-generated primary key
    - `questionTitle`: Title of the question
    - `questionSlug`: Unique identifier for the question
    - `questionDifficulty`: Difficulty level (Easy, Medium, Hard)
    - `questionUrl`: Direct URL to the question
    - `folderId`: Foreign key linking to QuestionFolderEntity
    - `savedAt`: Timestamp when saved
    - `notes`: User notes about the question
    - `isSolved`: Boolean flag for completion status
    - `solvedAt`: Timestamp when marked as solved

- **QuestionFolderEntity** (`data/local/entity/QuestionFolderEntity.kt`)
  - Represents a folder created by the user
  - Fields:
    - `folderId`: Auto-generated primary key
    - `folderName`: Name of the folder
    - `folderDescription`: Optional description
    - `createdAt`: Folder creation timestamp
    - `updatedAt`: Last modification timestamp

#### Data Access Objects (DAOs)
- **SavedQuestionDao** (`data/local/dao/SavedQuestionDao.kt`)
  - CRUD operations for saved questions
  - Query methods:
    - `insertQuestion()`: Save a new question
    - `updateQuestion()`: Update question details or notes
    - `deleteQuestion()`: Remove a question
    - `getQuestionsByFolderId()`: Get all questions in a folder (Flow)
    - `getSolvedQuestions()`: Get completed questions
    - `getUnsolvedQuestions()`: Get pending questions
    - `markQuestionSolved()`: Update completion status
    - `searchQuestions()`: Search by title

- **QuestionFolderDao** (`data/local/dao/QuestionFolderDao.kt`)
  - CRUD operations for folders
  - Query methods:
    - `insertFolder()`: Create a new folder
    - `updateFolder()`: Update folder details
    - `deleteFolder()`: Remove a folder (cascade deletes questions)
    - `getAllFolders()`: Get all folders (Flow)
    - `searchFolders()`: Search by folder name

#### Database
- **KenDatabase** (`data/local/databases/KenDatabase.kt`)
  - Version: 7 (updated from version 6)
  - Entities: Added `QuestionFolderEntity` and `SavedQuestionEntity`
  - Provides DAOs: `questionFolderDao()` and `savedQuestionDao()`
  - Uses `fallbackToDestructiveMigration()` for version updates

### Domain Layer

#### Models
- **SavedQuestion** (`domain/models/SavedQuestion.kt`)
  - Domain model for saved questions
  - Contains conversion methods: `toDomainModel()` and `fromDomainModel()`

- **QuestionFolder** (`domain/models/QuestionFolder.kt`)
  - Domain model for folders
  - Contains conversion methods: `toDomainModel()` and `fromDomainModel()`

#### Repository Interface
- **SavedQuestionRepository** (`domain/repository/local/SavedQuestionRepository.kt`)
  - Contract for saved questions operations
  - Methods:
    - Folder operations: `createFolder()`, `updateFolder()`, `deleteFolder()`, `getAllFolders()`, `getFolderById()`, `searchFolders()`
    - Question operations: `saveQuestion()`, `updateQuestion()`, `deleteQuestion()`, `getQuestionsByFolder()`, `getAllQuestions()`, `markQuestionSolved()`, `searchQuestions()`, `getSolvedQuestions()`, `getUnsolvedQuestions()`

#### Use Cases
All use cases are in `domain/usecases/savedQuestions/`:

1. **CreateFolderUseCase** - Create a new folder with validation
2. **DeleteFolderUseCase** - Delete a folder and cascade delete its questions
3. **DeleteQuestionUseCase** - Delete a specific question
4. **GetAllFoldersUseCase** - Retrieve all folders as Flow or sync
5. **GetQuestionsByFolderUseCase** - Get questions in a specific folder
6. **SaveQuestionUseCase** - Save a new question with validation
7. **MarkQuestionSolvedUseCase** - Mark a question as solved/unsolved
8. **GetAllSavedQuestionsUseCase** - Get all saved questions across folders
9. **UpdateQuestionUseCase** - Update question notes

### Presentation Layer

#### ViewModel
- **SheetsScreenViewModel** (`presentation/screens/dashboard/sheets/SheetsScreenViewModel.kt`)
  - Manages UI state for the sheets screen
  - StateFlows:
    - `folders`: List of all folders
    - `selectedFolder`: Currently selected folder
    - `selectedFolderQuestions`: Questions in selected folder
    - `isLoading`: Loading state
    - `errorMessage`: Error messages
  - Key methods:
    - `loadFolders()`: Load all folders on init
    - `selectFolder()`: Switch to a folder and load its questions
    - `createFolder()`: Create a new folder
    - `deleteFolder()`: Delete a folder
    - `saveQuestion()`: Add a question to a folder
    - `deleteQuestion()`: Remove a question
    - `markQuestionSolved()`: Toggle solved status
    - `updateQuestionNotes()`: Update question notes

### Dependency Injection

#### RepositoryModule (`di/modules/RepositoryModule.kt`)
- Provides database instance
- Provides DAOs: `questionFolderDao()`, `savedQuestionDao()`
- Provides `SavedQuestionRepository` implementation

#### UseCaseModule (`di/modules/UseCaseModule.kt`)
- Provides all use case implementations
- All use cases are injected with `SavedQuestionRepository`

## Database Schema

### Tables

#### `question_folders`
```
folderId (INTEGER, PRIMARY KEY, autoincrement)
folderName (TEXT, NOT NULL)
folderDescription (TEXT)
createdAt (INTEGER)
updatedAt (INTEGER)
```

#### `saved_questions`
```
questionId (INTEGER, PRIMARY KEY, autoincrement)
questionTitle (TEXT, NOT NULL)
questionSlug (TEXT, NOT NULL)
questionDifficulty (TEXT)
questionUrl (TEXT)
folderId (INTEGER, FOREIGN KEY -> question_folders.folderId, ON DELETE CASCADE)
savedAt (INTEGER)
notes (TEXT)
isSolved (BOOLEAN)
solvedAt (INTEGER)
```

**Indices:**
- `saved_questions.folderId` - For efficient folder-based queries

## Usage Example

### In a Composable Screen

```kotlin
@Composable
fun MyQuestionScreen() {
    val viewModel: SheetsScreenViewModel = hiltViewModel()
    val folders by viewModel.folders.collectAsState()
    val selectedFolderQuestions by viewModel.selectedFolderQuestions.collectAsState()
    
    // Create a new folder
    viewModel.createFolder("DSA Problems", "Data Structures and Algorithms")
    
    // Save a question to a folder
    viewModel.saveQuestion(
        questionTitle = "Two Sum",
        questionSlug = "two-sum",
        folderId = 1,
        questionDifficulty = "Easy",
        questionUrl = "https://leetcode.com/problems/two-sum/"
    )
    
    // Mark as solved
    viewModel.markQuestionSolved(questionId = 1, isSolved = true)
    
    // Delete a question
    viewModel.deleteQuestion(questionId = 1)
    
    // Update notes
    viewModel.updateQuestionNotes(questionId = 1, notes = "Use HashMap for O(n) solution")
}
```

### Direct Repository Usage

```kotlin
// Inject the repository
@Inject
lateinit var savedQuestionRepository: SavedQuestionRepository

// Create folder
val folderId = savedQuestionRepository.createFolder("MyFolder", "My folder description")

// Save question
val questionId = savedQuestionRepository.saveQuestion(
    SavedQuestion(
        questionTitle = "Two Sum",
        questionSlug = "two-sum",
        folderId = folderId,
        questionDifficulty = "Easy"
    )
)

// Get questions in folder
val questions = savedQuestionRepository.getQuestionsByFolderSync(folderId)

// Mark as solved
savedQuestionRepository.markQuestionSolved(questionId, isSolved = true)
```

## Key Features

1. **Folder Organization**: Users can create multiple folders to organize questions by topics
2. **CRUD Operations**: Full Create, Read, Update, Delete functionality
3. **Cascade Deletion**: Deleting a folder automatically deletes all its questions
4. **Solved Status Tracking**: Track which questions have been solved with timestamps
5. **User Notes**: Add personal notes to each saved question
6. **Asynchronous Operations**: All operations use coroutines and Flow for reactive UI updates
7. **Validation**: Input validation in use cases to ensure data integrity
8. **Search Functionality**: Search questions by title or folders by name

## Flow of Data

1. User triggers save action from question details screen
2. `SheetsScreenViewModel.saveQuestion()` is called
3. ViewModel uses `SaveQuestionUseCase` to validate and save
4. Use case calls `SavedQuestionRepository.saveQuestion()`
5. Repository converts domain model to entity
6. `SavedQuestionDao.insertQuestion()` saves to database
7. Questions are automatically reloaded and displayed via Flow

## Migration Notes

- Database version updated from 6 to 7
- New tables: `question_folders` and `saved_questions`
- Uses `fallbackToDestructiveMigration()` for development/testing
- For production, implement proper migration scripts

## Testing

To test the functionality:

1. Create a folder
2. Save multiple questions to the folder
3. Update question status and notes
4. Delete questions and folders
5. Verify UI updates reflect changes
6. Check database contains expected data

## Future Enhancements

- [ ] Sync saved questions to backend/cloud
- [ ] Share folders with other users
- [ ] Export questions to PDF/CSV
- [ ] Tags and categories in addition to folders
- [ ] Question difficulty filtering
- [ ] Statistics dashboard (solved/unsolved ratio)
- [ ] Collaboration features
