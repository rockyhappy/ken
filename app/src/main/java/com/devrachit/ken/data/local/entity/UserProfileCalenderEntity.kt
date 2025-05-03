package com.devrachit.ken.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.devrachit.ken.domain.models.Badge
import com.devrachit.ken.domain.models.DCCBadge
import com.devrachit.ken.domain.models.UserCalendar
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

@Entity(tableName = "user_calendars")
@TypeConverters(UserCalendarConverters::class)
data class UserProfileCalenderEntity(
    @PrimaryKey val username: String,
    val activeYears: List<Int>,
    val streak: Int,
    val totalActiveDays: Int,
    val dccBadges: List<DCCBadgeEntity>,
    val submissionCalendar: String,
    val lastFetchTime: Long
) {
    fun toDomainModel(): UserCalendar {
        return UserCalendar(
            activeYears = activeYears,
            streak = streak,
            totalActiveDays = totalActiveDays,
            dccBadges = dccBadges.map { it.toDomainModel() },
            submissionCalendar = submissionCalendar
        )
    }
    
    companion object {
        fun fromDomainModel(username: String, domainModel: UserCalendar, cacheTimestamp: Long = System.currentTimeMillis()): UserProfileCalenderEntity {
            return UserProfileCalenderEntity(
                username = username,
                activeYears = domainModel.activeYears,
                streak = domainModel.streak,
                totalActiveDays = domainModel.totalActiveDays,
                dccBadges = domainModel.dccBadges.map { DCCBadgeEntity.fromDomainModel(it) },
                submissionCalendar = domainModel.submissionCalendar,
                lastFetchTime = cacheTimestamp
            )
        }
    }
}
@Serializable
data class DCCBadgeEntity(
    val timestamp: Long,
    val badge: BadgeEntity?
) {
    fun toDomainModel(): DCCBadge {
        return DCCBadge(
            timestamp = timestamp,
            badge = badge?.toDomainModel()
        )
    }
    
    companion object {
        fun fromDomainModel(domainModel: DCCBadge): DCCBadgeEntity {
            return DCCBadgeEntity(
                timestamp = domainModel.timestamp,
                badge = domainModel.badge?.let { BadgeEntity.fromDomainModel(it) }
            )
        }
    }
}

@Serializable
data class BadgeEntity(
    val name: String,
    val icon: String
) {
    fun toDomainModel(): Badge {
        return Badge(
            name = name,
            icon = icon
        )
    }
    
    companion object {
        fun fromDomainModel(domainModel: Badge): BadgeEntity {
            return BadgeEntity(
                name = domainModel.name,
                icon = domainModel.icon
            )
        }
    }
}

class UserCalendarConverters {
    private val json = Json { ignoreUnknownKeys = true }
    
    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.let { json.encodeToString(it) }
    }
    
    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return value?.let { json.decodeFromString<List<Int>>(it) }
    }
    
    @TypeConverter
    fun fromDCCBadgeEntityList(value: List<DCCBadgeEntity>?): String? {
        return value?.let { json.encodeToString(it) }
    }
    
    @TypeConverter
    fun toDCCBadgeEntityList(value: String?): List<DCCBadgeEntity>? {
        return value?.let { json.decodeFromString<List<DCCBadgeEntity>>(it) }
    }
}