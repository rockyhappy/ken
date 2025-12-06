package com.devrachit.ken.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class DailyCodingChallengeResponse(
    val data: DailyCodingChallengeData
)

@Serializable
data class DailyCodingChallengeData(
    val dailyCodingChallengeV2: DailyCodingChallengeV2
)

@Serializable
data class DailyCodingChallengeV2(
    val challenges: List<DailyChallenge> = emptyList(),
    val weeklyChallenges: List<WeeklyChallenge> = emptyList()
)

@Serializable
data class DailyChallenge(
    val date: String,
    val userStatus: String,
    val link: String,
    val question: ChallengeQuestion
)

@Serializable
data class WeeklyChallenge(
    val date: String,
    val userStatus: String,
    val link: String,
    val question: WeeklyChallengeQuestion
)

@Serializable
data class ChallengeQuestion(
    val questionFrontendId: String,
    val title: String,
    val titleSlug: String,
    val difficulty: String = ""
)

@Serializable
data class WeeklyChallengeQuestion(
    val questionFrontendId: String,
    val title: String,
    val titleSlug: String,
    val isPaidOnly: Boolean,
    val difficulty: String = ""
)

// Models for questionOfTodayV2 API
@Serializable
data class TodayQuestionResponse(
    val data: TodayQuestionData
)

@Serializable
data class TodayQuestionData(
    val activeDailyCodingChallengeQuestion: TodayQuestion
)

@Serializable
data class TodayQuestion(
    val date: String,
    val userStatus: String,
    val link: String,
    val question: TodayQuestionDetails
)

@Serializable
data class TodayQuestionDetails(
    val id: String,
    val titleSlug: String,
    val title: String,
    val translatedTitle: String? = null,
    val questionFrontendId: String,
    val paidOnly: Boolean,
    val difficulty: String,
    val topicTags: List<SearchTopicTag> = emptyList(),
    val status: String? = null,
    val isInMyFavorites: Boolean = false,
    val acRate: Double = 0.0,
    val frequency: Double? = null
)

