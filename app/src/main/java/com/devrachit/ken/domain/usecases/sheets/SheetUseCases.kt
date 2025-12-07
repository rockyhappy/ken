package com.devrachit.ken.domain.usecases.sheets

import com.devrachit.ken.data.local.entity.SheetEntity
import com.devrachit.ken.data.local.entity.SheetQuestionCrossRef
import com.devrachit.ken.data.local.entity.SheetWithQuestions
import com.devrachit.ken.domain.repository.local.SheetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSheetsUseCase @Inject constructor(
    private val repository: SheetRepository
) {
    operator fun invoke(): Flow<List<SheetEntity>> = repository.getAllSheets()
}

class GetAllSheetsWithQuestionsUseCase @Inject constructor(
    private val repository: SheetRepository
) {
    operator fun invoke(): Flow<List<SheetWithQuestions>> = repository.getAllSheetsWithQuestions()
    
    suspend fun once(): List<SheetWithQuestions> = repository.getAllSheetsWithQuestionsOnce()
}

class GetSheetWithQuestionsUseCase @Inject constructor(
    private val repository: SheetRepository
) {
    suspend operator fun invoke(sheetId: Long): SheetWithQuestions? = 
        repository.getSheetWithQuestions(sheetId)
}

class CreateSheetUseCase @Inject constructor(
    private val repository: SheetRepository
) {
    suspend operator fun invoke(name: String): Result<Long> {
        return try {
            if (name.isBlank()) {
                Result.failure(IllegalArgumentException("Sheet name cannot be empty"))
            } else {
                val existingSheet = repository.getSheetByName(name.trim())
                if (existingSheet != null) {
                    Result.failure(IllegalArgumentException("Sheet with this name already exists"))
                } else {
                    val id = repository.createSheet(name.trim())
                    Result.success(id)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class DeleteSheetUseCase @Inject constructor(
    private val repository: SheetRepository
) {
    suspend operator fun invoke(sheetId: Long): Result<Unit> {
        return try {
            repository.deleteSheet(sheetId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class AddQuestionToSheetUseCase @Inject constructor(
    private val repository: SheetRepository
) {
    suspend operator fun invoke(
        sheetId: Long,
        questionSlug: String,
        questionTitle: String,
        questionDifficulty: String,
        questionFrontendId: String
    ): Result<Unit> {
        return try {
            repository.addQuestionToSheet(
                sheetId = sheetId,
                questionSlug = questionSlug,
                questionTitle = questionTitle,
                questionDifficulty = questionDifficulty,
                questionFrontendId = questionFrontendId
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class RemoveQuestionFromSheetUseCase @Inject constructor(
    private val repository: SheetRepository
) {
    suspend operator fun invoke(sheetId: Long, questionSlug: String): Result<Unit> {
        return try {
            repository.removeQuestionFromSheet(sheetId, questionSlug)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetSheetsForQuestionUseCase @Inject constructor(
    private val repository: SheetRepository
) {
    suspend operator fun invoke(questionSlug: String): List<SheetEntity> = 
        repository.getSheetsForQuestion(questionSlug)
}

class IsQuestionInSheetUseCase @Inject constructor(
    private val repository: SheetRepository
) {
    suspend operator fun invoke(sheetId: Long, questionSlug: String): Boolean = 
        repository.isQuestionInSheet(sheetId, questionSlug)
}

class GetQuestionsForSheetUseCase @Inject constructor(
    private val repository: SheetRepository
) {
    operator fun invoke(sheetId: Long): Flow<List<SheetQuestionCrossRef>> = 
        repository.getQuestionsForSheet(sheetId)
    
    suspend fun once(sheetId: Long): List<SheetQuestionCrossRef> = 
        repository.getQuestionsForSheetOnce(sheetId)
}

data class SheetUseCases(
    val getAllSheets: GetAllSheetsUseCase,
    val getAllSheetsWithQuestions: GetAllSheetsWithQuestionsUseCase,
    val getSheetWithQuestions: GetSheetWithQuestionsUseCase,
    val createSheet: CreateSheetUseCase,
    val deleteSheet: DeleteSheetUseCase,
    val addQuestionToSheet: AddQuestionToSheetUseCase,
    val removeQuestionFromSheet: RemoveQuestionFromSheetUseCase,
    val getSheetsForQuestion: GetSheetsForQuestionUseCase,
    val isQuestionInSheet: IsQuestionInSheetUseCase,
    val getQuestionsForSheet: GetQuestionsForSheetUseCase
)
