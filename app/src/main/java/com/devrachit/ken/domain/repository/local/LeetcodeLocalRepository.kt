package com.devrachit.ken.domain.repository.local

import com.devrachit.ken.data.local.entity.LeetCodeUserEntity
import com.devrachit.ken.data.local.entity.UserProfileCalenderEntity
import com.devrachit.ken.data.local.entity.UserQuestionStatusEntity
import com.devrachit.ken.data.local.entity.UserRecentSubmissionEntity
import com.devrachit.ken.data.local.entity.UserContestRankingEntity
import com.devrachit.ken.data.local.entity.UserBadgesEntity
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserBadges
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
    suspend fun getAllUsers() : List<LeetCodeUserEntity>

    suspend fun getLastUserQuestionStatusFetchTime(username: String): Long?
    suspend fun getUserQuestionStatus(username: String): Resource<UserQuestionStatusData>
    suspend fun saveUserQuestionStatus(userQuestionStatus: UserQuestionStatusEntity)
    suspend fun deleteUserQuestionStatus(username: String)
    suspend fun deleteAllUserQuestionStatus()

    // These function are for the User Streak
    suspend fun getUserProfileCalender(username: String): Resource<UserProfileCalenderEntity>
    suspend fun saveUserProfileCalender(username : String , userCalender: UserProfileCalenderEntity)
    suspend fun deleteUserProfileCalender(username: String)
    suspend fun deleteAllUserProfileCalender()
    suspend fun getLastUserProfileCalenderFetchTime(username: String): Long?


    // these functions are for user Recent Submissions
    suspend fun saveRecentSubmissions(username: String, recentSubmissions: UserRecentSubmissionEntity)
    suspend fun getRecentSubmissions(username: String): Resource<UserRecentSubmissionEntity>
    suspend fun deleteRecentSubmissions(username: String)
    suspend fun deleteAllRecentSubmissions()
    suspend fun getLastRecentSubmissionsFetchTime(username: String): Long?

    // These functions are for user contest ranking
    suspend fun getUserContestRanking(username: String): Resource<UserContestRankingEntity>
    suspend fun saveUserContestRanking(username: String, contestRanking: UserContestRankingEntity)
    suspend fun deleteUserContestRanking(username: String)
    suspend fun deleteAllUserContestRankings()
    suspend fun getLastUserContestRankingFetchTime(username: String): Long?

    // These functions are for user badges
    suspend fun getUserBadges(username: String): Resource<UserBadgesEntity>
    suspend fun saveUserBadges(username: String, userBadges: UserBadgesEntity)
    suspend fun deleteUserBadges(username: String)
    suspend fun deleteAllUserBadges()
    suspend fun getLastUserBadgesFetchTime(username: String): Long?
}