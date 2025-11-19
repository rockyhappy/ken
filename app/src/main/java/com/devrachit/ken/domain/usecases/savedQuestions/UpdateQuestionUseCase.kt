package com.devrachit.ken.domain.usecases.savedQuestions

import com.devrachit.ken.domain.repository.local.SavedQuestionRepository
import javax.inject.Inject

class UpdateQuestionUseCase @Inject constructor(
    private val repository: SavedQuestionRepository
) {
    suspend operator fun invoke(questionId: Int, notes: String) {
        if (questionId <= 0) {
            throw IllegalArgumentException("Invalid question ID")
        }
        val question = repository.getQuestionById(questionId)
            ?: throw IllegalArgumentException("Question not found")
        repository.updateQuestion(question.copy(notes = notes))
    }
}
