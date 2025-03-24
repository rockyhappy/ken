package com.devrachit.ken.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserContributions
import com.devrachit.ken.domain.models.UserProfile

@Entity(tableName = "leetcode_users")
@TypeConverters(LeetCodeConverters::class)
data class LeetCodeUserEntity(
    @PrimaryKey val username: String,
    val firstName: String?,
    val lastName: String?,
    val firstName: String?,
    val githubUrl: String?,
    val twitterUrl: String?,
    val linkedinUrl: String?,
    
    @Embedded(prefix = "contest_badge_")
    val contestBadge: ContestBadgeEntity?,
    
    @Embedded(prefix = "profile_")
    val profile: UserProfileEntity?,
    
    @Embedded(prefix = "contributions_")
    val contributions: UserContributionsEntity?,
    
    val lastFetchTime: Long // Added for cache management
) {
    fun toDomainModel(): LeetCodeUserInfo {
        return LeetCodeUserInfo(
            username = username,
            firstName = firstName,
            lastName = lastName,
            firstName = firstName,
            githubUrl = githubUrl,
            twitterUrl = twitterUrl,
            linkedinUrl = linkedinUrl,
            contestBadge = contestBadge?.toDomainModel(),
            profile = profile?.toDomainModel(),
            contributions = contributions?.toDomainModel()
        )
    }
    
    companion object {
        fun fromDomainModel(domainModel: LeetCodeUserInfo, cacheTimestamp: Long = System.currentTimeMillis()): LeetCodeUserEntity {
            return LeetCodeUserEntity(
                username = domainModel.username,
                firstName = domainModel.firstName,
                lastName = domainModel.lastName,
                fullName = domainModel.fullName,
                avatarUrl = domainModel.avatarUrl,
                ranking = domainModel.ranking,
                countryName = domainModel.countryName,
                company = domainModel.company,
                school = domainModel.school,
                githubUrl = domainModel.githubUrl,
                twitterUrl = domainModel.twitterUrl,
                linkedinUrl = domainModel.linkedinUrl,
                contestBadge = domainModel.contestBadge?.let { ContestBadgeEntity.fromDomainModel(it) },
                profile = domainModel.profile?.let { UserProfileEntity.fromDomainModel(it) },
                contributions = domainModel.contributions?.let { UserContributionsEntity.fromDomainModel(it) },
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
    val countryName: String?,
    val company: String?,
    val jobTitle: String?,
    val websites: List<String>?,
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
            countryName = countryName,
            company = company,
            jobTitle = jobTitle,
            websites = websites,
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
                countryName = domainModel.countryName,
                company = domainModel.company,
                jobTitle = domainModel.jobTitle,
                websites = domainModel.websites,
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

data class UserContributionsEntity(
    val points: Int?,
    val questionCount: Int?
) {
    fun toDomainModel(): UserContributions {
        return UserContributions(
            points = points,
            questionCount = questionCount
        )
    }
    
    companion object {
        fun fromDomainModel(domainModel: UserContributions): UserContributionsEntity {
            return UserContributionsEntity(
                points = domainModel.points,
                questionCount = domainModel.questionCount
            )
        }
    }
}

/**
 * Type converters for Room to handle Lists and complex objects
 */
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
}