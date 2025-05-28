package com.devrachit.ken.presentation.screens.dashboard.compare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.usecases.getCurrentTime.GetCurrentTime
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetAllUsersUsecase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetAllUserQuestionStatusesUsecase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetAllUserCalendarsUsecase
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
    private val getAllUsersUsecase: GetAllUsersUsecase,
    private val getAllUserQuestionStatusesUsecase: GetAllUserQuestionStatusesUsecase,
    private val getAllUserCalendarsUsecase: GetAllUserCalendarsUsecase,
    private val getCurrentTime: GetCurrentTime,
): ViewModel(){

    private val _userStatesValues =MutableStateFlow(CompareUiStates())
    val userStatesValues :StateFlow<CompareUiStates> = _userStatesValues.asStateFlow()

    private val _loadingStatesValues = MutableStateFlow(LoadingStates())
    val loadingStatesValues :StateFlow<LoadingStates> = _loadingStatesValues.asStateFlow()


    fun loadAllUsersInfo(){
        viewModelScope.launch {
            coroutineScope {
                launch(Dispatchers.IO){getAllUsersInfo()}
                launch(Dispatchers.IO){getAllUserQuestionStatuses()}
                launch(Dispatchers.IO){getAllUserCalendars()}
                launch(Dispatchers.IO){fetchCurrentTime()}
            }

        }
    }

    suspend fun updateLoadingState(){
        _userStatesValues.value = _userStatesValues.value.copy(
            isLoading = _loadingStatesValues.value.isLoadingUserList || 
                       _loadingStatesValues.value.isLoadingUserQuestionStatuses ||
                       _loadingStatesValues.value.isLoadingUserCalendars
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

    suspend fun getAllUserQuestionStatuses(){
        getAllUserQuestionStatusesUsecase().collectLatest{
            when(it){
                is Resource.Error-> {
                    _loadingStatesValues.value.isLoadingUserQuestionStatuses=false
                    updateLoadingState()
                    getAllUserQuestionStatuses()
                }
                is Resource.Loading -> {
                    _loadingStatesValues.value.isLoadingUserQuestionStatuses=true
                    updateLoadingState()
                }
                is Resource.Success -> {
                    val data = it.data
                    if ( data != null) {
                        _userStatesValues.value = _userStatesValues.value.copy(
                            friendsQuestionProgressInfo = data
                        )
                    }
                    _loadingStatesValues.value.isLoadingUserQuestionStatuses=false
                    updateLoadingState()
                }
            }
        }
    }

    suspend fun getAllUserCalendars(){
        getAllUserCalendarsUsecase().collectLatest{
            when(it){
                is Resource.Error-> {
                    _loadingStatesValues.value.isLoadingUserCalendars=false
                    updateLoadingState()
                    getAllUserCalendars()
                }
                is Resource.Loading -> {
                    _loadingStatesValues.value.isLoadingUserCalendars=true
                    updateLoadingState()
                }
                is Resource.Success -> {
                    val data = it.data
                    if ( data != null) {
                        _userStatesValues.value = _userStatesValues.value.copy(
                            userProfileCalender = data
                        )
                    }
                    _loadingStatesValues.value.isLoadingUserCalendars=false
                    updateLoadingState()
                }
            }
        }
    }
    private suspend fun fetchCurrentTime() {
        _loadingStatesValues.value.currentTimeLoading = true
        updateLoadingState()

        getCurrentTime().collectLatest {
            when (it) {
                is Resource.Loading -> {
                    _loadingStatesValues.value.currentTimeLoading = true
                    updateLoadingState()
                }

                is Resource.Success -> {
                    _userStatesValues.value = _userStatesValues.value.copy(
                        currentTimestamp = it.data?.data?.currentTimestamp
                    )
                    _loadingStatesValues.value.currentTimeLoading = false
                    updateLoadingState()
                }

                is Resource.Error -> {
                    _loadingStatesValues.value.currentTimeLoading = false
                    updateLoadingState()
                    fetchCurrentTime()
                }
            }
        }
    }

}
