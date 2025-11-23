package com.devrachit.ken.domain.repository.questions

import androidx.paging.PagingData
import com.devrachit.ken.domain.models.Question
import com.devrachit.ken.domain.models.QuestionSearchRequest
import kotlinx.coroutines.flow.Flow

interface QuestionsRepository {
    fun getQuestionsPagingFlow(searchRequest: QuestionSearchRequest): Flow<PagingData<Question>>
    suspend fun searchQuestions(query: String, limit: Int = 20): List<Question>
    suspend fun updateQuestionStatus(questionId: Int, status: String)
    suspend fun getQuestionById(questionId: Int): Question?
    suspend fun getAllDifficulties(): List<String>
    suspend fun getAllTopicTags(): List<String>
    suspend fun clearCache()
}
