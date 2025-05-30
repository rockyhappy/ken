package com.devrachit.ken.domain.usecases.getUserInfoUsecase

import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import com.devrachit.ken.utility.constants.Constants
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetUserInfoNoCacheUseCase @Inject constructor(
    private val remoteRepository: LeetcodeRemoteRepository,
    private val networkManager: NetworkManager
) {
    operator fun invoke(username: String): Flow<Resource<LeetCodeUserInfo>> = flow {
        // Start by emitting loading state
        emit(Resource.Loading())

        // Check network availability
        val isNetworkAvailable = networkManager.isConnected()

        if (!isNetworkAvailable) {
            // Return error with network unavailable key if no network
            emit(Resource.Error(Constants.NETWORK_UNAVAILABLE_ERROR))
            return@flow
        }

        // Fetch from network without caching
        val networkResult = remoteRepository.fetchUserInfo(username)
        
        // Emit the network result (success or error)
        emit(networkResult)
    }
}