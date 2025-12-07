package com.devrachit.ken.domain.repository.local

import com.devrachit.ken.data.local.entity.SheetEntity
import com.devrachit.ken.data.local.entity.SheetQuestionCrossRef
import com.devrachit.ken.data.local.entity.SheetWithQuestions
import kotlinx.coroutines.flow.Flow

interface SheetRepository {
    
    fun getAllSheets(): Flow<List<SheetEntity>>
    
    suspend fun getAllSheetsOnce(): List<SheetEntity>
    
    fun getAllSheetsWithQuestions(): Flow<List<SheetWithQuestions>>
    
    suspend fun getAllSheetsWithQuestionsOnce(): List<SheetWithQuestions>
    
    suspend fun getSheetById(sheetId: Long): SheetEntity?
    
    suspend fun getSheetByName(name: String): SheetEntity?
    
    suspend fun getSheetWithQuestions(sheetId: Long): SheetWithQuestions?
    
    suspend fun createSheet(name: String): Long
    
    suspend fun updateSheet(sheet: SheetEntity)
    
    suspend fun deleteSheet(sheetId: Long)
    
    suspend fun addQuestionToSheet(
        sheetId: Long,
        questionSlug: String,
        questionTitle: String,
        questionDifficulty: String,
        questionFrontendId: String
    )
    
    suspend fun removeQuestionFromSheet(sheetId: Long, questionSlug: String)
    
    suspend fun getSheetsForQuestion(questionSlug: String): List<SheetEntity>
    
    suspend fun isQuestionInSheet(sheetId: Long, questionSlug: String): Boolean
    
    suspend fun getQuestionCountForSheet(sheetId: Long): Int
    
    fun getQuestionsForSheet(sheetId: Long): Flow<List<SheetQuestionCrossRef>>
    
    suspend fun getQuestionsForSheetOnce(sheetId: Long): List<SheetQuestionCrossRef>
}
