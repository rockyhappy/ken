package com.devrachit.ken.domain.usecases.getUserInfoUsecase

import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetAllUsersUsecase @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val cachePolicy: CachePolicy
) {
    operator fun invoke(): Flow<Resource<List<LeetCodeUserInfo>>> = flow {
        emit(Resource.Loading())

        try {
            val localData = localRepository.getAllUsers()
            emit(Resource.Success(localData.map { it.toDomainModel() }))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }

    }
}