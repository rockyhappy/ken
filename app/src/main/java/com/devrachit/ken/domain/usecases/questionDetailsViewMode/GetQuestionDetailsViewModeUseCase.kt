package com.devrachit.ken.domain.usecases.questionDetailsViewMode

import com.devrachit.ken.data.local.datastore.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionDetailsViewModeUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(): Flow<String?> {
        return dataStoreRepository.questionDetailsViewMode
    }
    
    suspend fun readQuestionDetailsViewMode(): String {
        return dataStoreRepository.readQuestionDetailsViewMode()
    }
}
