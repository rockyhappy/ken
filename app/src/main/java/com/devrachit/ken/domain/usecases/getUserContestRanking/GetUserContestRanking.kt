package com.devrachit.ken.domain.usecases.getUserContestRanking

import android.util.Log
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.data.local.entity.UserContestRankingEntity
import com.devrachit.ken.domain.models.UserContestRankingResponse
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserContestRankingUseCase @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val remoteRepository: LeetcodeRemoteRepository,
    private val cachePolicy: CachePolicy,
    private val networkManager: NetworkManager
) {
    operator fun invoke(
        username: String,
        forceRefresh: Boolean = false
    ): Flow<Resource<UserContestRankingResponse>> = flow {
        emit(Resource.Loading())

        val isNetworkAvailable = networkManager.isConnected()
        Log.d("RankingUseCase", "Network available: $isNetworkAvailable")
        if (!forceRefresh || !isNetworkAvailable) {
            val lastFetchTime = localRepository.getLastUserContestRankingFetchTime(username)
            
            
            if (cachePolicy.isCacheValid(lastFetchTime) || !isNetworkAvailable) {
                val cachedData = localRepository.getUserContestRanking(username)
                if (cachedData is Resource.Success && cachedData.data != null) {
                    emit(Resource.Success(cachedData.data.toDomainModel()))
                    // Return if network is unavailable
                    if (!isNetworkAvailable) {
                        return@flow
                    }
                }
            }
        }

        if (isNetworkAvailable) {
            try {
                val networkResult = remoteRepository.fetchUserContestRanking(username)
                Log.d("RankingfUseCase", "Api result: $networkResult")
                when (networkResult) {
                    is Resource.Success -> {
                        if (networkResult.data != null) {
                            val contestRankingData = networkResult.data.data.userContestRanking
                            // Save to local repository
                            Log.d("Special ", networkResult.data.toString())
//                            Log.d("GetUserContestRankingUseCase", "Contest ranking data fetched from network")
                            localRepository.saveUserContestRanking(
                                username,
                                UserContestRankingEntity.fromDomainModel(
                                    username = username,
                                    response = networkResult.data,
                                    cacheTimestamp = System.currentTimeMillis()
                                )
                            )
                            emit(Resource.Success(networkResult.data))
                        } else {
                            emit(Resource.Error("Response data is null"))
                            val cachedData = localRepository.getUserContestRanking(username)
                            if (cachedData is Resource.Success && cachedData.data != null) {
                                emit(Resource.Success(cachedData.data.toDomainModel()))
                            }
                        }
                    }
                    is Resource.Error -> {
                        emit(Resource.Error(networkResult.message ?: "Unknown error"))
                        val cachedData = localRepository.getUserContestRanking(username)
                        if (cachedData is Resource.Success && cachedData.data != null) {
                            emit(Resource.Success(cachedData.data.toDomainModel()))
                        }
                    }
                    is Resource.Loading -> {
                        // This shouldn't happen, but just in case
                        val cachedData = localRepository.getUserContestRanking(username)
                        if (cachedData is Resource.Success && cachedData.data != null) {
                            emit(Resource.Success(cachedData.data.toDomainModel()))
                        }
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error("Exception: ${e.message}"))
                val cachedData = localRepository.getUserContestRanking(username)
                if (cachedData is Resource.Success && cachedData.data != null) {
                    emit(Resource.Success(cachedData.data.toDomainModel()))
                }
            }
        } else {
            emit(Resource.Error("Network not available"))
            val cachedData = localRepository.getUserContestRanking(username)
            if (cachedData is Resource.Success && cachedData.data != null) {
                emit(Resource.Success(cachedData.data.toDomainModel()))
            }
        }
    }
}