package com.devrachit.ken.domain.usecases.getCurrentTime

import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.models.CurrentTimeData
import com.devrachit.ken.domain.models.CurrentTimeResponse
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import kotlinx.coroutines.flow.*
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import javax.inject.Inject

class GetCurrentTime @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val remoteRepository: LeetcodeRemoteRepository,
    private val cachePolicy: CachePolicy,
    private val dataStoreRepository: DataStoreRepository,
    private val networkManager: NetworkManager
) {
    operator fun invoke(): Flow<Resource<CurrentTimeResponse>> = flow {

        emit(Resource.Loading())
        val isNetworkAvailable = networkManager.isConnected()
        if (isNetworkAvailable) {
            val currentTime = remoteRepository.fetchCurrentData()
            println(currentTime)
            dataStoreRepository.savePrimaryTime(currentTime.data?.data?.currentTimestamp.toString())
            emit(currentTime)
        } else {
            val currentTime = dataStoreRepository.readPrimaryTime()
            if(currentTime!=null)
            emit(Resource.Success(CurrentTimeResponse(
                data = CurrentTimeData(currentTimestamp = currentTime.toDouble()))))
            else
                emit(Resource.Error("No internet connection"))
        }

    }


}