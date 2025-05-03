package com.devrachit.ken.domain.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class UserProfileCalendarResponse(
    val data: UserProfileCalendarData? = null
)

@Serializable
data class UserProfileCalendarData(
    val matchedUser: MatchedUserCalender? = null
)

@Serializable
data class MatchedUserCalender(
    val userCalendar: UserCalendar? = null
)

@Serializable
data class UserCalendar(
    val activeYears: List<Int> = emptyList(),
    val streak: Int = 0,
    val totalActiveDays: Int = 0,
    @SerialName("dccBadges")
    val dccBadges: List<DCCBadge> = emptyList(),
    val submissionCalendar: String = "{}"
) {
    fun getSubmissionCalendarMap(): Map<String, Int> {
        return try {
            val jsonObject = Json.decodeFromString<JsonObject>(submissionCalendar)
            jsonObject.mapKeys { it.key }
                .mapValues { entry -> entry.value.jsonPrimitive.content.toInt() }
        } catch (e: Exception) {
            emptyMap()
        }
    }
}

@Serializable
data class DCCBadge(
    val timestamp: Long = 0L,
    val badge: Badge? = null
)

@Serializable
data class Badge(
    val name: String = "",
    val icon: String = ""
)