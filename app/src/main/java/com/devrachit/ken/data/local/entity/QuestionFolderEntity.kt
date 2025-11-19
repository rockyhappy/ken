package com.devrachit.ken.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devrachit.ken.domain.models.QuestionFolder

@Entity(tableName = "question_folders")
data class QuestionFolderEntity(
    @PrimaryKey(autoGenerate = true)
    val folderId: Int = 0,
    val folderName: String,
    val folderDescription: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun toDomainModel(): QuestionFolder {
        return QuestionFolder(
            folderId = folderId,
            folderName = folderName,
            folderDescription = folderDescription,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun fromDomainModel(domainModel: QuestionFolder): QuestionFolderEntity {
            return QuestionFolderEntity(
                folderId = domainModel.folderId,
                folderName = domainModel.folderName,
                folderDescription = domainModel.folderDescription,
                createdAt = domainModel.createdAt,
                updatedAt = domainModel.updatedAt
            )
        }
    }
}
