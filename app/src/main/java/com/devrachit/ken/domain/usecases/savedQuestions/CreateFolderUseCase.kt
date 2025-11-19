package com.devrachit.ken.domain.usecases.savedQuestions

import com.devrachit.ken.domain.repository.local.SavedQuestionRepository
import javax.inject.Inject

class CreateFolderUseCase @Inject constructor(
    private val repository: SavedQuestionRepository
) {
    suspend operator fun invoke(folderName: String, folderDescription: String = ""): Long {
        if (folderName.isBlank()) {
            throw IllegalArgumentException("Folder name cannot be empty")
        }
        android.util.Log.d("CreateFolderUseCase", "invoke: Creating folder '$folderName'")
        val result = repository.createFolder(folderName, folderDescription)
        android.util.Log.d("CreateFolderUseCase", "invoke: Repository returned ID $result")
        return result
    }
}
