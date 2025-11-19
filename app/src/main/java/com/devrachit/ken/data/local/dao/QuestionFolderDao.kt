package com.devrachit.ken.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devrachit.ken.data.local.entity.QuestionFolderEntity
import com.devrachit.ken.data.local.entity.FolderWithQuestionCount
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionFolderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: QuestionFolderEntity): Long

    @Update
    suspend fun updateFolder(folder: QuestionFolderEntity)

    @Delete
    suspend fun deleteFolder(folder: QuestionFolderEntity)

    @Query("DELETE FROM question_folders WHERE folderId = :folderId")
    suspend fun deleteFolderById(folderId: Int)

    @Query("SELECT * FROM question_folders WHERE folderId = :folderId")
    suspend fun getFolderById(folderId: Int): QuestionFolderEntity?

    @Query("SELECT * FROM question_folders ORDER BY createdAt DESC")
    fun getAllFolders(): Flow<List<QuestionFolderEntity>>

    @Query("SELECT * FROM question_folders ORDER BY createdAt DESC")
    suspend fun getAllFoldersSync(): List<QuestionFolderEntity>

    @Query("""
        SELECT 
            qf.folderId,
            qf.folderName,
            qf.folderDescription,
            qf.createdAt,
            qf.updatedAt,
            COUNT(sq.questionId) as questionCount
        FROM question_folders qf
        LEFT JOIN saved_questions sq ON qf.folderId = sq.folderId
        GROUP BY qf.folderId
        ORDER BY qf.createdAt DESC
    """)
    fun getAllFoldersWithCounts(): Flow<List<FolderWithQuestionCount>>

    @Query("""
        SELECT 
            qf.folderId,
            qf.folderName,
            qf.folderDescription,
            qf.createdAt,
            qf.updatedAt,
            COUNT(sq.questionId) as questionCount
        FROM question_folders qf
        LEFT JOIN saved_questions sq ON qf.folderId = sq.folderId
        GROUP BY qf.folderId
        ORDER BY qf.createdAt DESC
    """)
    suspend fun getAllFoldersWithCountsSync(): List<FolderWithQuestionCount>

    @Query("SELECT COUNT(*) FROM question_folders")
    suspend fun getFoldersCount(): Int

    @Query("SELECT * FROM question_folders WHERE folderName LIKE '%' || :folderName || '%'")
    suspend fun searchFolders(folderName: String): List<QuestionFolderEntity>
}
