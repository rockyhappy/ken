package com.devrachit.ken.domain.usecases.savedQuestions

import com.devrachit.ken.domain.models.QuestionFolder
import com.devrachit.ken.domain.repository.local.SavedQuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllFoldersUseCase @Inject constructor(
    private val repository: SavedQuestionRepository
) {
    operator fun invoke(): Flow<List<QuestionFolder>> {
        return repository.getAllFolders()
    }

    suspend fun getSync(): List<QuestionFolder> {
        return repository.getAllFoldersSync()
    }
}
