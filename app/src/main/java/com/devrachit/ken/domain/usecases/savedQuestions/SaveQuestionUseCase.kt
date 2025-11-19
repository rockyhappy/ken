package com.devrachit.ken.domain.usecases.savedQuestions

import com.devrachit.ken.domain.models.SavedQuestion
import com.devrachit.ken.domain.repository.local.SavedQuestionRepository
import javax.inject.Inject

class SaveQuestionUseCase @Inject constructor(
    private val repository: SavedQuestionRepository
) {
    suspend operator fun invoke(question: SavedQuestion): Long {
        if (question.questionTitle.isBlank()) {
            throw IllegalArgumentException("Question title cannot be empty")
        }
        if (question.questionSlug.isBlank()) {
            throw IllegalArgumentException("Question slug cannot be empty")
        }
        if (question.folderId <= 0) {
            throw IllegalArgumentException("Invalid folder ID")
        }
        return repository.saveQuestion(question)
    }
}
