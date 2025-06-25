package com.devrachit.ken.domain.usecases.getQuestionsUseCase

import Question
import android.util.Log
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
private const val TAG = "GET_QUESTIONS_USE_CASE"
class GetQuestionsUseCase @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val remoteRepository: LeetcodeRemoteRepository,
    private val cachePolicy: CachePolicy,
    private val networkManager: NetworkManager
) {
    operator fun invoke(
        limit: Int,
        skip: Int,
        forceRefresh: Boolean = true
    ): Flow<Resource<List<Question?>>> = flow {
        // Start by emitting loading state
        emit(Resource.Loading())

        // Check network availability
        val isNetworkAvailable = networkManager.isConnected()

        // Try to get data from cache if we're not forcing a refresh OR if network is unavailable
        /*if (!forceRefresh || !isNetworkAvailable) {
            val lastFetchTime = localRepository.getLastFetchTime(username)

            // Use cache if it's valid OR if network is unavailable (regardless of cache validity)
            if (cachePolicy.isCacheValid(lastFetchTime) || !isNetworkAvailable) {
                // Emit cached data if available
                localRepository.getUserInfoFlow(username).collect { cacheResult ->
                    if (cacheResult is Resource.Success) {
                        emit(cacheResult)
                        // If no network is available, return cached data and don't attempt network call
                        if (!isNetworkAvailable) {
                            return@collect
                        }
                    }
                }
            }
        }*/

        // Only proceed with network call if network is available
        if (isNetworkAvailable) {
            // Fetch from network
            val networkResult = remoteRepository.fetchQuestions(limit=limit, skip = skip)
            Log.d(TAG, "invoke: ${networkResult.data}")
            // Save successful response to cache
            if (networkResult is Resource.Success && networkResult.data != null) {
                //localRepository.saveUserInfo(networkResult.data)
            }

            // If network fetch failed but we have cache data, return that instead
            if (networkResult is Resource.Error) {
                Timber.tag(TAG).d("Error: ${networkResult.message}")
                /*var cacheData: Resource<LeetCodeUserInfo>? = null
                localRepository.getUserInfoFlow(username).firstOrNull()?.let { cacheResult ->
                    if (cacheResult is Resource.Success) {
                        cacheData = cacheResult
                    }
                }

                if (cacheData != null) {
                    emit(cacheData!!)
                    return@flow
                }*/
            }

            // Otherwise emit the network result (success or error)
            networkResult.data?.data?.problemsetQuestionListV2?.questions?.let { it ->
                emit(Resource.Success(it))
            }
            //emit(Resource.Error("No questions found"))

        }
    }
}