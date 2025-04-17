package com.devrachit.ken.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devrachit.ken.data.local.entity.LeetCodeUserEntity
import com.devrachit.ken.data.local.entity.UserQuestionStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LeetCodeUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: LeetCodeUserEntity)
    
    @Query("SELECT * FROM leetcode_users WHERE username = :username")
    suspend fun getUserByUsername(username: String): LeetCodeUserEntity?
    
    @Query("SELECT * FROM leetcode_users WHERE username = :username")
    fun getUserByUsernameFlow(username: String): Flow<LeetCodeUserEntity?>
    
    @Query("DELETE FROM leetcode_users WHERE username = :username")
    suspend fun deleteUser(username: String)
    
    @Query("DELETE FROM leetcode_users")
    suspend fun deleteAllUsers()
    
    @Query("SELECT * FROM leetcode_users WHERE lastFetchTime < :timestamp")
    suspend fun getExpiredCacheEntries(timestamp: Long): List<LeetCodeUserEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserQuestionStatus(userQuestionStatus: UserQuestionStatusEntity)


    @Query("SELECT * FROM USER_QUESTION_STATUS WHERE username = :username")
    suspend fun getUserQuestionStatus(username: String): UserQuestionStatusEntity?
}