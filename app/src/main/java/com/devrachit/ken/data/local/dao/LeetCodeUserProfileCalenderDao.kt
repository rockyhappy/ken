package com.devrachit.ken.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.devrachit.ken.data.local.entity.LeetCodeUserEntity
import com.devrachit.ken.data.local.entity.UserProfileCalenderEntity
import com.devrachit.ken.data.local.entity.UserQuestionStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LeetCodeUserProfileCalenderDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfileCalender(userProfileCalenderEntity: UserProfileCalenderEntity )
    
    @Query("SELECT * FROM user_calendars WHERE username = :username")
    suspend fun getUserProfileCalender(username: String): UserProfileCalenderEntity
    
    @Query("DELETE FROM user_calendars WHERE username = :username")
    suspend fun deleteUserCalendar(username: String)
    
    @Query("DELETE FROM user_calendars")
    suspend fun deleteAllUserCalendars()
    
    @Query("SELECT * FROM user_calendars WHERE lastFetchTime < :timestamp")
    suspend fun getExpiredCacheEntries(timestamp: Long): List<UserProfileCalenderEntity>
    
    @Query("SELECT * FROM user_calendars WHERE username = :username")
    fun getUserProfileCalenderFlow(username: String): Flow<UserProfileCalenderEntity?>
    
    @Query("DELETE FROM user_calendars WHERE lastFetchTime < :timestamp")
    suspend fun deleteExpiredCacheEntries(timestamp: Long): Int
    
    @Query("SELECT * FROM user_calendars")
    suspend fun getAllUserCalendars(): List<UserProfileCalenderEntity>
    
}
