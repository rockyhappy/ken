package com.devrachit.ken.domain.usecases.getQuestionDetails

import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.NetworkManager
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetQuestionDetailsUseCase @Inject constructor(
    private val remoteRepository: LeetcodeRemoteRepository,
    private val networkManager: NetworkManager
) {
    operator fun invoke(questionSlug: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())

        if (!networkManager.isConnected()) {
            emit(Resource.Error("No internet connection"))
            return@flow
        }

        try {
            val result = remoteRepository.fetchQuestionDetails(questionSlug)
            emit(result)
        } catch (e: Exception) {
            emit(Resource.Error("Error: ${e.message}"))
        }
    }
}
