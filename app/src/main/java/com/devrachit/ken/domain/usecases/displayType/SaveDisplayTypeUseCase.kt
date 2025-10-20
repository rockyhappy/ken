package com.devrachit.ken.domain.usecases.displayType

import com.devrachit.ken.data.local.datastore.DataStoreRepository
import javax.inject.Inject

class SaveDisplayTypeUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(displayType: String) {
        dataStoreRepository.saveFriendsViewMode(displayType)
    }
}
