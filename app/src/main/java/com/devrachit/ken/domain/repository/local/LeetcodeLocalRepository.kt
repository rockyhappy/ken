package com.devrachit.ken.domain.repository.local

import com.devrachit.ken.data.local.entity.UserProfileCalenderEntity
import com.devrachit.ken.data.local.entity.UserQuestionStatusEntity
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserCalendar
import com.devrachit.ken.domain.models.UserQuestionStatusData
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow

interface LeetcodeLocalRepository {
    fun getUserInfoFlow(username: String): Flow<Resource<LeetCodeUserInfo>>
    suspend fun getUserInfo(username: String): Resource<LeetCodeUserInfo>
    suspend fun saveUserInfo(userInfo: LeetCodeUserInfo)
    suspend fun getLastFetchTime(username: String): Long?
    suspend fun clearCache()
    suspend fun clearUserCache(username: String)
    suspend fun cleanExpiredCache(expiryTimeMillis: Long)

    suspend fun getLastUserQuestionStatusFetchTime(username: String): Long?
    suspend fun getUserQuestionStatus(username: String): Resource<UserQuestionStatusData>
    suspend fun saveUserQuestionStatus(userQuestionStatus: UserQuestionStatusEntity)


    // These function are for the User Streak
    suspend fun getUserProfileCalender(username: String): Resource<UserProfileCalenderEntity>
    suspend fun saveUserProfileCalender(username : String , userCalender: UserProfileCalenderEntity)
    suspend fun deleteUserProfileCalender(username: String)
    suspend fun deleteAllUserProfileCalender()
    suspend fun getLastUserProfileCalenderFetchTime(username: String): Long?
}