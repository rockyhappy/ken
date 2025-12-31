package com.devrachit.ken.domain.usecases.navigationPreferences

import com.devrachit.ken.data.local.datastore.DataStoreRepository
import javax.inject.Inject

class SaveSideNavItemsUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(items: List<String>) {
        dataStoreRepository.saveSideNavItems(items)
    }
}
