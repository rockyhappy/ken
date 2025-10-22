package com.devrachit.ken.data.repository.questions

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.devrachit.ken.data.local.databases.KenDatabase
import com.devrachit.ken.data.local.entity.toDomain
import com.devrachit.ken.data.paging.QuestionsRemoteMediator
import com.devrachit.ken.domain.models.Question
import com.devrachit.ken.domain.models.QuestionSearchRequest
import com.devrachit.ken.domain.repository.questions.QuestionsRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionsRepositoryImpl @Inject constructor(
    private val database: KenDatabase,
    private val remoteRepository: LeetcodeRemoteRepository
) : QuestionsRepository {

    private val questionDao = database.questionDao()
    private val remoteKeyDao = database.remoteKeyDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getQuestionsPagingFlow(searchRequest: QuestionSearchRequest): Flow<PagingData<Question>> {
        val searchQuery = buildSearchQuery(searchRequest)

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 20,
                enablePlaceholders = true,
                initialLoadSize = 40,
                maxSize = 200,
                jumpThreshold = 120
            ),
            remoteMediator = QuestionsRemoteMediator(
                remoteRepository = remoteRepository,
                database = database,
                searchRequest = searchRequest
            ),
            pagingSourceFactory = {
                questionDao.getQuestionsPagingSource(
                    searchQuery = searchQuery,
                    query = searchRequest.query.takeIf { it.isNotEmpty() },
                    difficulty = searchRequest.difficulty,
                    status = searchRequest.status
                )
            }
        ).flow.map { pagingData ->
            pagingData.map { questionEntity ->
                questionEntity.toDomain()
            }
        }
    }

    override suspend fun searchQuestions(query: String, limit: Int): List<Question> {
        return questionDao.searchQuestions(query, limit).map { it.toDomain() }
    }

    override suspend fun updateQuestionStatus(questionId: Int, status: String) {
        questionDao.updateQuestionStatus(questionId, status)
    }

    override suspend fun getQuestionById(questionId: Int): Question? {
        return questionDao.getQuestionById(questionId)?.toDomain()
    }

    override suspend fun getAllDifficulties(): List<String> {
        return questionDao.getAllDifficulties()
    }

    override suspend fun getAllTopicTags(): List<String> {
        val rawTags = questionDao.getAllTopicTagsRaw()
        val allTags = mutableSetOf<String>()

        rawTags.forEach { jsonString ->
            try {
                val tags: List<String> = Json.decodeFromString(jsonString)
                allTags.addAll(tags)
            } catch (e: Exception) {
                // Ignore invalid JSON
            }
        }

        return allTags.sorted()
    }

    override suspend fun clearCache() {
        questionDao.clearAllQuestions()
        remoteKeyDao.clearAllRemoteKeys()
    }

    private fun buildSearchQuery(request: QuestionSearchRequest): String {
        return "${request.query}_${request.difficulty}_${request.status}_${request.tags.joinToString(",")}"
    }
}
