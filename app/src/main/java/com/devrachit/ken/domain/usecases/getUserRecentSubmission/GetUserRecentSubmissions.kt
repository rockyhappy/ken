package com.devrachit.ken.domain.usecases.getUserRecentSubmission

import android.util.Log
import com.devrachit.ken.data.local.entity.UserRecentSubmissionEntity
import com.devrachit.ken.domain.models.UserRecentAcSubmissionResponse
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetUserRecentSubmissionUseCase @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val remoteRepository: LeetcodeRemoteRepository,
    private val cachePolicy: CachePolicy,
    private val networkManager: NetworkManager
) {
    operator fun invoke(
        username: String,
        forceRefresh: Boolean = false, // Default forceRefresh to false
        limit: Int? = null
    ): Flow<Resource<UserRecentAcSubmissionResponse>> = flow {
        emit(Resource.Loading())

        val isNetworkAvailable = networkManager.isConnected()

        // Try to use cache if not forcing refresh or network is unavailable
        if (!forceRefresh || !isNetworkAvailable) {
            // Correctly use the fetch time specific to recent submissions
            val lastFetchTime = localRepository.getLastRecentSubmissionsFetchTime(username)

            // Use cache if it's valid or if network is unavailable (even with force refresh)
            if (cachePolicy.isCacheValid(lastFetchTime) || !isNetworkAvailable) {
                val cachedResult = localRepository.getRecentSubmissions(username)
                if (cachedResult is Resource.Success && cachedResult.data != null) {
                    emit(Resource.Success(cachedResult.data.toDomainModel()))
                    // if network is unavailable, return after emitting cache
                    if (!isNetworkAvailable) {
                        return@flow
                    }
                }
            }
        }

        if (isNetworkAvailable) {
            try {
                val networkResult = remoteRepository.fetchUserRecentAcSubmissions(username, limit)
                when (networkResult) {
                    is Resource.Success -> {
                        if (networkResult.data != null) {
                            // Save to local repository
                            Log.d("GetUserRecentSubmissions", "Recent submissions fetched from network")
                            localRepository.saveRecentSubmissions(
                                username,
                                UserRecentSubmissionEntity.fromDomainModel(
                                    username = username,
                                    domainModel = networkResult.data,
                                    cacheTimestamp = System.currentTimeMillis()
                                )
                            )
                            emit(Resource.Success(networkResult.data))
                        } else {
                            emit(Resource.Error("Response data is null"))
                            // Fallback to cache on error
                            emitCachedDataOnError(username)
                        }
                    }
                    is Resource.Error -> {
                        emit(Resource.Error(networkResult.message ?: "Unknown error"))
                        // Fallback to cache on error
                        emitCachedDataOnError(username)
                    }
                    is Resource.Loading -> {
                        // Loading state handled initially, but if it somehow occurs here, fallback
                         emitCachedDataOnError(username)
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error("Exception: ${e.message}"))
                // Fallback to cache on exception
                emitCachedDataOnError(username)
            }
        } else {
            // Network was not available, and cache logic at the start might have failed or was skipped due to forceRefresh=true
            // We already emitted cached data if available and returned if network is unavailable,
            // but as a final fallback if we reach here without network:
             emit(Resource.Error("Network not available"))
             emitCachedDataOnError(username) // Try emitting cache one last time
        }
    }

    // Helper function to emit cached data if available during error/exception handling
    private suspend fun FlowCollector<Resource<UserRecentAcSubmissionResponse>>.emitCachedDataOnError(username: String) {
        val cachedResult = localRepository.getRecentSubmissions(username)
        if (cachedResult is Resource.Success && cachedResult.data != null) {
            emit(Resource.Success(cachedResult.data.toDomainModel()))
        }
    }
}