package com.devrachit.ken.domain.usecases.getUserProfileCalender

import com.devrachit.ken.data.local.datastore.DataStoreRepository
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
    ):Flow<Resource<Any?>>  = flow{
        emit(Resource.Loading())

        val isNetworkAvailable = networkManager.isConnected()
        if(!forceRefresh || !isNetworkAvailable)
        {
            // this is the condition when
            // 1. forceRefresh is false
            // 2. forceRefresh is true but no network available
        }
        if(isNetworkAvailable)
        {
            val networkResult = remoteRepository.fetchUserProfileCalender(username)
            if(networkResult is Resource.Success && networkResult.data!=null){
                println("Network Result: ${networkResult.data}")
                emit(Resource.Success(networkResult.data))
            }
        }
    }
}