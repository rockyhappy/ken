# Architecture Diagram - Saved Questions Feature

## System Architecture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          PRESENTATION LAYER                                 │
│  ┌──────────────────────────────────────────────────────────────────────┐  │
│  │                                                                      │  │
│  │  ┌─────────────────────────────────────────────────────────────┐   │  │
│  │  │  SheetsScreen.kt (Composable)                              │   │  │
│  │  │  - Displays folders and questions                          │   │  │
│  │  │  - Handles user interactions                               │   │  │
│  │  │  - Uses Hilt to inject ViewModel                           │   │  │
│  │  └─────────────────────────────────────────────────────────────┘   │  │
│  │                              ▲                                      │  │
│  │                              │                                      │  │
│  │                              │ Observes StateFlows                 │  │
│  │                              │                                      │  │
│  │  ┌─────────────────────────────────────────────────────────────┐   │  │
│  │  │  SheetsScreenViewModel.kt @HiltViewModel                   │   │  │
│  │  │  ┌───────────────────────────────────────────────────────┐ │   │  │
│  │  │  │ StateFlows:                                           │ │   │  │
│  │  │  │ - folders: StateFlow<List<QuestionFolder>>           │ │   │  │
│  │  │  │ - selectedFolder: StateFlow<QuestionFolder?>         │ │   │  │
│  │  │  │ - selectedFolderQuestions: StateFlow<List<...>>      │ │   │  │
│  │  │  │ - isLoading: StateFlow<Boolean>                      │ │   │  │
│  │  │  │ - errorMessage: StateFlow<String?>                  │ │   │  │
│  │  │  └───────────────────────────────────────────────────────┘ │   │  │
│  │  │                                                             │   │  │
│  │  │  ┌───────────────────────────────────────────────────────┐ │   │  │
│  │  │  │ Public Methods:                                       │ │   │  │
│  │  │  │ - selectFolder()        - deleteFolder()             │ │   │  │
│  │  │  │ - createFolder()        - deleteQuestion()           │ │   │  │
│  │  │  │ - saveQuestion()        - markQuestionSolved()       │ │   │  │
│  │  │  │ - updateQuestionNotes() - clearErrorMessage()        │ │   │  │
│  │  │  └───────────────────────────────────────────────────────┘ │   │  │
│  │  └─────────────────────────────────────────────────────────────┘   │  │
│  │                              ▲                                      │  │
│  │                              │                                      │  │
│  │                              │ Calls Use Cases                     │  │
│  └──────────────────────────────┼──────────────────────────────────────┘  │
│                                 │                                         │
├─────────────────────────────────┼─────────────────────────────────────────┤
│                                 │                                         │
│                          DOMAIN LAYER                                     │
│                                 │                                         │
│  ┌──────────────────────────────┴──────────────────────────────────────┐  │
│  │                                                                      │  │
│  │  ┌──────────────────────────────────────────────────────────────┐  │  │
│  │  │  Use Cases (9 Total)                                         │  │  │
│  │  │                                                              │  │  │
│  │  │  Folder Operations:                                          │  │  │
│  │  │  ├─ CreateFolderUseCase                                     │  │  │
│  │  │  ├─ DeleteFolderUseCase                                     │  │  │
│  │  │  └─ GetAllFoldersUseCase                                    │  │  │
│  │  │                                                              │  │  │
│  │  │  Question Operations:                                        │  │  │
│  │  │  ├─ SaveQuestionUseCase                                     │  │  │
│  │  │  ├─ DeleteQuestionUseCase                                   │  │  │
│  │  │  ├─ GetQuestionsByFolderUseCase                             │  │  │
│  │  │  ├─ GetAllSavedQuestionsUseCase                             │  │  │
│  │  │  ├─ MarkQuestionSolvedUseCase                               │  │  │
│  │  │  └─ UpdateQuestionUseCase                                   │  │  │
│  │  │                                                              │  │  │
│  │  │  Each use case:                                              │  │  │
│  │  │  - Validates input                                           │  │  │
│  │  │  - Calls repository method                                   │  │  │
│  │  │  - Throws exceptions on errors                               │  │  │
│  │  └──────────────────────────────────────────────────────────────┘  │  │
│  │                              ▲                                       │  │
│  │                              │                                       │  │
│  │                              │ Calls                                │  │
│  │                              │                                       │  │
│  │  ┌──────────────────────────────────────────────────────────────┐  │  │
│  │  │  SavedQuestionRepository (Interface)                        │  │  │
│  │  │  ┌──────────────────────────────────────────────────────┐  │  │  │
│  │  │  │ Folder Methods:                                      │  │  │  │
│  │  │  │ - createFolder()     - updateFolder()              │  │  │  │
│  │  │  │ - deleteFolder()     - getAllFolders()             │  │  │  │
│  │  │  │ - getFolderById()    - searchFolders()             │  │  │  │
│  │  │  └──────────────────────────────────────────────────────┘  │  │  │
│  │  │  ┌──────────────────────────────────────────────────────┐  │  │  │
│  │  │  │ Question Methods:                                    │  │  │  │
│  │  │  │ - saveQuestion()         - deleteQuestion()         │  │  │  │
│  │  │  │ - updateQuestion()       - deleteQuestionsByFolder()│  │  │  │
│  │  │  │ - getQuestionsByFolder() - getQuestionById()        │  │  │  │
│  │  │  │ - getAllQuestions()      - getQuestionBySlug()      │  │  │  │
│  │  │  │ - searchQuestions()      - getSolvedQuestions()     │  │  │  │
│  │  │  │ - getUnsolvedQuestions() - markQuestionSolved()     │  │  │  │
│  │  │  │ - getQuestionCountByFolder()                        │  │  │  │
│  │  │  └──────────────────────────────────────────────────────┘  │  │  │
│  │  └──────────────────────────────────────────────────────────────┘  │  │
│  │                                                                      │  │
│  │  ┌──────────────────────────────────────────────────────────────┐  │  │
│  │  │  Models                                                      │  │  │
│  │  │  - SavedQuestion (domain/models/)                           │  │  │
│  │  │  - QuestionFolder (domain/models/)                          │  │  │
│  │  └──────────────────────────────────────────────────────────────┘  │  │
│  │                                                                      │  │
│  └──────────────────────────────────────────────────────────────────────┘  │
│                                 ▲                                         │
│                                 │                                         │
│                                 │ Implements                             │
└─────────────────────────────────┼─────────────────────────────────────────┘
                                  │
┌─────────────────────────────────┼─────────────────────────────────────────┐
│                                 │                                         │
│                          DATA LAYER                                       │
│                                 │                                         │
│  ┌──────────────────────────────┴──────────────────────────────────────┐  │
│  │                                                                      │  │
│  │  ┌──────────────────────────────────────────────────────────────┐  │  │
│  │  │  SavedQuestionRepositoryImpl                                │  │  │
│  │  │  - Implements SavedQuestionRepository interface            │  │  │
│  │  │  - Maps domain models ↔ entities                          │  │  │
│  │  │  - Calls DAOs for all operations                          │  │  │
│  │  │  - Converts Flow<Entity> → Flow<Domain>                   │  │  │
│  │  └──────────────────────────────────────────────────────────────┘  │  │
│  │                              ▲                                       │  │
│  │                ┌─────────────┴─────────────┐                        │  │
│  │                │                           │                        │  │
│  │  ┌─────────────▼────────────┐  ┌──────────▼──────────────────────┐ │  │
│  │  │  SavedQuestionDao        │  │  QuestionFolderDao             │ │  │
│  │  │  @Dao                    │  │  @Dao                          │ │  │
│  │  │                          │  │                                │ │  │
│  │  │ Queries:                 │  │ Queries:                       │ │  │
│  │  │ - insertQuestion()       │  │ - insertFolder()              │ │  │
│  │  │ - updateQuestion()       │  │ - updateFolder()              │ │  │
│  │  │ - deleteQuestion()       │  │ - deleteFolder()              │ │  │
│  │  │ - deleteQuestionsByFolderId()│ - getAllFolders() (Flow)     │ │  │
│  │  │ - getQuestionsByFolderId()(F)│ - getFolderById()           │ │  │
│  │  │ - getQuestionsByFolderIdSync()│ - searchFolders()           │ │  │
│  │  │ - getAllQuestions() (Flow)   │ - getFoldersCount()         │ │  │
│  │  │ - getAllQuestionsSync()      │ - etc.                       │ │  │
│  │  │ - getQuestionById()          │                              │ │  │
│  │  │ - getQuestionBySlug()        │                              │ │  │
│  │  │ - searchQuestions()          │                              │ │  │
│  │  │ - getSolvedQuestions()       │                              │ │  │
│  │  │ - getUnsolvedQuestions()     │                              │ │  │
│  │  │ - markQuestionSolved()       │                              │ │  │
│  │  │ - getQuestionCountByFolderId()                             │ │  │
│  │  └──────────────┬───────────────┘  └──────────────┬───────────┘ │  │
│  │                 │                                 │              │  │
│  │                 └─────────────────┬───────────────┘              │  │
│  │                                   │                              │  │
│  │                    ┌──────────────┴────────────────┐             │  │
│  │                    │ Access                        │             │  │
│  │                    ▼                               ▼             │  │
│  │  ┌──────────────────────────────────────────────────────────┐  │  │
│  │  │  Entities (Room)                                         │  │  │
│  │  │                                                          │  │  │
│  │  │  ┌──────────────────────┐  ┌───────────────────────────┐ │  │  │
│  │  │  │ SavedQuestionEntity  │  │ QuestionFolderEntity     │ │  │  │
│  │  │  │ @Entity              │  │ @Entity                 │ │  │  │
│  │  │  │ tableName:           │  │ tableName:              │ │  │  │
│  │  │  │ "saved_questions"    │  │ "question_folders"      │ │  │  │
│  │  │  │                      │  │                         │ │  │  │
│  │  │  │ Fields:              │  │ Fields:                 │ │  │  │
│  │  │  │ - questionId (PK)    │  │ - folderId (PK)        │ │  │  │
│  │  │  │ - questionTitle      │  │ - folderName           │ │  │  │
│  │  │  │ - questionSlug       │  │ - folderDescription    │ │  │  │
│  │  │  │ - questionDifficulty │  │ - createdAt            │ │  │  │
│  │  │  │ - questionUrl        │  │ - updatedAt            │ │  │  │
│  │  │  │ - folderId (FK)------┼──│> (references folderId)│ │  │  │
│  │  │  │ - savedAt            │  │                         │ │  │  │
│  │  │  │ - notes              │  │                         │ │  │  │
│  │  │  │ - isSolved           │  │                         │ │  │  │
│  │  │  │ - solvedAt           │  │                         │ │  │  │
│  │  │  │                      │  │                         │ │  │  │
│  │  │  │ Indices:             │  │                         │ │  │  │
│  │  │  │ - folderId (for FK)  │  │                         │ │  │  │
│  │  │  │ - ON DELETE CASCADE  │  │                         │ │  │  │
│  │  │  └──────────────────────┘  └───────────────────────────┘ │  │  │
│  │  └──────────────────────────────────────────────────────────┘  │  │
│  │                              ▲                                   │  │
│  │                              │                                   │  │
│  │                              │ Query & Insert                   │  │
│  │  ┌──────────────────────────────────────────────────────────┐  │  │
│  │  │  KenDatabase (Room Database)                            │  │  │
│  │  │  @Database(version = 7)                                 │  │  │
│  │  │  abstract class KenDatabase : RoomDatabase              │  │  │
│  │  │                                                          │  │  │
│  │  │  Entities:                                              │  │  │
│  │  │  - SavedQuestionEntity (NEW)                            │  │  │
│  │  │  - QuestionFolderEntity (NEW)                           │  │  │
│  │  │  - LeetCodeUserEntity                                   │  │  │
│  │  │  - UserQuestionStatusEntity                             │  │  │
│  │  │  - and 3 more...                                        │  │  │
│  │  │                                                          │  │  │
│  │  │  Provides:                                              │  │  │
│  │  │  - savedQuestionDao()     (NEW)                         │  │  │
│  │  │  - questionFolderDao()    (NEW)                         │  │  │
│  │  │  - and 5 more DAOs...                                   │  │  │
│  │  │                                                          │  │  │
│  │  │  Database File:                                         │  │  │
│  │  │  Location: App's private directory                      │  │  │
│  │  │  Name: "ken_database"                                   │  │  │
│  │  │  Encrypted: Via Room's built-in encryption              │  │  │
│  │  └──────────────────────────────────────────────────────────┘  │  │
│  │                              ▲                                   │  │
│  │                              │                                   │  │
│  │                              │ SQLite                            │  │
│  │  ┌──────────────────────────────────────────────────────────┐  │  │
│  │  │  SQLite Database Files (on device)                      │  │  │
│  │  │  /data/data/com.devrachit.ken.{flavor}/                 │  │  │
│  │  │    databases/ken_database                               │  │  │
│  │  └──────────────────────────────────────────────────────────┘  │  │
│  │                                                                  │  │
│  └──────────────────────────────────────────────────────────────────┘  │
│                                                                         │
└─────────────────────────────────────────────────────────────────────────┘
```

## Data Flow Diagram

```
┌──────────────────────────────┐
│  User Action (Save Question) │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│  SheetsScreenViewModel.saveQuestion()    │
│  parameters: title, slug, folderId, ...  │
└──────────────┬───────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│  SaveQuestionUseCase.invoke()            │
│  - Validate input                        │
│  - Check title not empty                 │
│  - Check slug not empty                  │
│  - Check folderId > 0                    │
└──────────────┬───────────────────────────┘
               │
               ├─ Validation fails → Error thrown
               │
               ▼
┌──────────────────────────────────────────┐
│  SavedQuestionRepository.saveQuestion()  │
│  - Convert domain → entity                │
│  - Create SavedQuestionEntity             │
└──────────────┬───────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│  SavedQuestionDao.insertQuestion()       │
│  @Insert onConflict = REPLACE            │
└──────────────┬───────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│  Room Database Engine                    │
│  - Validate constraints                  │
│  - Check foreign key                     │
│  - Generate ID                           │
└──────────────┬───────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│  SQLite: INSERT into saved_questions     │
│  VALUES (id, title, slug, folderId, ...) │
└──────────────┬───────────────────────────┘
               │
               ▼ Success
┌──────────────────────────────────────────┐
│  Return questionId to Use Case           │
└──────────────┬───────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│  ViewModel updates selectedFolder        │
│  Calls loadQuestionsForFolder()          │
└──────────────┬───────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│  GetQuestionsByFolderUseCase.invoke()    │
└──────────────┬───────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│  Repository.getQuestionsByFolder()       │
│  Returns Flow<List<SavedQuestion>>       │
└──────────────┬───────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│  SavedQuestionDao.getQuestionsByFolderId()
│  @Query: SELECT * FROM saved_questions  │
│  WHERE folderId = ? ORDER BY savedAt    │
└──────────────┬───────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│  Room executes SQL query                 │
│  Returns List<SavedQuestionEntity>       │
└──────────────┬───────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│  Repository maps Entity → Domain Model   │
│  Returns Flow<List<SavedQuestion>>       │
└──────────────┬───────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│  ViewModel.selectedFolderQuestions       │
│  StateFlow emits new list                │
└──────────────┬───────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│  Composable collects as state            │
│  .collectAsState()                       │
└──────────────┬───────────────────────────┘
               │
               ▼
┌──────────────────────────────────────────┐
│  Recomposition triggered                 │
│  LazyColumn displays new questions       │
│  Each with action buttons                │
└──────────────────────────────────────────┘
```

## Dependency Injection Flow

```
┌─────────────────────────────────────┐
│  Hilt Component Bootstrap           │
│  @HiltAndroidApp on Application    │
└────────────┬────────────────────────┘
             │
             ▼
┌──────────────────────────────────────┐
│  RepositoryModule                    │
│  @Module                             │
│  @InstallIn(SingletonComponent)      │
├──────────────────────────────────────┤
│ Provides:                            │
│ ├─ KenDatabase.getDatabase()        │
│ ├─ QuestionFolderDao                │
│ ├─ SavedQuestionDao                 │
│ └─ SavedQuestionRepository impl      │
└────────────┬────────────────────────┘
             │
             ▼
┌──────────────────────────────────────┐
│  UseCaseModule                       │
│  @Module                             │
│  @InstallIn(SingletonComponent)      │
├──────────────────────────────────────┤
│ Provides:                            │
│ ├─ CreateFolderUseCase               │
│ ├─ DeleteFolderUseCase               │
│ ├─ SaveQuestionUseCase               │
│ ├─ GetAllFoldersUseCase              │
│ ├─ GetQuestionsByFolderUseCase       │
│ ├─ DeleteQuestionUseCase             │
│ ├─ MarkQuestionSolvedUseCase         │
│ ├─ UpdateQuestionUseCase             │
│ └─ GetAllSavedQuestionsUseCase       │
└────────────┬────────────────────────┘
             │
             ▼
┌──────────────────────────────────────┐
│  SheetsScreenViewModel Injection     │
│  @HiltViewModel                      │
│  hiltViewModel() in Composable      │
├──────────────────────────────────────┤
│ Constructor injects:                 │
│ ├─ GetAllFoldersUseCase              │
│ ├─ GetQuestionsByFolderUseCase       │
│ ├─ CreateFolderUseCase               │
│ ├─ DeleteFolderUseCase               │
│ ├─ DeleteQuestionUseCase             │
│ ├─ SaveQuestionUseCase               │
│ ├─ MarkQuestionSolvedUseCase         │
│ └─ UpdateQuestionUseCase             │
└────────────────────────────────────────┘
```

These diagrams provide a complete visual understanding of how the saved questions feature is architected and how data flows through the system.
