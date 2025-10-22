package com.devrachit.ken.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devrachit.ken.data.local.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeyEntity>)

    @Query("SELECT * FROM remote_keys WHERE questionId = :questionId AND searchQuery = :searchQuery")
    suspend fun remoteKeysQuestionId(questionId: Int, searchQuery: String?): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys WHERE searchQuery = :searchQuery")
    suspend fun clearRemoteKeys(searchQuery: String?)

    @Query("DELETE FROM remote_keys")
    suspend fun clearAllRemoteKeys()

    @Query("SELECT * FROM remote_keys WHERE searchQuery = :searchQuery ORDER BY questionId ASC LIMIT 1")
    suspend fun getFirstRemoteKey(searchQuery: String?): RemoteKeyEntity?

    @Query("SELECT * FROM remote_keys WHERE searchQuery = :searchQuery ORDER BY questionId DESC LIMIT 1")
    suspend fun getLastRemoteKey(searchQuery: String?): RemoteKeyEntity?
}
