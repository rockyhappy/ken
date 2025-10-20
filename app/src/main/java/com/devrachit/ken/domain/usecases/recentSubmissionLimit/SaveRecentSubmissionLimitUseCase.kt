package com.devrachit.ken.domain.usecases.recentSubmissionLimit

import com.devrachit.ken.data.local.datastore.DataStoreRepository
import javax.inject.Inject

class SaveRecentSubmissionLimitUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(limit: Int) {
        dataStoreRepository.saveRecentSubmissionLimit(limit)
    }
}
