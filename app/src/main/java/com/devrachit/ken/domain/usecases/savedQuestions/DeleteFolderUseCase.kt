package com.devrachit.ken.domain.usecases.savedQuestions

import com.devrachit.ken.domain.repository.local.SavedQuestionRepository
import javax.inject.Inject

class DeleteFolderUseCase @Inject constructor(
    private val repository: SavedQuestionRepository
) {
    suspend operator fun invoke(folderId: Int) {
        if (folderId <= 0) {
            throw IllegalArgumentException("Invalid folder ID")
        }
        repository.deleteFolder(folderId)
    }
}
