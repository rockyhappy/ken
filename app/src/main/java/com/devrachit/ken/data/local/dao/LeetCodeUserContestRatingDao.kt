package com.devrachit.ken.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devrachit.ken.data.local.entity.UserContestRankingEntity

@Dao
interface LeetCodeUserContestRatingDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contestRanking: UserContestRankingEntity)

    @Query("SELECT * FROM user_contest_ranking WHERE username = :username")
    suspend fun getUserContestRanking(username: String): UserContestRankingEntity?

    @Query("DELETE FROM user_contest_ranking WHERE username = :username")
    suspend fun deleteUserContestRanking(username: String)
    
    @Query("DELETE FROM user_contest_ranking")
    suspend fun deleteAllUserContestRankings()
    
    @Query("SELECT * FROM user_contest_ranking WHERE lastFetchTime < :timestamp")
    suspend fun getExpiredCacheEntries(timestamp: Long): List<UserContestRankingEntity>
    
    @Query("DELETE FROM user_contest_ranking WHERE lastFetchTime < :timestamp")
    suspend fun deleteExpiredCacheEntries(timestamp: Long): Int
}