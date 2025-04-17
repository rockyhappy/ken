package com.devrachit.ken.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.devrachit.ken.domain.models.ContestBadge
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.MatchedUser
import com.devrachit.ken.domain.models.QuestionCount
import com.devrachit.ken.domain.models.SubmitStats
import com.devrachit.ken.domain.models.SubmissionStat
import com.devrachit.ken.domain.models.UserProfile
import com.devrachit.ken.domain.models.UserQuestionStatusData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "leetcode_users")
@TypeConverters(LeetCodeConverters::class)
data class LeetCodeUserEntity(
    @PrimaryKey val username: String,
    val githubUrl: String?,
    val twitterUrl: String?,
    val linkedinUrl: String?,
    
    @Embedded(prefix = "contest_badge_")
    val contestBadge: ContestBadgeEntity?,
    
    @Embedded(prefix = "profile_")
    val profile: UserProfileEntity?,
    val lastFetchTime: Long
) {
    fun toDomainModel(): LeetCodeUserInfo {
        return LeetCodeUserInfo(
            username = username,
            githubUrl = githubUrl,
            twitterUrl = twitterUrl,
            linkedinUrl = linkedinUrl,
            contestBadge = contestBadge?.toDomainModel(),
            profile = profile?.toDomainModel()
        )
    }
    
    companion object {
        fun fromDomainModel(domainModel: LeetCodeUserInfo, cacheTimestamp: Long = System.currentTimeMillis()): LeetCodeUserEntity {
            return LeetCodeUserEntity(
                username = domainModel.username ?: "",
                githubUrl = domainModel.githubUrl,
                twitterUrl = domainModel.twitterUrl,
                linkedinUrl = domainModel.linkedinUrl,
                contestBadge = domainModel.contestBadge?.let { ContestBadgeEntity.fromDomainModel(it) },
                profile = domainModel.profile?.let { UserProfileEntity.fromDomainModel(it) },
                lastFetchTime = cacheTimestamp
            )
        }
    }
}

data class ContestBadgeEntity(
    val name: String?,
    val expired: Boolean?,
    val hoverText: String?,
    val icon: String?
) {
    fun toDomainModel(): ContestBadge {
        return ContestBadge(
            name = name,
            expired = expired,
            hoverText = hoverText,
            icon = icon
        )
    }
    
    companion object {
        fun fromDomainModel(domainModel: ContestBadge): ContestBadgeEntity {
            return ContestBadgeEntity(
                name = domainModel.name,
                expired = domainModel.expired,
                hoverText = domainModel.hoverText,
                icon = domainModel.icon
            )
        }
    }
}

data class UserProfileEntity(
    val ranking: Int?,
    val userAvatar: String?,
    val realName: String?,
    val aboutMe: String?,
    val school: String?,
    val websites: List<String>?,
    val countryName: String?,
    val company: String?,
    val jobTitle: String?,
    val skillTags: List<String>?,
    val postViewCount: Int?,
    val postViewCountDiff: Int?,
    val reputation: Int?,
    val reputationDiff: Int?,
    val solutionCount: Int?,
    val solutionCountDiff: Int?,
    val categoryDiscussCount: Int?,
    val categoryDiscussCountDiff: Int?,
    val certificationLevel: String?
) {
    fun toDomainModel(): UserProfile {
        return UserProfile(
            ranking = ranking,
            userAvatar = userAvatar,
            realName = realName,
            aboutMe = aboutMe,
            school = school,
            websites = websites,
            countryName = countryName,
            company = company,
            jobTitle = jobTitle,
            skillTags = skillTags,
            postViewCount = postViewCount,
            postViewCountDiff = postViewCountDiff,
            reputation = reputation,
            reputationDiff = reputationDiff,
            solutionCount = solutionCount,
            solutionCountDiff = solutionCountDiff,
            categoryDiscussCount = categoryDiscussCount,
            categoryDiscussCountDiff = categoryDiscussCountDiff,
            certificationLevel = certificationLevel
        )
    }
    
    companion object {
        fun fromDomainModel(domainModel: UserProfile): UserProfileEntity {
            return UserProfileEntity(
                ranking = domainModel.ranking,
                userAvatar = domainModel.userAvatar,
                realName = domainModel.realName,
                aboutMe = domainModel.aboutMe,
                school = domainModel.school,
                websites = domainModel.websites,
                countryName = domainModel.countryName,
                company = domainModel.company,
                jobTitle = domainModel.jobTitle,
                skillTags = domainModel.skillTags,
                postViewCount = domainModel.postViewCount,
                postViewCountDiff = domainModel.postViewCountDiff,
                reputation = domainModel.reputation,
                reputationDiff = domainModel.reputationDiff,
                solutionCount = domainModel.solutionCount,
                solutionCountDiff = domainModel.solutionCountDiff,
                categoryDiscussCount = domainModel.categoryDiscussCount,
                categoryDiscussCountDiff = domainModel.categoryDiscussCountDiff,
                certificationLevel = domainModel.certificationLevel
            )
        }
    }
}


class LeetCodeConverters {
    private val json = Json { ignoreUnknownKeys = true }
    
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { json.encodeToString(it) }
    }
    
    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let { json.decodeFromString<List<String>>(it) }
    }
    
    @TypeConverter
    fun fromQuestionCountList(value: List<QuestionCountEntity>?): String? {
        return value?.let { json.encodeToString(it) }
    }
    
    @TypeConverter
    fun toQuestionCountList(value: String?): List<QuestionCountEntity>? {
        return value?.let { json.decodeFromString<List<QuestionCountEntity>>(it) }
    }
    
    @TypeConverter
    fun fromSubmissionStatList(value: List<SubmissionStatEntity>?): String? {
        return value?.let { json.encodeToString(it) }
    }
    
    @TypeConverter
    fun toSubmissionStatList(value: String?): List<SubmissionStatEntity>? {
        return value?.let { json.decodeFromString<List<SubmissionStatEntity>>(it) }
    }
    
    @TypeConverter
    fun fromMatchedUserEntity(value: MatchedUserEntity?): String? {
        return value?.let { json.encodeToString(it) }
    }
    
    @TypeConverter
    fun toMatchedUserEntity(value: String?): MatchedUserEntity? {
        return value?.let { json.decodeFromString<MatchedUserEntity>(it) }
    }
    
    @TypeConverter
    fun fromSubmitStatsEntity(value: SubmitStatsEntity?): String? {
        return value?.let { json.encodeToString(it) }
    }
    
    @TypeConverter
    fun toSubmitStatsEntity(value: String?): SubmitStatsEntity? {
        return value?.let { json.decodeFromString<SubmitStatsEntity>(it) }
    }
}