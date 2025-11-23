package com.devrachit.ken.domain.usecases.getDailyCodingChallenge

import com.devrachit.ken.domain.models.DailyCodingChallengeResponse
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDailyCodingChallengeUseCase @Inject constructor(
    private val remoteRepository: LeetcodeRemoteRepository,
    private val networkManager: NetworkManager
) {
    operator fun invoke(year: Int, month: Int): Flow<Resource<DailyCodingChallengeResponse>> = flow {
        emit(Resource.Loading())
        
        val isNetworkAvailable = networkManager.isConnected()
        if (!isNetworkAvailable) {
            emit(Resource.Error("No internet connection"))
            return@flow
        }
        
        try {
            val result = remoteRepository.fetchDailyCodingChallenge(year, month)
            emit(result)
        } catch (e: Exception) {
            emit(Resource.Error("Error fetching daily challenges: ${e.message}"))
        }
    }
}
