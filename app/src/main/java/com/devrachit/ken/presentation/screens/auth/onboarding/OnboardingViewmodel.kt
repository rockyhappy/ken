package com.devrachit.ken.presentation.screens.auth.onboarding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import android.util.Log
import org.json.JSONObject

data class User(
    val userName: String? = "rockyhappy",
    val isUserNameValid: Boolean? = true,
    val isChecking: Boolean = false,
    val errorMessage: String? = null
)

@Serializable
data class LeetCodeUserInfo(
    val username: String,
    val lastName: String? = null,
    val firstName: String? = null,
    val profile: UserProfile? = null,
    val githubUrl: String? = null,
    val twitterUrl: String? = null,
    val linkedinUrl: String? = null,
    val contributions: UserContributions? = null
)

@Serializable
data class UserProfile(
    val ranking: Int? = null,
    val userAvatar: String? = null,
    val countryName: String? = null,
    val company: String? = null,
    val school: String? = null
)

@Serializable
data class UserContributions(
    val points: Int? = null,
    val questionCount: Int? = null
)

// Wrapper classes for GraphQL response
@Serializable
data class UserInfoResponse(
    val data: UserInfoData? = null,
    val errors: List<GraphQLError>? = null
)

@Serializable
data class UserInfoData(
    @SerialName("matchedUser")
    val matchedUser: LeetCodeUserInfo? = null
)

@Serializable
data class GraphQLError(
    val message: String
)

@HiltViewModel
class OnboardingViewmodel @Inject constructor(
    @ApplicationContext
    private val context: Context,
) : ViewModel() {
    private var _userValues = MutableStateFlow(User())
    val userValues: StateFlow<User> = _userValues.asStateFlow()

    private val client = OkHttpClient.Builder()
        .addInterceptor(ChuckerInterceptor.Builder(context).build())
        .build()
    private val json = Json { ignoreUnknownKeys = true }

    fun updateUserName(userName: String) {
        _userValues.value = _userValues.value.copy(userName = userName)
    }

    fun checkUserExists()
    {
        checkUsernameAvailability(_userValues.value.userName.toString())
    }
    suspend fun getUserInfoFromLeetCode(username: String): Result<LeetCodeUserInfo> {
        Log.d("OnboardingViewModel", "Fetching user info for $username from LeetCode")
        
        val query = """
            query userInfo(${"$"}username: String!) {
                matchedUser(username: ${"$"}username) {
                    username
                    lastName
                    firstName
                    profile {
                        ranking
                        userAvatar
                        countryName
                        company
                        school
                    }
                    githubUrl
                    twitterUrl
                    linkedinUrl
                    contributions {
                        points
                        questionCount
                    }
                }
            }
        """.trimIndent()

        // Construct the request JSON using org.json
        val jsonRequest = JSONObject().apply {
            put("query", query)
            put("operationName", "userInfo")
            put("variables", JSONObject().put("username", username))
        }
        
        val requestJson = jsonRequest.toString()
        Log.d("OnboardingViewModel", "Request JSON: $requestJson") // Log the exact JSON we're sending
        
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = requestJson.toRequestBody(mediaType)
        
        val request = Request.Builder()
            .url("https://leetcode.com/graphql")
            .post(requestBody)
            .header("Content-Type", "application/json")
            .header("Referer", "https://leetcode.com/")
            .header("Origin", "https://leetcode.com")
            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
            .build()
        
        return withContext(Dispatchers.IO) {
            try {
                client.newCall(request).execute().use { response ->
                    // Log response code and message
                    Log.d("OnboardingViewModel", "Response code: ${response.code}, message: ${response.message}")
                    
                    if (!response.isSuccessful) {
                        val errorBody = response.body?.string()
                        Log.e("OnboardingViewModel", "Error response body: $errorBody")
                        return@withContext Result.failure(Exception("Network error: ${response.code}, $errorBody"))
                    }
                    
                    val responseBody = response.body?.string() ?: 
                        return@withContext Result.failure(Exception("Empty response body"))
                    
                    Log.d("OnboardingViewModel", "Response: $responseBody")
                    
                    try {
                        val userInfoResponse = json.decodeFromString<UserInfoResponse>(responseBody)
                        
                        // Check for errors in the GraphQL response
                        if (!userInfoResponse.errors.isNullOrEmpty()) {
                            return@withContext Result.failure(
                                Exception("GraphQL error: ${userInfoResponse.errors.first().message}")
                            )
                        }
                        
                        // If no matchedUser was found, return failure
                        val userInfo = userInfoResponse.data?.matchedUser ?: 
                            return@withContext Result.failure(Exception("User not found"))
                            
                        Result.success(userInfo)
                    } catch (e: Exception) {
                        Log.e("OnboardingViewModel", "Failed to parse response: ${e.message}", e)
                        Result.failure(Exception("Failed to parse response: ${e.message}"))
                    }
                }
            } catch (e: Exception) {
                Log.e("OnboardingViewModel", "Request failed: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    suspend fun checkIfUserExists(username: String): Boolean {
        val query = """
            query userPublicProfile(${"$"}username: String!) {
                matchedUser(username: ${"$"}username) {
                    username
                }
            }
        """.trimIndent()
        
        val jsonBody = JSONObject()
        jsonBody.put("query", query)
        jsonBody.put("variables", JSONObject().put("username", username))
        
        val requestJson = jsonBody.toString()
        Log.d("OnboardingViewModel", "User exists check request: $requestJson")
        
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = requestJson.toRequestBody(mediaType)
        
        val request = Request.Builder()
            .url("https://leetcode.com/graphql")
            .post(requestBody)
            .header("Content-Type", "application/json")
            .header("Referer", "https://leetcode.com/")
            .build()
        
        return withContext(Dispatchers.IO) {
            try {
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        Log.e("OnboardingViewModel", "Error: ${response.code}")
                        return@withContext false
                    }
                    
                    val responseBody = response.body?.string()
                    Log.d("OnboardingViewModel", "Response: $responseBody")
                    
                    if (responseBody != null) {
                        val json = JSONObject(responseBody)
                        val data = json.optJSONObject("data")
                        val matchedUser = data?.optJSONObject("matchedUser")
                        return@withContext matchedUser != null
                    }
                    
                    false
                }
            } catch (e: Exception) {
                Log.e("OnboardingViewModel", "Exception: ${e.message}", e)
                false
            }
        }
    }

    fun checkUsernameAvailability(username: String) {
        _userValues.value = _userValues.value.copy(isChecking = true, errorMessage = null)
        
        viewModelScope.launch {
            try {
                val result = getUserInfoFromLeetCode(username)
                
                result.fold(
                    onSuccess = { userInfo ->
                        _userValues.value = _userValues.value.copy(
                            isChecking = false,
                            isUserNameValid = true
                        )
                        Log.d("OnboardingViewModel", "User $username exists on LeetCode: $userInfo")
                    },
                    onFailure = { error ->
                        if (error.message?.contains("User not found") == true) {
                            _userValues.value = _userValues.value.copy(
                                isChecking = false,
                                isUserNameValid = false,
                                errorMessage = "User not found on LeetCode"
                            )
                        } else {
                            _userValues.value = _userValues.value.copy(
                                isChecking = false,
                                errorMessage = "Error checking username: ${error.message}"
                            )
                        }
                        Log.e("OnboardingViewModel", "Error checking username: ${error.message}")
                    }
                )
            } catch (e: Exception) {
                _userValues.value = _userValues.value.copy(
                    isChecking = false, 
                    errorMessage = "Error: ${e.message}"
                )
                Log.e("OnboardingViewModel", "Exception during username check: ${e.message}", e)
            }
        }
    }
}