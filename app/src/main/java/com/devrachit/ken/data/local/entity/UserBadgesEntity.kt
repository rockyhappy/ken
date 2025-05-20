package com.devrachit.ken.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.devrachit.ken.domain.models.Medal
import com.devrachit.ken.domain.models.MedalConfig
import com.devrachit.ken.domain.models.UpcomingBadge
import com.devrachit.ken.domain.models.UserBadges
import com.devrachit.ken.domain.models.UserCentricBadge
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Entity(tableName = "user_badges")
@TypeConverters(UserBadgesConverters::class)
@Serializable
data class UserBadgesEntity(
    @PrimaryKey val username: String,
    val badges: List<UserCentricBadgeEntity>? = null,
    val upcomingBadges: List<UpcomingBadgeEntity>? = null,
    val lastFetchTime: Long = 0
) {
    fun toDomainModel(): UserBadges {
        return UserBadges(
            badges = badges?.map { it.toDomainModel() },
            upcomingBadges = upcomingBadges?.map { it.toDomainModel() }
        )
    }
    
    companion object {
        fun fromDomainModel(username: String, domainModel: UserBadges, cacheTimestamp: Long = System.currentTimeMillis()): UserBadgesEntity {
            return UserBadgesEntity(
                username = username,
                badges = domainModel.badges?.map { UserCentricBadgeEntity.fromDomainModel(it) },
                upcomingBadges = domainModel.upcomingBadges?.map { UpcomingBadgeEntity.fromDomainModel(it) },
                lastFetchTime = cacheTimestamp
            )
        }
    }
}
@Serializable
data class UserCentricBadgeEntity(
    val id: String?,
    val name: String?,
    val shortName: String?,
    val displayName: String?,
    val icon: String?,
    val hoverText: String?,
    val medal: MedalEntity?,
    val creationDate: String?,
    val category: String?
) {
    fun toDomainModel(): UserCentricBadge {
        return UserCentricBadge(
            id = id,
            name = name,
            shortName = shortName,
            displayName = displayName,
            icon = icon,
            hoverText = hoverText,
            medal = medal?.toDomainModel(),
            creationDate = creationDate,
            category = category
        )
    }
    
    companion object {
        fun fromDomainModel(domainModel: UserCentricBadge): UserCentricBadgeEntity {
            return UserCentricBadgeEntity(
                id = domainModel.id,
                name = domainModel.name,
                shortName = domainModel.shortName,
                displayName = domainModel.displayName,
                icon = domainModel.icon,
                hoverText = domainModel.hoverText,
                medal = domainModel.medal?.let { MedalEntity.fromDomainModel(it) },
                creationDate = domainModel.creationDate,
                category = domainModel.category
            )
        }
    }
}
@Serializable
data class MedalEntity(
    val slug: String?,
    val config: MedalConfigEntity?
) {
    fun toDomainModel(): Medal {
        return Medal(
            slug = slug,
            config = config?.toDomainModel()
        )
    }
    
    companion object {
        fun fromDomainModel(domainModel: Medal): MedalEntity {
            return MedalEntity(
                slug = domainModel.slug,
                config = domainModel.config?.let { MedalConfigEntity.fromDomainModel(it) }
            )
        }
    }
}
@Serializable
data class MedalConfigEntity(
    val iconGif: String?,
    val iconGifBackground: String?
) {
    fun toDomainModel(): MedalConfig {
        return MedalConfig(
            iconGif = iconGif,
            iconGifBackground = iconGifBackground
        )
    }
    
    companion object {
        fun fromDomainModel(domainModel: MedalConfig): MedalConfigEntity {
            return MedalConfigEntity(
                iconGif = domainModel.iconGif,
                iconGifBackground = domainModel.iconGifBackground
            )
        }
    }
}
@Serializable
data class UpcomingBadgeEntity(
    val name: String?,
    val icon: String?,
    val progress: Int?
) {
    fun toDomainModel(): UpcomingBadge {
        return UpcomingBadge(
            name = name,
            icon = icon,
            progress = progress
        )
    }
    
    companion object {
        fun fromDomainModel(domainModel: UpcomingBadge): UpcomingBadgeEntity {
            return UpcomingBadgeEntity(
                name = domainModel.name,
                icon = domainModel.icon,
                progress = domainModel.progress
            )
        }
    }
}

class UserBadgesConverters {
    private val json = Json { ignoreUnknownKeys = true }
    
    @TypeConverter
    fun fromUserCentricBadgeEntityList(value: List<UserCentricBadgeEntity>?): String? {
        return value?.let { json.encodeToString(it) }
    }
    
    @TypeConverter
    fun toUserCentricBadgeEntityList(value: String?): List<UserCentricBadgeEntity>? {
        return value?.let { json.decodeFromString<List<UserCentricBadgeEntity>>(it) }
    }
    
    @TypeConverter
    fun fromUpcomingBadgeEntityList(value: List<UpcomingBadgeEntity>?): String? {
        return value?.let { json.encodeToString(it) }
    }
    
    @TypeConverter
    fun toUpcomingBadgeEntityList(value: String?): List<UpcomingBadgeEntity>? {
        return value?.let { json.decodeFromString<List<UpcomingBadgeEntity>>(it) }
    }
    
    @TypeConverter
    fun fromMedalEntity(value: MedalEntity?): String? {
        return value?.let { json.encodeToString(it) }
    }
    
    @TypeConverter
    fun toMedalEntity(value: String?): MedalEntity? {
        return value?.let { json.decodeFromString<MedalEntity>(it) }
    }
    
    @TypeConverter
    fun fromMedalConfigEntity(value: MedalConfigEntity?): String? {
        return value?.let { json.encodeToString(it) }
    }
    
    @TypeConverter
    fun toMedalConfigEntity(value: String?): MedalConfigEntity? {
        return value?.let { json.decodeFromString<MedalConfigEntity>(it) }
    }
}