package com.devrachit.ken.domain.usecases.getUserBadges

import android.util.Log
import com.devrachit.ken.data.local.entity.UserBadgesEntity
import com.devrachit.ken.domain.models.UserBadgesResponse
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserBadgesUseCase @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val remoteRepository: LeetcodeRemoteRepository,
    private val cachePolicy: CachePolicy,
    private val networkManager: NetworkManager
) {
    operator fun invoke(
        username: String,
        forceRefresh: Boolean = false
    ): Flow<Resource<UserBadgesResponse>> = flow {
        emit(Resource.Loading())

        val isNetworkAvailable = networkManager.isConnected()
        Log.d("BadgesUseCase", "Network available: $isNetworkAvailable")
        
        // Try to get cached data if not forcing refresh or network unavailable
        if (!forceRefresh || !isNetworkAvailable) {
            val lastFetchTime = localRepository.getLastUserBadgesFetchTime(username)
            
            if (cachePolicy.isCacheValid(lastFetchTime) || !isNetworkAvailable) {
                val cachedData = localRepository.getUserBadges(username)
                if (cachedData is Resource.Success && cachedData.data != null) {
                    val userBadgesResponse = UserBadgesResponse(
                        data = com.devrachit.ken.domain.models.UserBadgesData(
                            matchedUser = cachedData.data.toDomainModel()
                        )
                    )
                    emit(Resource.Success(userBadgesResponse))
                    
                    // Return if network is unavailable
                    if (!isNetworkAvailable) {
                        return@flow
                    }
                }
            }
        }

        // Fetch from network if available
        if (isNetworkAvailable) {
            try {
                val networkResult = remoteRepository.fetchUserBadges(username)
                Log.d("BadgesUseCase", "API result: $networkResult")
                
                when (networkResult) {
                    is Resource.Success -> {
                        if (networkResult.data != null) {
                            // Save to local repository
                            val userBadges = networkResult.data.data?.matchedUser
                            if (userBadges != null) {
                                localRepository.saveUserBadges(
                                    username,
                                    UserBadgesEntity.fromDomainModel(
                                        username = username,
                                        domainModel = userBadges,
                                        cacheTimestamp = System.currentTimeMillis()
                                    )
                                )
                            }
                            emit(Resource.Success(networkResult.data))
                        } else {
                            emit(Resource.Error("Response data is null"))
                            // Try to return cached data as fallback
                            val cachedData = localRepository.getUserBadges(username)
                            if (cachedData is Resource.Success && cachedData.data != null) {
                                val userBadgesResponse = UserBadgesResponse(
                                    data = com.devrachit.ken.domain.models.UserBadgesData(
                                        matchedUser = cachedData.data.toDomainModel()
                                    )
                                )
                                emit(Resource.Success(userBadgesResponse))
                            }
                        }
                    }
                    is Resource.Error -> {
                        emit(Resource.Error(networkResult.message ?: "Unknown error"))
                        // Try to return cached data as fallback
                        val cachedData = localRepository.getUserBadges(username)
                        if (cachedData is Resource.Success && cachedData.data != null) {
                            val userBadgesResponse = UserBadgesResponse(
                                data = com.devrachit.ken.domain.models.UserBadgesData(
                                    matchedUser = cachedData.data.toDomainModel()
                                )
                            )
                            emit(Resource.Success(userBadgesResponse))
                        }
                    }
                    is Resource.Loading -> {
                        // This shouldn't happen, but just in case
                        val cachedData = localRepository.getUserBadges(username)
                        if (cachedData is Resource.Success && cachedData.data != null) {
                            val userBadgesResponse = UserBadgesResponse(
                                data = com.devrachit.ken.domain.models.UserBadgesData(
                                    matchedUser = cachedData.data.toDomainModel()
                                )
                            )
                            emit(Resource.Success(userBadgesResponse))
                        }
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error("Exception: ${e.message}"))
                // Try to return cached data as fallback
                val cachedData = localRepository.getUserBadges(username)
                if (cachedData is Resource.Success && cachedData.data != null) {
                    val userBadgesResponse = UserBadgesResponse(
                        data = com.devrachit.ken.domain.models.UserBadgesData(
                            matchedUser = cachedData.data.toDomainModel()
                        )
                    )
                    emit(Resource.Success(userBadgesResponse))
                }
            }
        } else {
            emit(Resource.Error("Network not available"))
            // Try to return cached data as fallback
            val cachedData = localRepository.getUserBadges(username)
            if (cachedData is Resource.Success && cachedData.data != null) {
                val userBadgesResponse = UserBadgesResponse(
                    data = com.devrachit.ken.domain.models.UserBadgesData(
                        matchedUser = cachedData.data.toDomainModel()
                    )
                )
                emit(Resource.Success(userBadgesResponse))
            }
        }
    }
}