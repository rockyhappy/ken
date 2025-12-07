package com.devrachit.ken.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.devrachit.ken.data.local.entity.SheetEntity
import com.devrachit.ken.data.local.entity.SheetQuestionCrossRef
import com.devrachit.ken.data.local.entity.SheetWithQuestions
import kotlinx.coroutines.flow.Flow

@Dao
interface SheetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSheet(sheet: SheetEntity): Long

    @Update
    suspend fun updateSheet(sheet: SheetEntity)

    @Delete
    suspend fun deleteSheet(sheet: SheetEntity)

    @Query("DELETE FROM sheets WHERE id = :sheetId")
    suspend fun deleteSheetById(sheetId: Long)

    @Query("SELECT * FROM sheets ORDER BY updatedAt DESC")
    fun getAllSheets(): Flow<List<SheetEntity>>

    @Query("SELECT * FROM sheets ORDER BY updatedAt DESC")
    suspend fun getAllSheetsOnce(): List<SheetEntity>

    @Query("SELECT * FROM sheets WHERE id = :sheetId")
    suspend fun getSheetById(sheetId: Long): SheetEntity?

    @Query("SELECT * FROM sheets WHERE name = :name LIMIT 1")
    suspend fun getSheetByName(name: String): SheetEntity?

    @Transaction
    @Query("SELECT * FROM sheets WHERE id = :sheetId")
    suspend fun getSheetWithQuestions(sheetId: Long): SheetWithQuestions?

    @Transaction
    @Query("SELECT * FROM sheets ORDER BY updatedAt DESC")
    fun getAllSheetsWithQuestions(): Flow<List<SheetWithQuestions>>

    @Transaction
    @Query("SELECT * FROM sheets ORDER BY updatedAt DESC")
    suspend fun getAllSheetsWithQuestionsOnce(): List<SheetWithQuestions>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuestionToSheet(crossRef: SheetQuestionCrossRef)

    @Query("DELETE FROM sheet_question_cross_ref WHERE sheetId = :sheetId AND questionSlug = :questionSlug")
    suspend fun removeQuestionFromSheet(sheetId: Long, questionSlug: String)

    @Query("DELETE FROM sheet_question_cross_ref WHERE sheetId = :sheetId")
    suspend fun removeAllQuestionsFromSheet(sheetId: Long)

    @Query("SELECT * FROM sheet_question_cross_ref WHERE questionSlug = :questionSlug")
    suspend fun getSheetsForQuestion(questionSlug: String): List<SheetQuestionCrossRef>

    @Query("""
        SELECT s.* FROM sheets s 
        INNER JOIN sheet_question_cross_ref sq ON s.id = sq.sheetId 
        WHERE sq.questionSlug = :questionSlug
    """)
    suspend fun getSheetEntitiesForQuestion(questionSlug: String): List<SheetEntity>

    @Query("SELECT COUNT(*) FROM sheet_question_cross_ref WHERE sheetId = :sheetId")
    suspend fun getQuestionCountForSheet(sheetId: Long): Int

    @Query("SELECT EXISTS(SELECT 1 FROM sheet_question_cross_ref WHERE sheetId = :sheetId AND questionSlug = :questionSlug)")
    suspend fun isQuestionInSheet(sheetId: Long, questionSlug: String): Boolean

    @Query("UPDATE sheets SET updatedAt = :updatedAt WHERE id = :sheetId")
    suspend fun updateSheetTimestamp(sheetId: Long, updatedAt: Long = System.currentTimeMillis())

    @Query("SELECT * FROM sheet_question_cross_ref WHERE sheetId = :sheetId ORDER BY addedAt DESC")
    fun getQuestionsForSheet(sheetId: Long): Flow<List<SheetQuestionCrossRef>>

    @Query("SELECT * FROM sheet_question_cross_ref WHERE sheetId = :sheetId ORDER BY addedAt DESC")
    suspend fun getQuestionsForSheetOnce(sheetId: Long): List<SheetQuestionCrossRef>
}
