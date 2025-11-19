package com.devrachit.ken.domain.usecases.savedQuestions

import com.devrachit.ken.domain.models.SavedQuestion
import com.devrachit.ken.domain.repository.local.SavedQuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionsByFolderUseCase @Inject constructor(
    private val repository: SavedQuestionRepository
) {
    operator fun invoke(folderId: Int): Flow<List<SavedQuestion>> {
        if (folderId <= 0) {
            throw IllegalArgumentException("Invalid folder ID")
        }
        return repository.getQuestionsByFolder(folderId)
    }

    suspend fun getSync(folderId: Int): List<SavedQuestion> {
        if (folderId <= 0) {
            throw IllegalArgumentException("Invalid folder ID")
        }
        return repository.getQuestionsByFolderSync(folderId)
    }
}
