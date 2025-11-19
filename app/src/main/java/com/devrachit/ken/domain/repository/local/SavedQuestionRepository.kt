package com.devrachit.ken.domain.repository.local

import com.devrachit.ken.domain.models.QuestionFolder
import com.devrachit.ken.domain.models.SavedQuestion
import kotlinx.coroutines.flow.Flow

interface SavedQuestionRepository {

    // Folder operations
    suspend fun createFolder(folderName: String, folderDescription: String): Long
    suspend fun updateFolder(folder: QuestionFolder)
    suspend fun deleteFolder(folderId: Int)
    fun getAllFolders(): Flow<List<QuestionFolder>>
    suspend fun getAllFoldersSync(): List<QuestionFolder>
    suspend fun getFolderById(folderId: Int): QuestionFolder?
    suspend fun searchFolders(folderName: String): List<QuestionFolder>

    // Question operations
    suspend fun saveQuestion(question: SavedQuestion): Long
    suspend fun updateQuestion(question: SavedQuestion)
    suspend fun deleteQuestion(questionId: Int)
    suspend fun deleteQuestionsByFolder(folderId: Int)
    fun getQuestionsByFolder(folderId: Int): Flow<List<SavedQuestion>>
    suspend fun getQuestionsByFolderSync(folderId: Int): List<SavedQuestion>
    suspend fun getAllQuestions(): List<SavedQuestion>
    suspend fun getQuestionById(questionId: Int): SavedQuestion?
    suspend fun getQuestionBySlug(questionSlug: String): SavedQuestion?
    suspend fun searchQuestions(title: String): List<SavedQuestion>
    suspend fun getSolvedQuestions(folderId: Int): List<SavedQuestion>
    suspend fun getUnsolvedQuestions(folderId: Int): List<SavedQuestion>
    suspend fun markQuestionSolved(questionId: Int, isSolved: Boolean = true)
    suspend fun getQuestionCountByFolder(folderId: Int): Int
}
