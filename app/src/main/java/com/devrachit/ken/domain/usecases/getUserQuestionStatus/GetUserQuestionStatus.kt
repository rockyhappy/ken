package com.devrachit.ken.domain.usecases.getUserQuestionStatus

import com.devrachit.ken.data.local.entity.UserQuestionStatusEntity
import com.devrachit.ken.domain.models.UserQuestionStatusData
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserQuestionStatusUseCase @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val remoteRepository: LeetcodeRemoteRepository,
    private val cachePolicy: CachePolicy,
    private val networkManager: NetworkManager
) {
    operator fun invoke(
        username: String,
        forceRefresh: Boolean = false
    ): Flow<Resource<UserQuestionStatusData>> = flow {

        emit(Resource.Loading())

        val isNetworkAvailable = networkManager.isConnected()
        if (!forceRefresh || !isNetworkAvailable) {
            val lastFetchTime = localRepository.getLastUserQuestionStatusFetchTime(username)

            if (cachePolicy.isCacheValid(lastFetchTime) || isNetworkAvailable) {
                val data = localRepository.getUserQuestionStatus(username)
                if (data is Resource.Success) {
                    emit(data)
                    if (!isNetworkAvailable) {
                        return@flow
                    }
                }
            }
        }

        if (isNetworkAvailable) {
            val networkResult = remoteRepository.fetchUserRankingInfo(username)
            if (networkResult is Resource.Success && networkResult.data != null) {
                localRepository.saveUserQuestionStatus(
                    UserQuestionStatusEntity.fromDomainModel(
                        username = username,
                        domainModel = networkResult.data,
                        cacheTimestamp = System.currentTimeMillis()
                    )
                )
                emit(networkResult)
            }
            else if(networkResult is Resource.Error){
                val data = localRepository.getUserQuestionStatus(username)
                if (data is Resource.Success) {
                    emit(data)
                    return@flow
                }
            }
            emit(networkResult)
        }

    }
}