package com.devrachit.ken.data.repository.local

import com.devrachit.ken.data.local.dao.LeetCodeUserContestRatingDao
import com.devrachit.ken.data.local.dao.LeetCodeUserDao
import com.devrachit.ken.data.local.dao.LeetCodeUserProfileCalenderDao
import com.devrachit.ken.data.local.dao.LeetCodeUserRecentSubmissionDao
import com.devrachit.ken.data.local.dao.LeetCodeUserBadgesDao
import com.devrachit.ken.data.local.entity.LeetCodeUserEntity
import com.devrachit.ken.data.local.entity.UserContestRankingEntity
import com.devrachit.ken.data.local.entity.UserProfileCalenderEntity
import com.devrachit.ken.data.local.entity.UserQuestionStatusEntity
import com.devrachit.ken.data.local.entity.UserRecentSubmissionEntity
import com.devrachit.ken.data.local.entity.UserBadgesEntity
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserCalendar
import com.devrachit.ken.domain.models.UserProfileCalendarData
import com.devrachit.ken.domain.models.UserQuestionStatusData
import com.devrachit.ken.domain.repository.local.LeetcodeLocalRepository
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LeetcodeLocalRepositoryImpl @Inject constructor(
    private val userDao: LeetCodeUserDao,
    private val userProfileCalenderDao: LeetCodeUserProfileCalenderDao,
    private val userRecentSubmissionDao: LeetCodeUserRecentSubmissionDao,
    private val userContestRatingDao: LeetCodeUserContestRatingDao,
    private val userBadgesDao: LeetCodeUserBadgesDao
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

    override suspend fun getLastUserQuestionStatusFetchTime(username: String): Long? {
        return userDao.getUserQuestionStatus(username)?.lastFetchTime
    }

    override suspend fun getUserQuestionStatus(username: String): Resource<UserQuestionStatusData> {
        val data = userDao.getUserQuestionStatus(username)?.toDomainModel()
        return if (data != null) {
            Resource.Success(data)
        } else {
            Resource.Error("User Question Status not found in cache")
        }
    }

    override suspend fun saveUserQuestionStatus(userQuestionStatus: UserQuestionStatusEntity) {
        userDao.insertUserQuestionStatus(userQuestionStatus)
    }
    override suspend fun deleteAllUserQuestionStatus() {
        userDao.deleteAllUserQuestionStatus()
    }
    override suspend fun deleteUserQuestionStatus(username: String) {
        userDao.deleteUserQuestionStatus(username)
    }

    override suspend fun getUserProfileCalender(username: String): Resource<UserProfileCalenderEntity> {
        val data = userProfileCalenderDao.getUserProfileCalender(username)
        return if (data != null) {
            Resource.Success(data)
        } else {
            Resource.Error("User Profile Calender not found in cache")
        }
    }

    override suspend fun saveUserProfileCalender(username: String, userCalender: UserProfileCalenderEntity) {
        userProfileCalenderDao.insertUserProfileCalender(userCalender)
    }

    override suspend fun deleteAllUserProfileCalender() {
        userProfileCalenderDao.deleteAllUserCalendars()
    }
    override suspend fun deleteUserProfileCalender(username: String) {
        userProfileCalenderDao.deleteUserCalendar(username)
    }

    override suspend fun getLastUserProfileCalenderFetchTime(username: String): Long? {
        return userProfileCalenderDao.getUserProfileCalender(username)?.lastFetchTime
    }


    // these functions are for recent submissions
    override suspend fun saveRecentSubmissions(
        username: String,
        recentSubmissions: UserRecentSubmissionEntity
    ) {
        userRecentSubmissionDao.insertAll(recentSubmissions)
    }

    override suspend fun deleteAllRecentSubmissions() {
        userRecentSubmissionDao.deleteAllUserRecentSubmissions()
    }

    override suspend fun deleteRecentSubmissions(username: String) {
        userRecentSubmissionDao.deleteUserRecentSubmission(username)
    }

    override suspend fun getRecentSubmissions(username: String): Resource<UserRecentSubmissionEntity> {
        val data = userRecentSubmissionDao.getRecentSubmissions(username)
        return if (data != null) {
            Resource.Success(data)
        } else {
            Resource.Error("No data found")
        }
    }

    override suspend fun getLastRecentSubmissionsFetchTime(username: String): Long? {
        userRecentSubmissionDao.getRecentSubmissions(username)?.let {
            return it.lastFetchTime
        }
        return null
    }

    override suspend fun getUserContestRanking(username: String): Resource<UserContestRankingEntity> {
        val data = userContestRatingDao.getUserContestRanking(username)
        return if (data != null) {
            Resource.Success(data)
        } else {
            Resource.Error("User Contest Ranking not found in cache")
        }
    }

    override suspend fun saveUserContestRanking(username: String, contestRanking: UserContestRankingEntity) {
        userContestRatingDao.insertAll(contestRanking)
    }

    override suspend fun deleteUserContestRanking(username: String) {
        userContestRatingDao.deleteUserContestRanking(username)
    }

    override suspend fun deleteAllUserContestRankings() {
        userContestRatingDao.deleteAllUserContestRankings()
    }

    override suspend fun getLastUserContestRankingFetchTime(username: String): Long? {
        return userContestRatingDao.getUserContestRanking(username)?.lastFetchTime
    }
    
    // User Badges implementation
    override suspend fun getUserBadges(username: String): Resource<UserBadgesEntity> {
        val data = userBadgesDao.getUserBadges(username)
        return if (data != null) {
            Resource.Success(data)
        } else {
            Resource.Error("User Badges not found in cache")
        }
    }

    override suspend fun saveUserBadges(username: String, userBadges: UserBadgesEntity) {
        userBadgesDao.insertUserBadges(userBadges)
    }

    override suspend fun deleteUserBadges(username: String) {
        userBadgesDao.deleteUserBadges(username)
    }

    override suspend fun deleteAllUserBadges() {
        userBadgesDao.deleteAllUserBadges()
    }

    override suspend fun getLastUserBadgesFetchTime(username: String): Long? {
        return userBadgesDao.getUserBadges(username)?.lastFetchTime
    }

    override suspend fun getAllUsers():List<LeetCodeUserEntity>{
        return userDao.getAllUsers()
    }

    override suspend fun getAllUserQuestionStatuses(): List<UserQuestionStatusEntity> {
        return userDao.getAllUserQuestionStatuses()
    }

    override suspend fun getAllUserCalendars(): List<UserProfileCalenderEntity> {
        return userProfileCalenderDao.getAllUserCalendars()
    }
}
