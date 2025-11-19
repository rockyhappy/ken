package com.devrachit.ken.data.local.entity

import androidx.room.ColumnInfo

data class FolderWithQuestionCount(
    @ColumnInfo(name = "folderId")
    val folderId: Int,
    @ColumnInfo(name = "folderName")
    val folderName: String,
    @ColumnInfo(name = "folderDescription")
    val folderDescription: String,
    @ColumnInfo(name = "createdAt")
    val createdAt: Long,
    @ColumnInfo(name = "updatedAt")
    val updatedAt: Long,
    @ColumnInfo(name = "questionCount")
    val questionCount: Int
)
