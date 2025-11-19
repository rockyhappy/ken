package com.devrachit.ken.domain.models

data class SavedQuestion(
    val questionId: Int = 0,
    val questionTitle: String,
    val questionSlug: String,
    val questionDifficulty: String = "",
    val questionUrl: String = "",
    val folderId: Int,
    val savedAt: Long = System.currentTimeMillis(),
    val notes: String = "",
    val isSolved: Boolean = false,
    val solvedAt: Long? = null
)
