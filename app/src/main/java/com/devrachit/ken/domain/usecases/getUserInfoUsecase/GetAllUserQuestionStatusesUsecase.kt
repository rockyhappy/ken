package com.devrachit.ken.domain.usecases.getUserInfoUsecase

import com.devrachit.ken.domain.models.toQuestionProgressUiState
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.presentation.screens.dashboard.home.QuestionProgressUiState
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetAllUserQuestionStatusesUsecase @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val cachePolicy: CachePolicy
) {
    operator fun invoke(): Flow<Resource<Map<String, QuestionProgressUiState>>> = flow {
        emit(Resource.Loading())

        try {
            val localData = localRepository.getAllUserQuestionStatuses()
            val mappedData = localData.associate { entity -> 
                entity.username to entity.toDomainModel().toQuestionProgressUiState() 
            }
            emit(Resource.Success(mappedData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }

    }
}
