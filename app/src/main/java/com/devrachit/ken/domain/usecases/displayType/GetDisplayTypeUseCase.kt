package com.devrachit.ken.domain.usecases.displayType

import com.devrachit.ken.data.local.datastore.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDisplayTypeUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(): Flow<String?> {
        return dataStoreRepository.friendsViewMode
    }
    
    suspend fun readDisplayType(): String {
        return dataStoreRepository.readFriendsViewMode()
    }
}
