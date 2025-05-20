package com.devrachit.ken.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devrachit.ken.data.local.entity.UserBadgesEntity

@Dao
interface LeetCodeUserBadgesDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserBadges(userBadges: UserBadgesEntity)

    @Query("SELECT * FROM user_badges WHERE username = :username")
    suspend fun getUserBadges(username: String): UserBadgesEntity?

    @Query("DELETE FROM user_badges WHERE username = :username")
    suspend fun deleteUserBadges(username: String)
    
    @Query("DELETE FROM user_badges")
    suspend fun deleteAllUserBadges()
    
    @Query("SELECT * FROM user_badges WHERE lastFetchTime < :timestamp")
    suspend fun getExpiredCacheEntries(timestamp: Long): List<UserBadgesEntity>
    
    @Query("DELETE FROM user_badges WHERE lastFetchTime < :timestamp")
    suspend fun deleteExpiredCacheEntries(timestamp: Long): Int
}