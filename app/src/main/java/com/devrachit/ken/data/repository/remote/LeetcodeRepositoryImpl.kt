package com.devrachit.ken.data.repository.remote


import android.util.Log
import com.devrachit.ken.data.remote.queries.GraphqlQuery
import com.devrachit.ken.data.remote.services.LeetcodeApiService
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserInfoResponse
import com.devrachit.ken.domain.repository.LeetcodeRepository
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class LeetcodeRepositoryImpl @Inject constructor(
    private val apiService: LeetcodeApiService
) : LeetcodeRepository {
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getUserInfo(username: String): Resource<LeetCodeUserInfo> {
        val jsonRequest = GraphqlQuery.getUserExistsJsonRequest(username=username)

        val requestBody = jsonRequest.toString().toRequestBody("application/json".toMediaType())

        return try {
            val response = apiService.fetchUser(requestBody)
            val responseBody = response.string()
            val userInfoResponse = json.decodeFromString<UserInfoResponse>(responseBody)

            userInfoResponse.data?.let {
                Resource.Success(
                    it.matchedUser ?: LeetCodeUserInfo()
                )
            } ?: Resource.Error("User not found")
        } catch (e: Exception) {
            Log.e("LeetcodeRepositoryImpl", "Error fetching user: ${e.message}", e)
            Resource.Error("Error fetching user: ${e.message}")
        }
    }
}