package com.devrachit.ken.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val acRate: Double,
    val difficulty: String,
    val freqBar: Double? = null,
    val frontendQuestionId: String,
    val isFavor: Boolean = false,
    val paidOnly: Boolean = false,
    val status: String? = null,
    val title: String,
    val titleSlug: String,
    val topicTags: List<TopicTag> = emptyList(),
    val hasSolution: Boolean = false,
    val hasVideoSolution: Boolean = false
) {
    // Computed properties for backward compatibility
    val id: Int get() = frontendQuestionId.toIntOrNull() ?: 0
    val acceptanceRate: Double get() = acRate
    val likes: Int get() = 0 // Not available in API
    val dislikes: Int get() = 0 // Not available in API
    val isPaidOnly: Boolean get() = paidOnly
}

@Serializable
data class TopicTag(
    val name: String,
    val id: String,
    val slug: String
)

@Serializable
data class ProblemsetQuestionListData(
    val total: Int,
    val questions: List<Question>
)

@Serializable
data class QuestionsApiResponse(
    val data: ProblemsetQuestionListResponseData
)

@Serializable
data class ProblemsetQuestionListResponseData(
    val problemsetQuestionList: ProblemsetQuestionListData
)

// Simplified response for our use case
@Serializable
data class QuestionsResponse(
    val questions: List<Question>,
    val totalCount: Int,
    val hasNext: Boolean
)

@Serializable
data class QuestionSearchRequest(
    val query: String = "",
    val difficulty: String? = null,
    val status: String? = null,
    val tags: List<String> = emptyList(),
    val limit: Int = 50,
    val offset: Int = 0
)

// Search-specific response matching LeetCode search API
@Serializable
data class SearchQuestionNode(
    val id: String,
    val titleSlug: String,
    val title: String,
    val translatedTitle: String? = null,
    val questionFrontendId: String,
    val paidOnly: Boolean,
    val difficulty: String,
    val topicTags: List<SearchTopicTag>,
    val status: String? = null,
    val isInMyFavorites: Boolean = false,
    val frequency: Double? = null,
    val acRate: Double,
    val contestPoint: Double? = null
)

@Serializable
data class SearchTopicTag(
    val name: String,
    val slug: String,
    val nameTranslated: String? = null
)

@Serializable
data class SearchQuestionsListData(
    val questions: List<SearchQuestionNode>,
    val totalLength: Int,
    val finishedLength: Int = 0,
    val hasMore: Boolean = false
)

@Serializable
data class SearchQuestionsResponseData(
    val problemsetQuestionListV2: SearchQuestionsListData
)

@Serializable
data class SearchQuestionsResponse(
    val data: SearchQuestionsResponseData
)
