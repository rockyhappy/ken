package com.devrachit.ken.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.devrachit.ken.data.local.entity.QuestionEntity

@Dao
interface LeetcodeQuestionDao {
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuestion(question: QuestionEntity)
}