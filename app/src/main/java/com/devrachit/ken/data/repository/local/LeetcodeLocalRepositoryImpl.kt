package com.devrachit.ken.data.repository.local

import com.devrachit.ken.data.local.dao.LeetCodeUserDao
import com.devrachit.ken.data.local.entity.LeetCodeUserEntity
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LeetcodeLocalRepositoryImpl @Inject constructor(
    private val userDao: LeetCodeUserDao
) : LeetcodeLocalRepository {

    override fun getUserInfoFlow(username: String): Flow<Resource<LeetCodeUserInfo>> {
        return userDao.getUserByUsernameFlow(username)
            .map { cachedUser ->
                if (cachedUser != null) {
                    Resource.Success(cachedUser.toDomainModel())
                } else {
                    Resource.Error("User not found in cache")
                }
            }
    }
    
    override suspend fun getUserInfo(username: String): Resource<LeetCodeUserInfo> {
        val cachedUser = userDao.getUserByUsername(username)
        return if (cachedUser != null) {
            Resource.Success(cachedUser.toDomainModel())
        } else {
            Resource.Error("User not found in cache")
        }
    }

    override suspend fun saveUserInfo(userInfo: LeetCodeUserInfo) {
        if (userInfo.username != null) {
            userDao.insertUser(
                LeetCodeUserEntity.fromDomainModel(userInfo, System.currentTimeMillis())
            )
        }
    }

    override suspend fun getLastFetchTime(username: String): Long? {
        return userDao.getUserByUsername(username)?.lastFetchTime
    }

    override suspend fun clearCache() {
        userDao.deleteAllUsers()
    }

    override suspend fun clearUserCache(username: String) {
        userDao.deleteUser(username)
    }
    
    override suspend fun cleanExpiredCache(expiryTimeMillis: Long) {
        val expiredEntries = userDao.getExpiredCacheEntries(expiryTimeMillis)
        expiredEntries.forEach { 
            userDao.deleteUser(it.username)
        }
    }
}