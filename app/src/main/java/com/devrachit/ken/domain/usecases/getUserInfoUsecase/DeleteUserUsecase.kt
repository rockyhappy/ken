package com.devrachit.ken.domain.usecases.getUserInfoUsecase

import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DeleteUserUsecase @Inject constructor(
    private val localRepository: LeetcodeLocalRepository
) {
    operator fun invoke(username: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())

        try {
            localRepository.deleteUser(username)
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to delete user"))
        }
    }
}