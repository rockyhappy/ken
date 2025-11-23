package com.devrachit.ken.domain.usecases.questions

import androidx.paging.PagingData
import com.devrachit.ken.domain.models.Question
import com.devrachit.ken.domain.models.QuestionSearchRequest
import com.devrachit.ken.domain.repository.questions.QuestionsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val questionsRepository: QuestionsRepository
) {
    operator fun invoke(searchRequest: QuestionSearchRequest): Flow<PagingData<Question>> {
        return questionsRepository.getQuestionsPagingFlow(searchRequest)
    }
}

class SearchQuestionsUseCase @Inject constructor(
    private val questionsRepository: QuestionsRepository
) {
    suspend operator fun invoke(query: String, limit: Int = 20): List<Question> {
        return questionsRepository.searchQuestions(query, limit)
    }
}

class SearchQuestionsRemoteUseCase @Inject constructor(
    private val remoteRepository: com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
) {
    suspend operator fun invoke(searchKeyword: String, limit: Int = 50): List<Question> {
        val result = remoteRepository.searchQuestions(searchKeyword, limit)
        return when (result) {
            is com.devrachit.ken.utility.NetworkUtility.Resource.Success -> {
                result.data?.data?.problemsetQuestionListV2?.questions?.map { searchNode ->
                    Question(
                        acRate = searchNode.acRate,
                        difficulty = searchNode.difficulty,
                        frontendQuestionId = searchNode.questionFrontendId,
                        paidOnly = searchNode.paidOnly,
                        status = searchNode.status,
                        title = searchNode.title,
                        titleSlug = searchNode.titleSlug,
                        topicTags = searchNode.topicTags.map { tag ->
                            com.devrachit.ken.domain.models.TopicTag(
                                name = tag.name,
                                slug = tag.slug,
                                id = tag.slug
                            )
                        }
                    )
                } ?: emptyList()
            }
            else -> emptyList()
        }
    }
}

class UpdateQuestionStatusUseCase @Inject constructor(
    private val questionsRepository: QuestionsRepository
) {
    suspend operator fun invoke(questionId: Int, status: String) {
        questionsRepository.updateQuestionStatus(questionId, status)
    }
}

class GetQuestionFiltersUseCase @Inject constructor(
    private val questionsRepository: QuestionsRepository
) {
    suspend fun getDifficulties(): List<String> {
        return questionsRepository.getAllDifficulties()
    }

    suspend fun getTopicTags(): List<String> {
        return questionsRepository.getAllTopicTags()
    }
}
