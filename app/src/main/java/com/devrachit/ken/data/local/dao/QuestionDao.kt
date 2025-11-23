package com.devrachit.ken.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devrachit.ken.data.local.entity.QuestionEntity

@Dao
interface QuestionDao {

    @Query("""
        SELECT * FROM questions 
        WHERE (:searchQuery IS NULL OR searchQuery = :searchQuery OR searchQuery IS NULL)
        AND (:difficulty IS NULL OR difficulty = :difficulty)
        AND (:status IS NULL OR status = :status)
        AND (:query IS NULL OR 
             title LIKE '%' || :query || '%' OR 
             titleSlug LIKE '%' || :query || '%' OR
             topicTags LIKE '%' || :query || '%')
        ORDER BY 
            CASE 
                WHEN :sortBy = 'title' THEN title
                WHEN :sortBy = 'difficulty' THEN difficulty
                WHEN :sortBy = 'number' THEN CAST(frontendQuestionId AS INTEGER)
                ELSE CAST(frontendQuestionId AS INTEGER)
            END ASC
    """)
    fun getQuestionsPagingSource(
        searchQuery: String? = null,
        query: String? = null,
        difficulty: String? = null,
        status: String? = null,
        sortBy: String = "number"
    ): PagingSource<Int, QuestionEntity>

    @Query("""
        SELECT * FROM questions 
        WHERE title LIKE '%' || :query || '%' OR 
              titleSlug LIKE '%' || :query || '%' OR
              topicTags LIKE '%' || :query || '%'
        ORDER BY 
            CASE 
                WHEN title LIKE :query || '%' THEN 1
                WHEN title LIKE '%' || :query || '%' THEN 2
                ELSE 3
            END,
            title ASC
        LIMIT :limit
    """)
    suspend fun searchQuestions(query: String, limit: Int = 20): List<QuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity)

    @Query("DELETE FROM questions WHERE searchQuery = :searchQuery")
    suspend fun clearQuestionsForSearch(searchQuery: String)

    @Query("DELETE FROM questions")
    suspend fun clearAllQuestions()

    @Query("SELECT COUNT(*) FROM questions WHERE searchQuery = :searchQuery")
    suspend fun getQuestionsCount(searchQuery: String): Int

    @Query("SELECT * FROM questions WHERE id = :questionId")
    suspend fun getQuestionById(questionId: Int): QuestionEntity?

    @Query("""
        SELECT * FROM questions 
        WHERE difficulty = :difficulty 
        ORDER BY title ASC 
        LIMIT :limit
    """)
    suspend fun getQuestionsByDifficulty(difficulty: String, limit: Int = 50): List<QuestionEntity>

    @Query("""
        UPDATE questions 
        SET status = :status 
        WHERE id = :questionId
    """)
    suspend fun updateQuestionStatus(questionId: Int, status: String)

    @Query("""
        SELECT DISTINCT difficulty FROM questions 
        ORDER BY 
            CASE difficulty
                WHEN 'Easy' THEN 1
                WHEN 'Medium' THEN 2  
                WHEN 'Hard' THEN 3
                ELSE 4
            END
    """)
    suspend fun getAllDifficulties(): List<String>

    @Query("""
        SELECT DISTINCT topicTags FROM questions
        WHERE topicTags != '[]' AND topicTags IS NOT NULL
    """)
    suspend fun getAllTopicTagsRaw(): List<String>
}
