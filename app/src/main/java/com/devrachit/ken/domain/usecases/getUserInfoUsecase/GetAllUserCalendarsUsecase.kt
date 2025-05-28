package com.devrachit.ken.domain.usecases.getUserInfoUsecase

import com.devrachit.ken.data.local.entity.UserProfileCalenderEntity
import com.devrachit.ken.domain.models.UserCalendar
import com.devrachit.ken.domain.policy.CachePolicy
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class GetAllUserCalendarsUsecase @Inject constructor(
    private val localRepository: LeetcodeLocalRepository,
    private val cachePolicy: CachePolicy
) {
    operator fun invoke(): Flow<Resource<Map<String, UserCalendar>>> = flow {
        emit(Resource.Loading())

        try {
            val localData = localRepository.getAllUserCalendars()
            val mappedData = localData.associate { entity -> 
                entity.username to entity.toDomainModel() 
            }
            emit(Resource.Success(mappedData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }

    }
}
