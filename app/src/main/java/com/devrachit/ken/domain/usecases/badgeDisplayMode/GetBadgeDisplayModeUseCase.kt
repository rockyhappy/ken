package com.devrachit.ken.domain.usecases.badgeDisplayMode

import com.devrachit.ken.data.local.datastore.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBadgeDisplayModeUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(): Flow<String?> {
        return dataStoreRepository.badgeDisplayMode
    }
    
    suspend fun readBadgeDisplayMode(): String {
        return dataStoreRepository.readBadgeDisplayMode()
    }
}
