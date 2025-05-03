package com.devrachit.ken.domain.usecases.getUserProfileCalender

import android.util.Log
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.data.local.entity.UserProfileCalenderEntity
import com.devrachit.ken.domain.models.UserCalendar
import com.devrachit.ken.domain.models.UserProfileCalendarResponse
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserProfileCalenderUseCase @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val remoteRepository: LeetcodeRemoteRepository,
    private val cachePolicy: CachePolicy,
    private val dataStoreRepository: DataStoreRepository,
    private val networkManager: NetworkManager
) {
    operator fun invoke(
        username: String,
        forceRefresh: Boolean = false
    ): Flow<Resource<UserCalendar>> = flow {
        emit(Resource.Loading())

        val isNetworkAvailable = networkManager.isConnected()
        
        // Try to use cache if not forcing refresh or network is unavailable
        if (!forceRefresh || !isNetworkAvailable) {
            val lastFetchTime = localRepository.getLastUserProfileCalenderFetchTime(username)
            
            // Use cache if it's valid or if network is unavailable (even with force refresh)
            if (cachePolicy.isCacheValid(lastFetchTime) || !isNetworkAvailable) {
                val cachedData = localRepository.getUserProfileCalender(username)
                if (cachedData is Resource.Success && cachedData.data != null) {
                    emit(Resource.Success(cachedData.data.toDomainModel()))
                    // Only return if network is unavailable or cache is valid and not forcing refresh
//                    if (!isNetworkAvailable || (!forceRefresh && cachePolicy.isCacheValid(lastFetchTime))) {
//                        return@flow
//                    }
                    // if network is unavailable
                    if (!isNetworkAvailable) {
                        return@flow
                    }
                }
            }
        }

        if (isNetworkAvailable) {
            try {
                val networkResult = remoteRepository.fetchUserProfileCalender(username)
                when (networkResult) {
                    is Resource.Success -> {
                        if (networkResult.data != null) {
                            val userCalendarData = networkResult.data.data?.matchedUser?.userCalendar
                            if (userCalendarData != null) {
                                // Save to local repository
                                Log.d("GetUserProfileCalenderUseCase", "User calendar data fetched from network")
                                localRepository.saveUserProfileCalender(
                                    username,
                                    UserProfileCalenderEntity.fromDomainModel(
                                        username = username,
                                        domainModel = userCalendarData,
                                        cacheTimestamp = System.currentTimeMillis()
                                    )
                                )
                                emit(Resource.Success(userCalendarData))
                            } else {
                                emit(Resource.Error("User calendar data not found"))
                                val cachedData = localRepository.getUserProfileCalender(username)
                                if (cachedData is Resource.Success && cachedData.data != null) {
                                    emit(Resource.Success(cachedData.data.toDomainModel()))
                                }
                            }
                        } else {
                            emit(Resource.Error("Response data is null"))
                            val cachedData = localRepository.getUserProfileCalender(username)
                            if (cachedData is Resource.Success && cachedData.data != null) {
                                emit(Resource.Success(cachedData.data.toDomainModel()))
                            }
                        }
                    }
                    is Resource.Error -> {
                        emit(Resource.Error(networkResult.message ?: "Unknown error"))
                        val cachedData = localRepository.getUserProfileCalender(username)
                        if (cachedData is Resource.Success && cachedData.data != null) {
                            emit(Resource.Success(cachedData.data.toDomainModel()))
                        }
                    }
                    is Resource.Loading -> {
                        // This shouldn't happen, but just in case
                        val cachedData = localRepository.getUserProfileCalender(username)
                        if (cachedData is Resource.Success && cachedData.data != null) {
                            emit(Resource.Success(cachedData.data.toDomainModel()))
                        }
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error("Exception: ${e.message}"))
                val cachedData = localRepository.getUserProfileCalender(username)
                if (cachedData is Resource.Success && cachedData.data != null) {
                    emit(Resource.Success(cachedData.data.toDomainModel()))
                }
            }
        } else {
            emit(Resource.Error("Network not available"))
            val cachedData = localRepository.getUserProfileCalender(username)
            if (cachedData is Resource.Success && cachedData.data != null) {
                emit(Resource.Success(cachedData.data.toDomainModel()))
            }
        }
    }
}