package com.devrachit.ken.data.repository.remote

import android.util.Log
import com.devrachit.ken.data.local.dao.LeetCodeUserDao
import com.devrachit.ken.data.local.entity.LeetCodeUserEntity
import com.devrachit.ken.data.remote.queries.GraphqlQuery
import com.devrachit.ken.data.remote.services.LeetcodeApiService
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserInfoResponse
import com.devrachit.ken.domain.repository.LeetcodeRepository
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LeetcodeRepositoryImpl @Inject constructor(
    private val apiService: LeetcodeApiService,
    private val userDao: LeetCodeUserDao
) : LeetcodeRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private val CACHE_TIMEOUT_MS = TimeUnit.HOURS.toMillis(2)


    override suspend fun fetchUserInfoFromNetwork(username: String): Resource<LeetCodeUserInfo> {
        val jsonRequest = GraphqlQuery.getUserExistsJsonRequest(username=username)
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
            Timber.e("Error fetching user: ${e.message}", e)
            Resource.Error("Error fetching user: ${e.message}")
        }
    }
    
    override fun getUserInfoFromCache(username: String): Flow<Resource<LeetCodeUserInfo>> {
        return userDao.getUserByUsernameFlow(username)
            .map { cachedUser ->
                if (cachedUser != null) {
                    Resource.Success(cachedUser.toDomainModel())
                } else {
                    Resource.Error("User not found in cache")
                }
            }
    }
    
    override suspend fun saveUserInfoToCache(userInfo: LeetCodeUserInfo) {
        if (userInfo.username != null) {
            userDao.insertUser(
                LeetCodeUserEntity.fromDomainModel(userInfo, System.currentTimeMillis())
            )
        }
    }
    
    override suspend fun getLastFetchTimeForUser(username: String): Long? {
        return userDao.getUserByUsername(username)?.lastFetchTime
    }
    
    override suspend fun clearCache() {
        userDao.deleteAllUsers()
    }
    
    override suspend fun clearUserCache(username: String) {
        userDao.deleteUser(username)
    }

    
    suspend fun cleanExpiredCache() {
        val expiryTimestamp = System.currentTimeMillis() - CACHE_TIMEOUT_MS
        val expiredEntries = userDao.getExpiredCacheEntries(expiryTimestamp)
        expiredEntries.forEach { 
            userDao.deleteUser(it.username)
        }
    }
}