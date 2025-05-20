package com.devrachit.ken.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UserContestRankingResponse(
    val data: UserContestRankingData
)

@Serializable
data class UserContestRankingData(
    val userContestRanking: UserContestRanking
)

@Serializable
data class UserContestRanking(
    val attendedContestsCount: Int,
    val rating: Double,
    val globalRanking: Int,
    val totalParticipants: Int,
    val topPercentage: Double,
    val badge: BadgeName?
)

@Serializable
data class BadgeName(
    val name: String?
)