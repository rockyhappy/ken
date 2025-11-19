package com.devrachit.ken.domain.usecases.savedQuestions

import com.devrachit.ken.domain.repository.local.SavedQuestionRepository
import javax.inject.Inject

class DeleteQuestionUseCase @Inject constructor(
    private val repository: SavedQuestionRepository
) {
    suspend operator fun invoke(questionId: Int) {
        if (questionId <= 0) {
            throw IllegalArgumentException("Invalid question ID")
        }
        repository.deleteQuestion(questionId)
    }
}
