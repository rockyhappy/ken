package com.devrachit.ken.domain.usecases.navigationPreferences

import com.devrachit.ken.data.local.datastore.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBottomNavItemsUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(): Flow<List<String>> {
        return dataStoreRepository.bottomNavItems
    }
}
