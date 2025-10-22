package com.devrachit.ken.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.devrachit.ken.domain.models.Question
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val titleSlug: String,
    val difficulty: String,
    val status: String? = null,
    val acceptanceRate: Double,
    val frequency: Double? = null,
    val likes: Int,
    val dislikes: Int,
    val topicTags: String, // JSON string of List<String>
    val companies: String, // JSON string of List<String>
    val isPaidOnly: Boolean = false,
    val frontendQuestionId: String,
    val categoryTitle: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val searchQuery: String? = null // To support search-based caching
)

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey
    val questionId: Int,
    val prevKey: Int?,
    val nextKey: Int?,
    val searchQuery: String? = null
)

// Extension functions to convert between domain and entity
fun QuestionEntity.toDomain(): Question {
    return Question(
        acRate = acceptanceRate,
        difficulty = difficulty,
        freqBar = frequency,
        frontendQuestionId = frontendQuestionId,
        isFavor = false,
        paidOnly = isPaidOnly,
        status = status,
        title = title,
        titleSlug = titleSlug,
        topicTags = try {
            val tagNames: List<String> = Json.decodeFromString(topicTags)
            tagNames.map { name ->
                com.devrachit.ken.domain.models.TopicTag(
                    name = name,
                    id = name.lowercase().replace(" ", "-"),
                    slug = name.lowercase().replace(" ", "-")
                )
            }
        } catch (e: Exception) {
            emptyList()
        },
        hasSolution = false,
        hasVideoSolution = false
    )
}

fun Question.toEntity(searchQuery: String? = null): QuestionEntity {
    return QuestionEntity(
        id = id,
        title = title,
        titleSlug = titleSlug,
        difficulty = difficulty,
        status = status,
        acceptanceRate = acceptanceRate,
        frequency = freqBar,
        likes = 0, // Not available in new API
        dislikes = 0, // Not available in new API
        topicTags = Json.encodeToString(topicTags.map { it.name }),
        companies = Json.encodeToString(emptyList<String>()), // Not available in new API
        isPaidOnly = isPaidOnly,
        frontendQuestionId = frontendQuestionId,
        categoryTitle = null,
        searchQuery = searchQuery
    )
}

class QuestionConverters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }
}
