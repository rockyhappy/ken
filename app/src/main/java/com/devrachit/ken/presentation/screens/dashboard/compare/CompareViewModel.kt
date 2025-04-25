package com.devrachit.ken.presentation.screens.dashboard.compare

import androidx.lifecycle.ViewModel
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoUseCase
import com.devrachit.ken.domain.usecases.getUserQuestionStatus.GetUserQuestionStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CompareViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val getUserQuestionStatusUseCase: GetUserQuestionStatusUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
): ViewModel(){

    private val _userStatesValues =MutableStateFlow(CompareUiStates())
    val userStatesValues :StateFlow<CompareUiStates> = _userStatesValues.asStateFlow()

}