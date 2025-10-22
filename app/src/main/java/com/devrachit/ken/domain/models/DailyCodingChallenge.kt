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
    val titleSlug: String
)

@Serializable
data class WeeklyChallengeQuestion(
    val questionFrontendId: String,
    val title: String,
    val titleSlug: String,
    val isPaidOnly: Boolean
)
