# Developer Checklist - Saved Questions Feature

## Pre-Integration Checklist

### ✅ Core Implementation
- [x] SavedQuestionEntity created (data/local/entity/)
- [x] QuestionFolderEntity created (data/local/entity/)
- [x] SavedQuestionDao created (data/local/dao/)
- [x] QuestionFolderDao created (data/local/dao/)
- [x] KenDatabase updated to version 7
- [x] SavedQuestion domain model created
- [x] QuestionFolder domain model created
- [x] SavedQuestionRepository interface created
- [x] SavedQuestionRepositoryImpl created
- [x] 9 Use Cases created
- [x] SheetsScreenViewModel created
- [x] Dependency Injection configured

### ✅ Documentation
- [x] SAVED_QUESTIONS_README.md
- [x] INTEGRATION_GUIDE.md
- [x] IMPLEMENTATION_SUMMARY.md
- [x] QUICK_REFERENCE.md
- [x] UI_COMPONENTS.md
- [x] COMPLETE_SUMMARY.md

## Building & Testing

### Build Verification
- [ ] Run `./gradlew clean build` - should compile without errors
- [ ] Verify no import errors in IDE
- [ ] Check database version updated correctly (7)
- [ ] Confirm all DAOs are accessible in KenDatabase

### Unit Testing (Optional)
- [ ] Test CreateFolderUseCase validation
- [ ] Test SaveQuestionUseCase validation
- [ ] Test SavedQuestionRepositoryImpl methods
- [ ] Test DAO queries

### Integration Testing
```bash
# Run instrumented tests
./gradlew connectedAndroidTest
```

## Feature Integration Steps

### Step 1: Update QuestionDetailsSettings.kt
- [ ] Add "Save to Sheets" menu item
- [ ] Import necessary classes
- [ ] Add callback parameter: `onSaveToSheets: (String, String) -> Unit`

### Step 2: Update QuestionDetailsContent.kt
- [ ] Pass onSaveToSheets callback through
- [ ] Implement callback forwarding
- [ ] Handle potential errors

### Step 3: Update QuestionsDetailsScreen.kt
- [ ] Inject SheetsScreenViewModel
- [ ] Show SaveToSheetsDialog on menu click
- [ ] Implement save logic
- [ ] Add error/success toast messages

### Step 4: Implement SaveToSheetsDialog
- [ ] Create dialog composable (use UI_COMPONENTS.md reference)
- [ ] Show folder list with radio buttons
- [ ] Allow optional notes input
- [ ] Handle save and cancel actions

### Step 5: Update SheetsScreen.kt
- [ ] Use ViewModel provided via hiltViewModel()
- [ ] Display folders from viewModel.folders
- [ ] Show questions for selected folder
- [ ] Add action buttons (mark solved, delete, edit notes)
- [ ] Handle loading and error states

### Step 6: UI Polish
- [ ] Add appropriate icons for buttons
- [ ] Style dialogs to match app theme
- [ ] Add confirmation dialogs for destructive actions
- [ ] Implement proper error toasts/snackbars
- [ ] Test responsive layout on different screen sizes

## Runtime Testing Checklist

### Folder Operations
- [ ] Create folder with name
- [ ] Create folder with name and description
- [ ] Verify folder appears in list
- [ ] Edit folder name
- [ ] Delete folder
- [ ] Verify cascade deletion of questions

### Question Operations
- [ ] Save question to folder
- [ ] Verify question count in folder
- [ ] View questions in folder
- [ ] Edit question notes
- [ ] Mark question as solved
- [ ] Mark question as unsolved
- [ ] Delete single question
- [ ] Search questions by title

### UI/UX Testing
- [ ] Empty state shows when no folders
- [ ] Loading indicator appears while loading
- [ ] Error messages display correctly
- [ ] Navigation works smoothly
- [ ] Toasts appear on successful actions
- [ ] Buttons are clickable and responsive
- [ ] Dialogs close properly

### Data Persistence
- [ ] Data persists after app restart
- [ ] Questions remain in correct folder
- [ ] Solved status persists
- [ ] Notes persist correctly
- [ ] Deletion is permanent

### Error Handling
- [ ] Duplicate questions prevented
- [ ] Empty folder name rejected
- [ ] Invalid folder ID handled
- [ ] Database errors show user message
- [ ] Network issues don't crash app

## Database Testing

### SQL Queries (via Android Studio)
```sql
-- Check all folders
SELECT * FROM question_folders;

-- Check all questions
SELECT * FROM saved_questions;

-- Check questions in a folder
SELECT * FROM saved_questions WHERE folderId = 1;

-- Check solved questions
SELECT * FROM saved_questions WHERE isSolved = 1;

-- Count questions per folder
SELECT folderId, COUNT(*) as count FROM saved_questions GROUP BY folderId;
```

### Database Inspector (Android Studio)
- [ ] Navigate to Database Inspector tab
- [ ] Verify question_folders table exists
- [ ] Verify saved_questions table exists
- [ ] Check foreign key constraint works
- [ ] Verify cascade delete works

## Performance Optimization

### Initial Implementation Review
- [ ] All queries are optimized (indices present)
- [ ] No N+1 query problems
- [ ] Flow collections don't leak
- [ ] ViewModels properly scoped
- [ ] No memory leaks in dialogs

### Load Testing
- [ ] Add 100+ questions to folder
- [ ] Verify scroll performance is smooth
- [ ] Check memory usage doesn't spike
- [ ] Verify search still responsive

## Documentation Review

- [ ] README accurately describes feature
- [ ] Code examples work correctly
- [ ] All file paths are accurate
- [ ] Architecture diagram is clear
- [ ] Quick reference has useful snippets
- [ ] UI components are copy-paste ready

## Code Review Checklist

- [ ] No hardcoded strings
- [ ] Proper error handling throughout
- [ ] Input validation present
- [ ] No null pointer exceptions possible
- [ ] Naming conventions followed
- [ ] Comments explain complex logic
- [ ] No duplicate code
- [ ] Proper encapsulation

## Deployment Checklist

### Pre-Release
- [ ] All features working on target device
- [ ] No warnings or errors in build
- [ ] All documentation reviewed
- [ ] Database migration strategy confirmed
- [ ] Error messages are user-friendly

### Release
- [ ] Bump version number if needed
- [ ] Update release notes
- [ ] Tag release in git
- [ ] Create backup of production database

## Post-Release Monitoring

- [ ] Monitor crash reports
- [ ] Check for database corruption reports
- [ ] Verify user feedback on feature
- [ ] Monitor performance metrics
- [ ] Check battery/memory impact

## Future Enhancements (Not in Current Release)

- [ ] Cloud sync for questions
- [ ] Share folders with other users
- [ ] Export questions to PDF
- [ ] Tags in addition to folders
- [ ] Question difficulty filtering
- [ ] Statistics dashboard
- [ ] Collaboration features
- [ ] Question solving hints
- [ ] Time tracking for questions
- [ ] Difficulty progression

## Troubleshooting Guide

### Issue: Questions not saving
**Solution:**
1. Verify folder exists
2. Check database version is 7
3. Verify folderId is valid (> 0)
4. Check error message in errorMessage StateFlow

### Issue: App crashes on folder deletion
**Solution:**
1. Verify cascade delete is enabled
2. Check no queries reference deleted folder
3. Ensure ViewModel resets selected folder

### Issue: UI not updating
**Solution:**
1. Verify StateFlow is collected with `.collectAsState()`
2. Check ViewModel is using viewModelScope
3. Verify Flow is not being collected multiple times
4. Monitor for collection cancellation

### Issue: Database migration error
**Solution:**
1. Verify KenDatabase version is 7
2. Check all new entities are in @Database annotation
3. Confirm fallbackToDestructiveMigration() is present
4. Clear app data if in development

## Sign-Off

- [ ] Developer: Code complete and tested
- [ ] Code Reviewer: Approved
- [ ] QA: Feature tested and working
- [ ] PM: Feature release approved
- [ ] Release: Feature deployed to production

## Notes

_Add any additional notes or issues here:_

```
[Add notes here]
```

---

**Last Updated:** [Date]
**Version:** 1.0
**Status:** Ready for Integration
