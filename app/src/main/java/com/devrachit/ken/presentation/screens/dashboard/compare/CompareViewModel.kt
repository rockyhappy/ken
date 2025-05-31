package com.devrachit.ken.presentation.screens.dashboard.compare

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.toQuestionProgressUiState
import com.devrachit.ken.domain.usecases.getCurrentTime.GetCurrentTime
import com.devrachit.ken.domain.usecases.getUserBadges.GetUserBadgesUseCase
import com.devrachit.ken.domain.usecases.getUserContestRanking.GetUserContestRankingUseCase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetAllUsersUsecase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetAllUserCalendarsUsecase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetAllUserQuestionStatusesUsecase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoNoCacheUseCase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.GetUserInfoUseCase
import com.devrachit.ken.domain.usecases.getUserInfoUsecase.DeleteUserUsecase
import com.devrachit.ken.domain.usecases.getUserProfileCalender.GetUserProfileCalenderUseCase
import com.devrachit.ken.domain.usecases.getUserQuestionStatus.GetUserQuestionStatusUseCase
import com.devrachit.ken.domain.usecases.getUserRecentSubmission.GetUserRecentSubmissionUseCase
import com.devrachit.ken.utility.NetworkUtility.Resource
import com.devrachit.ken.utility.constants.Constants
import com.devrachit.ken.utility.constants.Constants.Companion.USERCONTESTPARTICIPATIONERROR
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CompareViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val getUserQuestionStatusUseCase: GetUserQuestionStatusUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getUserInfoNoCacheUseCase: GetUserInfoNoCacheUseCase,
    private val getAllUsersUsecase: GetAllUsersUsecase,
    private val getAllUserQuestionStatusesUsecase: GetAllUserQuestionStatusesUsecase,
    private val getAllUserCalendarsUsecase: GetAllUserCalendarsUsecase,
    private val getCurrentTime: GetCurrentTime,
    private val deleteUserUsecase: DeleteUserUsecase,
    private val getUserProfileCalenderUseCase: GetUserProfileCalenderUseCase,
    private val getUserRecentSubmissionUseCase: GetUserRecentSubmissionUseCase,
    private val getUserBadgesUseCase: GetUserBadgesUseCase,
    private val getUserContestRankingUseCase: GetUserContestRankingUseCase,

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

    // Add a function for explicit refresh (pull-to-refresh)
    fun refreshAllData() {
        viewModelScope.launch {
            coroutineScope {
                // Reload cached data to show immediately
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
            isSearching = false,
            platformSearchResult = null,
            platformSearchError = null,
            showPlatformResult = false
        )
    }

    fun fetchUserInfoNoCache(username: String, callback: (Resource<LeetCodeUserInfo>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserInfoNoCacheUseCase(username).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Handle loading state if needed
                    }
                    is Resource.Success -> {
                        callback(result)
                    }
                    is Resource.Error -> {
                        if (result.message == Constants.NETWORK_UNAVAILABLE_ERROR) {
                            // Handle network unavailable error
                            callback(Resource.Error("Network is not available. Please check your connection."))
                        } else {
                            callback(result)
                        }
                    }
                }
            }
        }
    }

    fun searchPlatformUser(username: String) {
        if (username.isBlank()) return
        
        viewModelScope.launch {
            _userStatesValues.value = _userStatesValues.value.copy(
                isPlatformSearching = true,
                platformSearchError = null,
                platformSearchResult = null,
                showPlatformResult = false
            )
            
            launch(Dispatchers.IO) {
                getUserInfoNoCacheUseCase(username.trim()).collectLatest { result ->
                    when (result) {
                        is Resource.Loading -> {
                            withContext(Dispatchers.Main) {
                                _userStatesValues.value = _userStatesValues.value.copy(
                                    isPlatformSearching = true
                                )
                            }
                        }
                        is Resource.Success -> {
                            withContext(Dispatchers.Main) {
                                _userStatesValues.value = _userStatesValues.value.copy(
                                    isPlatformSearching = false,
                                    platformSearchResult = result.data,
                                    platformSearchError = null,
                                    showPlatformResult = true
                                )
                            }
                        }
                        is Resource.Error -> {
                            val errorMessage = when (result.message) {
                                Constants.NETWORK_UNAVAILABLE_ERROR -> 
                                    "Network is not available. Please check your connection."
                                else -> "User not found on the platform"
                            }
                            
                            withContext(Dispatchers.Main) {
                                _userStatesValues.value = _userStatesValues.value.copy(
                                    isPlatformSearching = false,
                                    platformSearchResult = null,
                                    platformSearchError = errorMessage,
                                    showPlatformResult = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun hidePlatformResult() {
        _userStatesValues.value = _userStatesValues.value.copy(
            showPlatformResult = false,
            platformSearchResult = null,
            platformSearchError = null
        )
    }

    fun deleteUser(username: String) {
        viewModelScope.launch {
            deleteUserUsecase(username).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        // Handle loading state if needed
                    }
                    is Resource.Success -> {
                        // Remove user from all UI state maps immediately
                        val currentFriendsDetails = _userStatesValues.value.friendsDetails?.toMutableMap()
                        val currentQuestionProgress = _userStatesValues.value.friendsQuestionProgressInfo?.toMutableMap()
                        val currentCalendarData = _userStatesValues.value.userProfileCalender?.toMutableMap()
                        
                        // Remove the user from all maps
                        currentFriendsDetails?.remove(username)
                        currentQuestionProgress?.remove(username)
                        currentCalendarData?.remove(username)
                        
                        // Update UI state with the updated maps
                        _userStatesValues.value = _userStatesValues.value.copy(
                            friendsDetails = currentFriendsDetails,
                            friendsQuestionProgressInfo = currentQuestionProgress,
                            userProfileCalender = currentCalendarData
                        )
                    }
                    is Resource.Error -> {
                        // Handle error - you might want to show a toast or error message
                    }
                }
            }
        }
    }

    fun refreshSingleUser(username: String) {
        viewModelScope.launch {
            // First show loading state for this specific refresh
            // Then refresh individual user data in background
            launch(Dispatchers.IO) {
                refreshSingleUserData(username)
                // After refresh is complete, reload the cache data to update UI
                refreshBulkUIData()
            }
        }
    }

    private suspend fun refreshSingleUserData(username: String) {
        try {
            coroutineScope {
                launch { refreshUserQuestionStatus(username) }
                launch { refreshUserProfileCalender(username) }
                launch { refreshUserRecentSubmission(username) }
                launch { refreshUserBadges(username) }
                launch { refreshUserContestRanking(username) }
            }
        } catch (e: Exception) {
            // Handle errors silently for background refresh
        }
    }
    
    private suspend fun refreshBulkUIData() {
        try {
            // These operations read from cache that was just updated by individual API calls
            coroutineScope {
                launch { 
                    getAllUsersInfo() 
                }
                launch { 
                    getAllUserQuestionStatuses() 
                }
                launch { 
                    getAllUserCalendars() 
                }
            }
        } catch (e: Exception) {
            // Handle errors silently for background refresh
        }
    }
    
    private suspend fun refreshUserQuestionStatus(username: String) {
        try {
            getUserQuestionStatusUseCase(username, forceRefresh = true).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        // Data is automatically cached by the use case
                        return@collect // Exit after first success
                    }
                    is Resource.Error -> {
                        // Handle silently for background refresh and continue
                        return@collect
                    }
                    is Resource.Loading -> {
                        // Continue waiting
                    }
                }
            }
        } catch (e: Exception) {
            // Handle silently
        }
    }
    
    private suspend fun refreshUserProfileCalender(username: String) {
        try {
            getUserProfileCalenderUseCase(username, forceRefresh = true).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        // Data is automatically cached by the use case
                        return@collect // Exit after first success
                    }
                    is Resource.Error -> {
                        // Handle silently for background refresh and continue
                        return@collect
                    }
                    is Resource.Loading -> {
                        // Continue waiting
                    }
                }
            }
        } catch (e: Exception) {
            // Handle silently
        }
    }
    
    private suspend fun refreshUserRecentSubmission(username: String) {
        try {
            getUserRecentSubmissionUseCase(username, limit = 15, forceRefresh = true).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        // Data is automatically cached by the use case
                        return@collect // Exit after first success
                    }
                    is Resource.Error -> {
                        // Handle silently for background refresh and continue
                        return@collect
                    }
                    is Resource.Loading -> {
                        // Continue waiting
                    }
                }
            }
        } catch (e: Exception) {
            // Handle silently
        }
    }
    
    private suspend fun refreshUserBadges(username: String) {
        try {
            getUserBadgesUseCase(username, forceRefresh = true).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        // Data is automatically cached by the use case
                        return@collect // Exit after first success
                    }
                    is Resource.Error -> {
                        // Handle silently for background refresh and continue
                        return@collect
                    }
                    is Resource.Loading -> {
                        // Continue waiting
                    }
                }
            }
        } catch (e: Exception) {
            // Handle silently
        }
    }
    
    private suspend fun refreshUserContestRanking(username: String) {
        try {
            getUserContestRankingUseCase(username, forceRefresh = true).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        // Data is automatically cached by the use case
                        return@collect // Exit after first success
                    }
                    is Resource.Error -> {
                        // Handle silently for background refresh and continue
                        return@collect
                    }
                    is Resource.Loading -> {
                        // Continue waiting
                    }
                }
            }
        } catch (e: Exception) {
            // Handle silently
        }
    }

}
