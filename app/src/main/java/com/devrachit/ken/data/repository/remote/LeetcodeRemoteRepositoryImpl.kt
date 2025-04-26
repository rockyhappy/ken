package com.devrachit.ken.data.repository.remote

import com.devrachit.ken.data.remote.queries.GraphqlQuery
import com.devrachit.ken.data.remote.services.LeetcodeApiService
import com.devrachit.ken.domain.models.CurrentTimeResponse
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserInfoResponse
import com.devrachit.ken.domain.models.UserQuestionStatusData
import com.devrachit.ken.domain.models.UserQuestionStatusResponse
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import javax.inject.Inject

class LeetcodeRemoteRepositoryImpl @Inject constructor(
    private val apiService: LeetcodeApiService
) : LeetcodeRemoteRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun fetchUserInfo(username: String): Resource<LeetCodeUserInfo> {
        val jsonRequest = GraphqlQuery.getUserExistsJsonRequest(username = username)
        val requestBody = jsonRequest.toString().toRequestBody("application/json".toMediaType())

        return try {
            val response = apiService.fetchUser(requestBody)
            val responseBody = response.string()
            val userInfoResponse = json.decodeFromString<UserInfoResponse>(responseBody)

            userInfoResponse.data?.let {
                val userInfo = it.matchedUser ?: LeetCodeUserInfo()
                Resource.Success(userInfo)
            } ?: Resource.Error("User not found")
        } catch (e: Exception) {
            Resource.Error("Error fetching user: ${e.message}")
        }
    }

    override suspend fun fetchUserRankingInfo(username: String): Resource<UserQuestionStatusData> {
        val jsonRequest = GraphqlQuery.getUserQuestionCountJsonRequest(username = username)
        val requestBody = jsonRequest.toString().toRequestBody("application/json".toMediaType())

        return try {
            val response = apiService.fetchUserQuestionCount(requestBody)
            val responseBody = response.string()
            val userQuestionStatusData =
                json.decodeFromString<UserQuestionStatusResponse>(responseBody)

            userQuestionStatusData.data?.let {
                val userQuestionStatusData = it
                Resource.Success(userQuestionStatusData)
            } ?: Resource.Error("User not found")
        } catch (e: Exception) {
            Resource.Error("Error fetching user: ${e.message}")
        }
    }

    override suspend fun fetchCurrentData(): Resource<CurrentTimeResponse> {
        val jsonRequest = GraphqlQuery.getCurrentDataJsonRequest()
        val request = jsonRequest.toString().toRequestBody("application/json".toMediaType())

        return try {
            val response = apiService.fetchCurrentTime(request)
            val responseBody = response.string()
            val currentData = json.decodeFromString<CurrentTimeResponse>(responseBody)
            Resource.Success(currentData)
        } catch (e: Exception) {
            Resource.Error("Error fetching current data: ${e.message}")
        }

    }

    override suspend fun fetchUserProfileCalender(username: String): Resource<Any?> {
        val jsonRequest = GraphqlQuery.getUserProfileCalendarJsonRequest(username = username)
        val request = jsonRequest.toString().toRequestBody("application/json".toMediaType())
        return try {
            val response = apiService.fetUserProfileCalender(request)
            val responseBody = response.string()
            val userProfileCalendar = json.decodeFromString<Any?>(responseBody)
            Resource.Success(userProfileCalendar)

        } catch (e: Exception) {
            Resource.Error("Error fetching user profile calendar: ${e.message}")
        }
    }
}