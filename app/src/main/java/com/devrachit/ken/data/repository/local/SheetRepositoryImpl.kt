package com.devrachit.ken.data.repository.local

import com.devrachit.ken.data.local.dao.SheetDao
import com.devrachit.ken.data.local.entity.SheetEntity
import com.devrachit.ken.data.local.entity.SheetQuestionCrossRef
import com.devrachit.ken.data.local.entity.SheetWithQuestions
import com.devrachit.ken.domain.repository.local.SheetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SheetRepositoryImpl @Inject constructor(
    private val sheetDao: SheetDao
) : SheetRepository {

    override fun getAllSheets(): Flow<List<SheetEntity>> {
        return sheetDao.getAllSheets()
    }

    override suspend fun getAllSheetsOnce(): List<SheetEntity> {
        return sheetDao.getAllSheetsOnce()
    }

    override fun getAllSheetsWithQuestions(): Flow<List<SheetWithQuestions>> {
        return sheetDao.getAllSheetsWithQuestions()
    }

    override suspend fun getAllSheetsWithQuestionsOnce(): List<SheetWithQuestions> {
        return sheetDao.getAllSheetsWithQuestionsOnce()
    }

    override suspend fun getSheetById(sheetId: Long): SheetEntity? {
        return sheetDao.getSheetById(sheetId)
    }

    override suspend fun getSheetByName(name: String): SheetEntity? {
        return sheetDao.getSheetByName(name)
    }

    override suspend fun getSheetWithQuestions(sheetId: Long): SheetWithQuestions? {
        return sheetDao.getSheetWithQuestions(sheetId)
    }

    override suspend fun createSheet(name: String): Long {
        val sheet = SheetEntity(name = name)
        return sheetDao.insertSheet(sheet)
    }

    override suspend fun updateSheet(sheet: SheetEntity) {
        sheetDao.updateSheet(sheet.copy(updatedAt = System.currentTimeMillis()))
    }

    override suspend fun deleteSheet(sheetId: Long) {
        sheetDao.removeAllQuestionsFromSheet(sheetId)
        sheetDao.deleteSheetById(sheetId)
    }

    override suspend fun addQuestionToSheet(
        sheetId: Long,
        questionSlug: String,
        questionTitle: String,
        questionDifficulty: String,
        questionFrontendId: String
    ) {
        val crossRef = SheetQuestionCrossRef(
            sheetId = sheetId,
            questionSlug = questionSlug,
            questionTitle = questionTitle,
            questionDifficulty = questionDifficulty,
            questionFrontendId = questionFrontendId
        )
        sheetDao.addQuestionToSheet(crossRef)
        sheetDao.updateSheetTimestamp(sheetId)
    }

    override suspend fun removeQuestionFromSheet(sheetId: Long, questionSlug: String) {
        sheetDao.removeQuestionFromSheet(sheetId, questionSlug)
        sheetDao.updateSheetTimestamp(sheetId)
    }

    override suspend fun getSheetsForQuestion(questionSlug: String): List<SheetEntity> {
        return sheetDao.getSheetEntitiesForQuestion(questionSlug)
    }

    override suspend fun isQuestionInSheet(sheetId: Long, questionSlug: String): Boolean {
        return sheetDao.isQuestionInSheet(sheetId, questionSlug)
    }

    override suspend fun getQuestionCountForSheet(sheetId: Long): Int {
        return sheetDao.getQuestionCountForSheet(sheetId)
    }

    override fun getQuestionsForSheet(sheetId: Long): Flow<List<SheetQuestionCrossRef>> {
        return sheetDao.getQuestionsForSheet(sheetId)
    }

    override suspend fun getQuestionsForSheetOnce(sheetId: Long): List<SheetQuestionCrossRef> {
        return sheetDao.getQuestionsForSheetOnce(sheetId)
    }
}
