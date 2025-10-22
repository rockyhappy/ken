package com.devrachit.ken.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.devrachit.ken.data.local.databases.KenDatabase
import com.devrachit.ken.data.local.entity.QuestionEntity
import com.devrachit.ken.data.local.entity.RemoteKeyEntity
import com.devrachit.ken.data.local.entity.toEntity
import com.devrachit.ken.domain.models.Question
import com.devrachit.ken.domain.models.QuestionSearchRequest
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.Resource
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class QuestionsRemoteMediator @Inject constructor(
    private val remoteRepository: LeetcodeRemoteRepository,
    private val database: KenDatabase,
    private val searchRequest: QuestionSearchRequest
) : RemoteMediator<Int, QuestionEntity>() {

    private val questionDao = database.questionDao()
    private val remoteKeyDao = database.remoteKeyDao()
    private val searchQuery = buildSearchQuery(searchRequest)

    override suspend fun initialize(): InitializeAction {
        // Check if we have cached data that's still fresh
        val cacheTimeout = 60 * 60 * 1000L // 1 hour in milliseconds
        val questionsCount = questionDao.getQuestionsCount(searchQuery)

        return if (questionsCount > 0) {
            // We have cached data, skip initial refresh
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // No cached data, launch initial refresh
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, QuestionEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 0
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

            Timber.d("Loading questions for page: $page, loadType: $loadType")

            // Create request with pagination
            val request = searchRequest.copy(
                offset = page * searchRequest.limit,
                limit = searchRequest.limit
            )

            // Fetch data from remote
            when (val response = remoteRepository.fetchQuestions(request)) {
                is Resource.Success -> {
                    val questions = response.data?.questions ?: emptyList()
                    val endOfPaginationReached = !(response.data?.hasNext ?: false)

                    database.withTransaction {
                        // Clear cache on refresh
                        if (loadType == LoadType.REFRESH) {
                            remoteKeyDao.clearRemoteKeys(searchQuery)
                            questionDao.clearQuestionsForSearch(searchQuery)
                        }

                        // Insert new data
                        val prevKey = if (page == 0) null else page - 1
                        val nextKey = if (endOfPaginationReached) null else page + 1

                        val keys = questions.map {
                            RemoteKeyEntity(
                                questionId = it.id,
                                prevKey = prevKey,
                                nextKey = nextKey,
                                searchQuery = searchQuery
                            )
                        }

                        val questionEntities = questions.map { it.toEntity(searchQuery) }

                        remoteKeyDao.insertAll(keys)
                        questionDao.insertQuestions(questionEntities)
                    }

                    MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
                }
                is Resource.Error -> {
                    Timber.e("Error loading questions: ${response.message}")
                    MediatorResult.Error(Exception(response.message ?: "Unknown error"))
                }
                is Resource.Loading -> {
                    // This case should not occur in this context as we're making a direct call
                    // But we need to handle it to make the when expression exhaustive
                    MediatorResult.Error(Exception("Unexpected loading state"))
                }
            }
        } catch (exception: Exception) {
            Timber.e(exception, "Exception in QuestionsRemoteMediator")
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, QuestionEntity>): RemoteKeyEntity? {
        return state.pages.lastOrNull()?.data?.lastOrNull()?.let { question ->
            remoteKeyDao.remoteKeysQuestionId(question.id, searchQuery)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, QuestionEntity>): RemoteKeyEntity? {
        return state.pages.firstOrNull()?.data?.firstOrNull()?.let { question ->
            remoteKeyDao.remoteKeysQuestionId(question.id, searchQuery)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, QuestionEntity>
    ): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { questionId ->
                remoteKeyDao.remoteKeysQuestionId(questionId, searchQuery)
            }
        }
    }

    private fun buildSearchQuery(request: QuestionSearchRequest): String {
        return "${request.query}_${request.difficulty}_${request.status}_${request.tags.joinToString(",")}"
    }
}
