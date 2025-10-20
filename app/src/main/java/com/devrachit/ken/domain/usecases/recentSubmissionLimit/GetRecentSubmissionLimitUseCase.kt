package com.devrachit.ken.domain.usecases.recentSubmissionLimit

import com.devrachit.ken.data.local.datastore.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentSubmissionLimitUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(): Flow<Int> {
        return dataStoreRepository.recentSubmissionLimit
    }
    
    suspend fun readRecentSubmissionLimit(): Int {
        return dataStoreRepository.readRecentSubmissionLimit()
    }
}
