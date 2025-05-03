package com.devrachit.ken.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devrachit.ken.data.local.entity.UserRecentSubmissionEntity

@Dao
interface LeetCodeUserRecentSubmissionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(recentSubmissions: UserRecentSubmissionEntity)

    @Query("SELECT * FROM user_recent_submissions WHERE username = :username")
    suspend fun getRecentSubmissions(username: String): UserRecentSubmissionEntity


    @Query("DELETE FROM user_recent_submissions WHERE username = :username")
    suspend fun deleteUserRecentSubmission(username: String)
    
    @Query("DELETE FROM user_recent_submissions")
    suspend fun deleteAllUserRecentSubmissions()
    
    @Query("SELECT * FROM user_recent_submissions WHERE lastFetchTime < :timestamp")
    suspend fun getExpiredCacheEntries(timestamp: Long): List<UserRecentSubmissionEntity>
    
    @Query("DELETE FROM user_recent_submissions WHERE lastFetchTime < :timestamp")
    suspend fun deleteExpiredCacheEntries(timestamp: Long): Int
}   