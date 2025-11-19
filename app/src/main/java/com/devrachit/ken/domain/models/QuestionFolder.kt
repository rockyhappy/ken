package com.devrachit.ken.domain.models

data class QuestionFolder(
    val folderId: Int = 0,
    val folderName: String,
    val folderDescription: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val questionCount: Int = 0
)
