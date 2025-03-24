package com.devrachit.ken.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LeetCodeUserInfo(
    val username: String?=null,
    val lastName: String? = null,
    val firstName: String? = null,
    val githubUrl: String? = null,
    val twitterUrl: String? = null,
    val linkedinUrl: String? = null,
    val contestBadge: ContestBadge? = null,
    val profile: UserProfile? = null,
    val contributions: UserContributions? = null
)

@Serializable
data class ContestBadge(
    val name: String? = null,
    val expired: Boolean? = null,
    val hoverText: String? = null,
    val icon: String? = null
)

@Serializable
data class UserProfile(
    val ranking: Int? = null,
    val userAvatar: String? = null,
    val countryName: String? = null,
    val company: String? = null,
    val school: String? = null,
    val realName: String? = null,
    val aboutMe: String? = null,
    val websites: List<String>? = null,
    val jobTitle: String? = null,
    val skillTags: List<String>? = null,
    val postViewCount: Int? = null,
    val postViewCountDiff: Int? = null,
    val reputation: Int? = null,
    val reputationDiff: Int? = null,
    val solutionCount: Int? = null,
    val solutionCountDiff: Int? = null,
    val categoryDiscussCount: Int? = null,
    val categoryDiscussCountDiff: Int? = null,
    val certificationLevel: String? = null
)

@Serializable
data class UserContributions(
    val points: Int? = null,
    val questionCount: Int? = null
)

@Serializable
data class UserInfoResponse(
    val data: UserInfoData? = null,
    val errors: List<GraphQLError>? = null
)

@Serializable
data class UserInfoData(
    @SerialName("matchedUser")
    val matchedUser: LeetCodeUserInfo? = null
)

@Serializable
data class GraphQLError(
    val message: String
)