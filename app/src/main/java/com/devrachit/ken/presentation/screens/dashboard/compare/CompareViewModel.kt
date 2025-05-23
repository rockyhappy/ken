package com.devrachit.ken.presentation.screens.dashboard.compare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetAllUsersUsecase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoUseCase
import com.devrachit.ken.domain.usecases.getUserQuestionStatus.GetUserQuestionStatusUseCase
import com.devrachit.ken.utility.NetworkUtility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompareViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val getUserQuestionStatusUseCase: GetUserQuestionStatusUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getAllUsersUsecase: GetAllUsersUsecase
): ViewModel(){

    private val _userStatesValues =MutableStateFlow(CompareUiStates())
    val userStatesValues :StateFlow<CompareUiStates> = _userStatesValues.asStateFlow()

    private val _loadingStatesValues = MutableStateFlow(LoadingStates())
    val loadingStatesValues :StateFlow<LoadingStates> = _loadingStatesValues.asStateFlow()


    fun loadAllUsersInfo(){
        viewModelScope.launch {
            coroutineScope {
                launch(Dispatchers.IO){getAllUsersInfo()}
            }

        }
    }

    suspend fun updateLoadingState(){
        _userStatesValues.value = _userStatesValues.value.copy(
            isLoading = _loadingStatesValues.value.isLoadingUserList
        )
    }
    suspend fun getAllUsersInfo(){
        getAllUsersUsecase().collectLatest{
            when(it){
                is Resource.Error-> {
                    _loadingStatesValues.value.isLoadingUserList=false
                    updateLoadingState()
                    getAllUsersInfo()
                }
                is Resource.Loading -> {
                    _loadingStatesValues.value.isLoadingUserList=true
                    updateLoadingState()
                }
                is Resource.Success -> {
                    val data = it.data
                    if ( data != null) {
                        _userStatesValues.value = _userStatesValues.value.copy(
                            friendsDetails =data.associateBy { user -> user.username ?: "" },
                        )
                    }
                    _loadingStatesValues.value.isLoadingUserList=false
                    updateLoadingState()
                }
            }
        }
    }

}