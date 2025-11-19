package com.devrachit.ken.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devrachit.ken.data.local.entity.SavedQuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedQuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: SavedQuestionEntity): Long

    @Update
    suspend fun updateQuestion(question: SavedQuestionEntity)

    @Delete
    suspend fun deleteQuestion(question: SavedQuestionEntity)

    @Query("DELETE FROM saved_questions WHERE questionId = :questionId")
    suspend fun deleteQuestionById(questionId: Int)

    @Query("DELETE FROM saved_questions WHERE folderId = :folderId")
    suspend fun deleteQuestionsByFolderId(folderId: Int)

    @Query("SELECT * FROM saved_questions WHERE questionId = :questionId")
    suspend fun getQuestionById(questionId: Int): SavedQuestionEntity?

    @Query("SELECT * FROM saved_questions WHERE folderId = :folderId ORDER BY savedAt DESC")
    fun getQuestionsByFolderId(folderId: Int): Flow<List<SavedQuestionEntity>>

    @Query("SELECT * FROM saved_questions WHERE folderId = :folderId ORDER BY savedAt DESC")
    suspend fun getQuestionsByFolderIdSync(folderId: Int): List<SavedQuestionEntity>

    @Query("SELECT * FROM saved_questions ORDER BY savedAt DESC")
    fun getAllQuestions(): Flow<List<SavedQuestionEntity>>

    @Query("SELECT * FROM saved_questions ORDER BY savedAt DESC")
    suspend fun getAllQuestionsSync(): List<SavedQuestionEntity>

    @Query("SELECT COUNT(*) FROM saved_questions WHERE folderId = :folderId")
    suspend fun getQuestionCountByFolderId(folderId: Int): Int

    @Query("SELECT COUNT(*) FROM saved_questions")
    suspend fun getTotalQuestionsCount(): Int

    @Query("SELECT * FROM saved_questions WHERE questionSlug = :questionSlug")
    suspend fun getQuestionBySlug(questionSlug: String): SavedQuestionEntity?

    @Query("SELECT * FROM saved_questions WHERE questionTitle LIKE '%' || :title || '%' ORDER BY savedAt DESC")
    suspend fun searchQuestions(title: String): List<SavedQuestionEntity>

    @Query("SELECT * FROM saved_questions WHERE folderId = :folderId AND isSolved = 1 ORDER BY solvedAt DESC")
    suspend fun getSolvedQuestions(folderId: Int): List<SavedQuestionEntity>

    @Query("SELECT * FROM saved_questions WHERE folderId = :folderId AND isSolved = 0 ORDER BY savedAt DESC")
    suspend fun getUnsolvedQuestions(folderId: Int): List<SavedQuestionEntity>

    @Query("UPDATE saved_questions SET isSolved = :isSolved, solvedAt = :solvedAt WHERE questionId = :questionId")
    suspend fun markQuestionSolved(questionId: Int, isSolved: Boolean, solvedAt: Long? = null)
}
