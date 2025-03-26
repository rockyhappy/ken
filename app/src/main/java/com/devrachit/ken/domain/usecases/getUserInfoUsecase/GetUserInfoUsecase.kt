package com.devrachit.ken.domain.usecases.getUserInfoUsecase

import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val remoteRepository: LeetcodeRemoteRepository,
    private val cachePolicy: CachePolicy
) {
    operator fun invoke(username: String, forceRefresh: Boolean = false): Flow<Resource<LeetCodeUserInfo>> = flow {
        // Start by emitting loading state
        emit(Resource.Loading())
        
        // First try to get data from cache if we're not forcing a refresh
        if (!forceRefresh) {
            val lastFetchTime = localRepository.getLastFetchTime(username)
            
            if (cachePolicy.isCacheValid(lastFetchTime)) {
                // Emit cached data if available and valid
                localRepository.getUserInfoFlow(username).collect { cacheResult ->
                    if (cacheResult is Resource.Success) {
                        emit(cacheResult)
                        // Can return here if we only want cached data
//                         return@flow
                    }
                }
            }
        }
        
        // Either cache is invalid/missing or we need fresh data, so fetch from network
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