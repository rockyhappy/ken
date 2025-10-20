package com.devrachit.ken.domain.usecases.questionDetailsViewMode

import com.devrachit.ken.data.local.datastore.DataStoreRepository
import javax.inject.Inject

class SaveQuestionDetailsViewModeUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(viewMode: String) {
        dataStoreRepository.saveQuestionDetailsViewMode(viewMode)
    }
}
