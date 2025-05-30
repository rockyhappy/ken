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
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
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

    private var searchJob: Job? = null

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

    fun updateSearchQuery(query: String) {
        _userStatesValues.value = _userStatesValues.value.copy(
            searchQuery = query,
            showSearchSuggestions = query.isNotEmpty()
        )
        
        // Cancel previous search job
        searchJob?.cancel()
        
        if (query.isEmpty()) {
            _userStatesValues.value = _userStatesValues.value.copy(
                searchResults = emptyMap(),
                showSearchSuggestions = false,
                isSearching = false
            )
            return
        }
        
        // Start new search with debouncing
        searchJob = viewModelScope.launch {
            delay(300) // Debounce for 300ms
            performSearch(query)
        }
    }
    
    private suspend fun performSearch(query: String) {
        _userStatesValues.value = _userStatesValues.value.copy(isSearching = true)
        
        withContext(Dispatchers.Default) {
            try {
                val friendsDetails = _userStatesValues.value.friendsDetails
                if (friendsDetails.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        _userStatesValues.value = _userStatesValues.value.copy(
                            searchResults = emptyMap(),
                            isSearching = false
                        )
                    }
                    return@withContext
                }
                
                val searchQuery = query.lowercase().trim()
                val filteredResults = friendsDetails.filter { (username, userInfo) ->
                    // Search in username
                    val usernameMatch = userInfo.username?.lowercase()?.contains(searchQuery) == true
                    // Search in real name
                    val realNameMatch = userInfo.profile?.realName?.lowercase()?.contains(searchQuery) == true
                    // Search in company
                    val companyMatch = userInfo.profile?.company?.lowercase()?.contains(searchQuery) == true
                    
                    usernameMatch || realNameMatch || companyMatch
                }.toList().take(10).toMap() // Limit to 10 suggestions
                
                withContext(Dispatchers.Main) {
                    _userStatesValues.value = _userStatesValues.value.copy(
                        searchResults = filteredResults,
                        isSearching = false
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _userStatesValues.value = _userStatesValues.value.copy(
                        searchResults = emptyMap(),
                        isSearching = false
                    )
                }
            }
        }
    }
    
    fun selectSearchResult(username: String, userInfo: LeetCodeUserInfo) {
        val displayName = userInfo.profile?.realName?.takeIf { it.isNotBlank() } 
            ?: userInfo.username 
            ?: "Unknown User"
            
        _userStatesValues.value = _userStatesValues.value.copy(
            searchQuery = displayName,
            showSearchSuggestions = false,
            searchResults = emptyMap()
        )
        
        // Here you can add logic to handle the selected user
        // For example, add to comparison list, navigate to profile, etc.
    }
    
    fun clearSearch() {
        searchJob?.cancel()
        _userStatesValues.value = _userStatesValues.value.copy(
            searchQuery = "",
            searchResults = emptyMap(),
            showSearchSuggestions = false,
            isSearching = false
        )
    }
}
