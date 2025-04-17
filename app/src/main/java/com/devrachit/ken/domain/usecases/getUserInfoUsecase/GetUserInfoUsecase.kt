package com.devrachit.ken.domain.usecases.getUserInfoUsecase

import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val remoteRepository: LeetcodeRemoteRepository,
    private val cachePolicy: CachePolicy,
    private val networkManager: NetworkManager
) {
    operator fun invoke(
        username: String,
        forceRefresh: Boolean = false
    ): Flow<Resource<LeetCodeUserInfo>> = flow {
        // Start by emitting loading state
        emit(Resource.Loading())

        // Check network availability
        val isNetworkAvailable = networkManager.isConnected()

        // Try to get data from cache if we're not forcing a refresh OR if network is unavailable
        if (!forceRefresh || !isNetworkAvailable) {
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
        }

        // Only proceed with network call if network is available
        if (isNetworkAvailable) {
            // Fetch from network
            val networkResult = remoteRepository.fetchUserInfo(username)

            // Save successful response to cache
            if (networkResult is Resource.Success && networkResult.data?.username != null) {
                localRepository.saveUserInfo(networkResult.data)
            }

            // If network fetch failed but we have cache data, return that instead
            if (networkResult is Resource.Error) {
                var cacheData: Resource<LeetCodeUserInfo>? = null
                localRepository.getUserInfoFlow(username).firstOrNull()?.let { cacheResult ->
                    if (cacheResult is Resource.Success) {
                        cacheData = cacheResult
                    }
                }

                if (cacheData != null) {
                    emit(cacheData!!)
                    return@flow
                }
            }

            // Otherwise emit the network result (success or error)
            emit(networkResult)
        }
    }

    suspend fun getUserInfo(username: String): Resource<LeetCodeUserInfo> {
        return localRepository.getUserInfo(username)
    }
}