package com.devrachit.ken.data.repository.remote

import android.util.Log
import com.devrachit.ken.data.remote.queries.GraphqlQuery
import com.devrachit.ken.data.remote.services.LeetcodeApiService
import com.devrachit.ken.domain.models.ContestRatingHistogramResponse
import com.devrachit.ken.domain.models.CurrentTimeResponse
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserBadgesResponse
import com.devrachit.ken.domain.models.UserContestRankingResponse
import com.devrachit.ken.domain.models.UserInfoResponse
import com.devrachit.ken.domain.models.UserProfileCalendarResponse
import com.devrachit.ken.domain.models.UserQuestionStatusData
import com.devrachit.ken.domain.models.UserQuestionStatusResponse
import com.devrachit.ken.domain.models.UserRecentAcSubmissionResponse
import com.devrachit.ken.domain.repository.remote.LeetcodeRemoteRepository
import com.devrachit.ken.utility.NetworkUtility.Resource
import com.devrachit.ken.utility.constants.Constants.Companion.USERCONTESTPARTICIPATIONERROR
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
            println("Response: Current Data:$response")
            val responseBody = response.string()
            val currentData = json.decodeFromString<CurrentTimeResponse>(responseBody)
            println("Response: Current Data:$currentData")
            Resource.Success(currentData)
        } catch (e: Exception) {
            Resource.Error("Error fetching current data: ${e.message}")
        }

    }

    override suspend fun fetchUserProfileCalender(username: String): Resource<UserProfileCalendarResponse> {
        val jsonRequest = GraphqlQuery.getUserProfileCalendarJsonRequest(username = username)
        val request = jsonRequest.toString().toRequestBody("application/json".toMediaType())
        return try {
            val response = apiService.fetUserProfileCalender(request)
            val responseBody = response.string()
            val userProfileCalendar = json.decodeFromString<UserProfileCalendarResponse>(responseBody)
            Resource.Success(userProfileCalendar)

        } catch (e: Exception) {
            Resource.Error("Error fetching user profile calendar: ${e.message}")
        }
    }

    override suspend fun fetchUserRecentAcSubmissions(username: String, limit: Int?): Resource<UserRecentAcSubmissionResponse> {
        val jsonRequest = GraphqlQuery.getRecentAcSubmissionsJsonRequest(username = username , limit=limit?:15)
        val request = jsonRequest.toString().toRequestBody("application/json".toMediaType())
        return try {
            val response = apiService.fetchRecentSubmissionList(request)
            val responseBody = response.string()
            val userRecentAcSubmissions = json.decodeFromString<UserRecentAcSubmissionResponse>(responseBody)
            Resource.Success(userRecentAcSubmissions)
        }catch (e: Exception){
            Resource.Error("Error fetching user recent ac submissions: ${e.message}")
        }
    }

    override suspend fun fetchContestRankingHistogram(): Resource<ContestRatingHistogramResponse> {
        val jsonRequest = GraphqlQuery.getContestRatingHistogramJsonRequest()
        val request = jsonRequest.toString().toRequestBody("application/json".toMediaType())
        return try {
            val response = apiService.fetchContestRankingHistogram(request)
            val responseBody = response.string()
            val contestRankingHistogram = json.decodeFromString<ContestRatingHistogramResponse>(responseBody)
            Resource.Success(contestRankingHistogram)
        }
        catch (e: Exception){
            Resource.Error("Error fetching contest ranking histogram: ${e.message}")
        }
    }

    override suspend fun fetchUserBadges(username : String): Resource<UserBadgesResponse> {
        val jsonRequest = GraphqlQuery.getUserBadgesJsonRequest(username = username)
        val request = jsonRequest.toString().toRequestBody("application/json".toMediaType())
        return try {
            val response = apiService.fetchUserBadges(request)
            val responseBody = response.string()
            val userBadges = json.decodeFromString<UserBadgesResponse>(responseBody)
            Resource.Success(userBadges)
        }
        catch (e: Exception){
            Resource.Error("Error fetching user badges: ${e.message}")
        }
    }

    override suspend fun fetchUserContestRanking(username: String): Resource<UserContestRankingResponse> {
        val jsonRequest = GraphqlQuery.getUserContestRankingJsonRequest(username = username)
        val request = jsonRequest.toString().toRequestBody("application/json".toMediaType())
        return try {
            val response = apiService.fetchUserContestRanking(request)
            val responseBody = response.string()
            try{
                val userContestRanking = json.decodeFromString<UserContestRankingResponse>(responseBody)
                Resource.Success(userContestRanking)
            }catch (e: Exception){
                Resource.Error(USERCONTESTPARTICIPATIONERROR)
            }

        }catch (e: Exception){
            Resource.Error("Error fetching user contest ranking: ${e.message}")
        }
    }
}