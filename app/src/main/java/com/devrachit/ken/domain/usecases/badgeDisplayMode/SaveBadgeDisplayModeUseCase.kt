package com.devrachit.ken.domain.usecases.badgeDisplayMode

import com.devrachit.ken.data.local.datastore.DataStoreRepository
import javax.inject.Inject

class SaveBadgeDisplayModeUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(displayMode: String) {
        dataStoreRepository.saveBadgeDisplayMode(displayMode)
    }
}
