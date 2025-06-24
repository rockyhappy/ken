package com.devrachit.ken.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey val questionId: Int,
    val title: String,
    val difficulty: String,
    val isSolved: Boolean
)
