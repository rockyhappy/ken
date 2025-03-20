package com.devrachit.ken.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LeetCodeUserInfo(
    val username: String?=null,
    val lastName: String? = null,
    val firstName: String? = null,
    val profile: UserProfile? = null,
    val githubUrl: String? = null,
    val twitterUrl: String? = null,
    val linkedinUrl: String? = null,
    val contributions: UserContributions? = null
)

@Serializable
data class UserProfile(
    val ranking: Int? = null,
    val userAvatar: String? = null,
    val countryName: String? = null,
    val company: String? = null,
    val school: String? = null
)

@Serializable
data class UserContributions(
    val points: Int? = null,
    val questionCount: Int? = null
)

// Wrapper classes for GraphQL response
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