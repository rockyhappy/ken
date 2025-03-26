package com.devrachit.ken.domain.repository

import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow

interface LeetcodeRepository {
    // Get user info directly from network
    suspend fun fetchUserInfoFromNetwork(username: String): Resource<LeetCodeUserInfo>
    
    // Get user info directly from cache
    fun getUserInfoFromCache(username: String): Flow<Resource<LeetCodeUserInfo>>
    
    // Save user info to cache
    suspend fun saveUserInfoToCache(userInfo: LeetCodeUserInfo)
    
    // Get last fetch time for a user
    suspend fun getLastFetchTimeForUser(username: String): Long?
    
    // Cache management methods
    suspend fun clearCache()
    suspend fun clearUserCache(username: String)
}