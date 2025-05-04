package com.devrachit.ken.domain.usecases.getUserContestRanking

import android.util.Log
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserContestRankingUseCase  @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val remoteRepository: LeetcodeRemoteRepository,
    private val cachePolicy: CachePolicy,
    private val networkManager: NetworkManager) {
    operator fun invoke(username: String): Flow<Resource<Any?>> = flow {
        emit(Resource.Loading())
        try{
            val response = remoteRepository.fetchUserContestRanking(username)
            emit(Resource.Success(response))
            Log.d("GetUserContestRankingUseCase", "invoke: $response")
        }
        catch (e: Exception){
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }
}