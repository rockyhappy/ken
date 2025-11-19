package com.devrachit.ken.domain.usecases.savedQuestions

import com.devrachit.ken.domain.models.SavedQuestion
import com.devrachit.ken.domain.repository.local.SavedQuestionRepository
import javax.inject.Inject

class GetAllSavedQuestionsUseCase @Inject constructor(
    private val repository: SavedQuestionRepository
) {
    suspend operator fun invoke(): List<SavedQuestion> {
        return repository.getAllQuestions()
    }
}
