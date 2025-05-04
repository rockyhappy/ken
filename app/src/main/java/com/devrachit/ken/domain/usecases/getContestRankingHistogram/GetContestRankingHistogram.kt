package com.devrachit.ken.domain.usecases.getContestRankingHistogram

import android.util.Log
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetContestRankingHistogramUseCase @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val remoteRepository: LeetcodeRemoteRepository,
    private val cachePolicy: CachePolicy,
    private val networkManager: NetworkManager) {
        operator fun invoke(): Flow<Resource<Any?>> = flow {
            emit(Resource.Loading())
            try{
                val response= remoteRepository.fetchContestRankingHistogram()
                Log.d("TAG", "invoke: $response")
            }catch (e: Exception)
            {

            }
        }
}