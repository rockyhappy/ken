package com.devrachit.ken.data.repository.local

import com.devrachit.ken.data.local.dao.QuestionFolderDao
import com.devrachit.ken.data.local.dao.SavedQuestionDao
import com.devrachit.ken.data.local.entity.QuestionFolderEntity
import com.devrachit.ken.data.local.entity.SavedQuestionEntity
import com.devrachit.ken.domain.models.QuestionFolder
import com.devrachit.ken.domain.models.SavedQuestion
import com.devrachit.ken.domain.repository.local.SavedQuestionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SavedQuestionRepositoryImpl @Inject constructor(
    private val questionFolderDao: QuestionFolderDao,
    private val savedQuestionDao: SavedQuestionDao
) : SavedQuestionRepository {

    // ==================== Folder Operations ====================

    override suspend fun createFolder(folderName: String, folderDescription: String): Long {
        android.util.Log.d("SavedQuestionRepository", "createFolder: Inserting folder '$folderName'")
        val folder = QuestionFolderEntity(
            folderName = folderName,
            folderDescription = folderDescription
        )
        val result = questionFolderDao.insertFolder(folder)
        android.util.Log.d("SavedQuestionRepository", "createFolder: DAO returned ID $result")
        return result
    }

    override suspend fun updateFolder(folder: QuestionFolder) {
        val entity = QuestionFolderEntity.fromDomainModel(folder)
        entity.copy(updatedAt = System.currentTimeMillis()).let {
            questionFolderDao.updateFolder(it)
        }
    }

    override suspend fun deleteFolder(folderId: Int) {
        // Delete all questions in the folder first (cascade handled by FK but explicit for clarity)
        savedQuestionDao.deleteQuestionsByFolderId(folderId)
        questionFolderDao.deleteFolderById(folderId)
    }

    override fun getAllFolders(): Flow<List<QuestionFolder>> {
        android.util.Log.d("SavedQuestionRepository", "getAllFolders: Querying database")
        return questionFolderDao.getAllFoldersWithCounts().map { entities ->
            android.util.Log.d("SavedQuestionRepository", "getAllFolders: DAO returned ${entities.size} entities")
            entities.forEach { entity ->
                android.util.Log.d("SavedQuestionRepository", "  - Entity: ${entity.folderName} with ${entity.questionCount} questions")
            }
            val domainModels = entities.map { 
                QuestionFolder(
                    folderId = it.folderId,
                    folderName = it.folderName,
                    folderDescription = it.folderDescription,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                    questionCount = it.questionCount
                )
            }
            android.util.Log.d("SavedQuestionRepository", "getAllFolders: Mapped to ${domainModels.size} domain models")
            domainModels
        }
    }

    override suspend fun getAllFoldersSync(): List<QuestionFolder> {
        return questionFolderDao.getAllFoldersWithCountsSync().map { 
            QuestionFolder(
                folderId = it.folderId,
                folderName = it.folderName,
                folderDescription = it.folderDescription,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                questionCount = it.questionCount
            )
        }
    }

    override suspend fun getFolderById(folderId: Int): QuestionFolder? {
        return questionFolderDao.getFolderById(folderId)?.toDomainModel()
    }

    override suspend fun searchFolders(folderName: String): List<QuestionFolder> {
        return questionFolderDao.searchFolders(folderName).map { it.toDomainModel() }
    }

    // ==================== Question Operations ====================

    override suspend fun saveQuestion(question: SavedQuestion): Long {
        val entity = SavedQuestionEntity.fromDomainModel(question)
        return savedQuestionDao.insertQuestion(entity)
    }

    override suspend fun updateQuestion(question: SavedQuestion) {
        val entity = SavedQuestionEntity.fromDomainModel(question)
        savedQuestionDao.updateQuestion(entity)
    }

    override suspend fun deleteQuestion(questionId: Int) {
        savedQuestionDao.deleteQuestionById(questionId)
    }

    override suspend fun deleteQuestionsByFolder(folderId: Int) {
        savedQuestionDao.deleteQuestionsByFolderId(folderId)
    }

    override fun getQuestionsByFolder(folderId: Int): Flow<List<SavedQuestion>> {
        return savedQuestionDao.getQuestionsByFolderId(folderId).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getQuestionsByFolderSync(folderId: Int): List<SavedQuestion> {
        return savedQuestionDao.getQuestionsByFolderIdSync(folderId).map { it.toDomainModel() }
    }

    override suspend fun getAllQuestions(): List<SavedQuestion> {
        return savedQuestionDao.getAllQuestionsSync().map { it.toDomainModel() }
    }

    override suspend fun getQuestionById(questionId: Int): SavedQuestion? {
        return savedQuestionDao.getQuestionById(questionId)?.toDomainModel()
    }

    override suspend fun getQuestionBySlug(questionSlug: String): SavedQuestion? {
        return savedQuestionDao.getQuestionBySlug(questionSlug)?.toDomainModel()
    }

    override suspend fun searchQuestions(title: String): List<SavedQuestion> {
        return savedQuestionDao.searchQuestions(title).map { it.toDomainModel() }
    }

    override suspend fun getSolvedQuestions(folderId: Int): List<SavedQuestion> {
        return savedQuestionDao.getSolvedQuestions(folderId).map { it.toDomainModel() }
    }

    override suspend fun getUnsolvedQuestions(folderId: Int): List<SavedQuestion> {
        return savedQuestionDao.getUnsolvedQuestions(folderId).map { it.toDomainModel() }
    }

    override suspend fun markQuestionSolved(questionId: Int, isSolved: Boolean) {
        val solvedAt = if (isSolved) System.currentTimeMillis() else null
        savedQuestionDao.markQuestionSolved(questionId, isSolved, solvedAt)
    }

    override suspend fun getQuestionCountByFolder(folderId: Int): Int {
        return savedQuestionDao.getQuestionCountByFolderId(folderId)
    }
}
