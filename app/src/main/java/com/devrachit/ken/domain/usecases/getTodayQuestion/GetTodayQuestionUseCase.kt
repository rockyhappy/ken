package com.devrachit.ken.domain.usecases.getTodayQuestion

import com.devrachit.ken.domain.models.TodayQuestionResponse
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTodayQuestionUseCase @Inject constructor(
    private val remoteRepository: LeetcodeRemoteRepository,
    private val networkManager: NetworkManager
) {
    operator fun invoke(): Flow<Resource<TodayQuestionResponse>> = flow {
        emit(Resource.Loading())
        
        val isNetworkAvailable = networkManager.isConnected()
        if (!isNetworkAvailable) {
            emit(Resource.Error("No internet connection"))
            return@flow
        }
        
        try {
            val result = remoteRepository.fetchTodayQuestion()
            emit(result)
        } catch (e: Exception) {
            emit(Resource.Error("Error fetching today's question: ${e.message}"))
        }
    }
}
