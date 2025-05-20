package com.devrachit.ken.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserBadgesResponse(
    val data: UserBadgesData? = null,
    val errors: List<GraphQLError>? = null
)

@Serializable
data class UserBadgesData(
    @SerialName("matchedUser")
    val matchedUser: UserBadges? = null
)

@Serializable
data class UserBadges(
    val badges: List<UserCentricBadge>? = null,
    val upcomingBadges: List<UpcomingBadge>? = null
)

@Serializable
data class UserCentricBadge(
    val id: String? = null,
    val name: String? = null,
    val shortName: String? = null,
    val displayName: String? = null,
    val icon: String? = null,
    val hoverText: String? = null,
    val medal: Medal? = null,
    val creationDate: String? = null,
    val category: String? = null
)

@Serializable
data class Medal(
    val slug: String? = null,
    val config: MedalConfig? = null
)

@Serializable
data class MedalConfig(
    val iconGif: String? = null,
    val iconGifBackground: String? = null
)

@Serializable
data class UpcomingBadge(
    val name: String? = null,
    val icon: String? = null,
    val progress: Int? = null
)