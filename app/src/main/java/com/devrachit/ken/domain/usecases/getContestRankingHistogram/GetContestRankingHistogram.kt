package com.devrachit.ken.domain.usecases.getContestRankingHistogram

import android.util.Log
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.models.ContestRatingHistogramResponse
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetContestRankingHistogramUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val remoteRepository: LeetcodeRemoteRepository,
    private val cachePolicy: CachePolicy,
    private val networkManager: NetworkManager) {
        operator fun invoke(): Flow<Resource<ContestRatingHistogramResponse>> = flow {
            emit(Resource.Loading())
            try{
                val isNetworkAvailable = networkManager.isConnected()
                if (isNetworkAvailable) {
                    val response = remoteRepository.fetchContestRankingHistogram()
                    response.data?.let {
                        dataStoreRepository.saveContestRatingHistogram(it)
                        emit(Resource.Success(it))
                    }
                    if(response.data == null) {
                        val data =dataStoreRepository.readContestRatingHistogram()
                        if(data != null)
                        {
                            emit(Resource.Success(data))
                        }
                        else{
                            emit(Resource.Error("No data found"))
                        }
                    }

                }
                else{
                    val data =dataStoreRepository.readContestRatingHistogram()
                    if(data != null)
                    {
                        emit(Resource.Success(data))
                    }
                }
            }catch (e: Exception)
            {
                emit(Resource.Error(e.message ?: "An unexpected error occurred"))
            }
        }
}
