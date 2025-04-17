package com.devrachit.ken.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.devrachit.ken.domain.models.MatchedUser
import com.devrachit.ken.domain.models.QuestionCount
import com.devrachit.ken.domain.models.UserQuestionStatusData
import java.io.Serializable

@Entity(tableName = "user_question_status")
@TypeConverters(LeetCodeConverters::class)
data class UserQuestionStatusEntity(
    @PrimaryKey val username: String,
    val allQuestionsCount: List<QuestionCountEntity>?,
    val matchedUser: MatchedUserEntity?,
    val lastFetchTime: Long
) : Serializable {
    fun toDomainModel(): UserQuestionStatusData {
        return UserQuestionStatusData(
            allQuestionsCount = allQuestionsCount?.map { it.toDomainModel() },
            matchedUser = matchedUser?.toDomainModel()
        )
    }

    companion object {
        fun fromDomainModel(username: String, domainModel: UserQuestionStatusData, cacheTimestamp: Long = System.currentTimeMillis()): UserQuestionStatusEntity {
            return UserQuestionStatusEntity(
                username = username,
                allQuestionsCount = domainModel.allQuestionsCount?.map { QuestionCountEntity.fromDomainModel(it) },
                matchedUser = domainModel.matchedUser?.let { MatchedUserEntity.fromDomainModel(it) },
                lastFetchTime = cacheTimestamp
            )
        }
    }
}

@kotlinx.serialization.Serializable
data class QuestionCountEntity(
    val difficulty: String,
    val count: Int
)  {
    fun toDomainModel(): QuestionCount {
        return QuestionCount(
            difficulty = difficulty,
            count = count
        )
    }

    companion object {
        fun fromDomainModel(domainModel: QuestionCount): QuestionCountEntity {
            return QuestionCountEntity(
                difficulty = domainModel.difficulty,
                count = domainModel.count
            )
        }
    }
}
@kotlinx.serialization.Serializable
data class MatchedUserEntity(
    val submitStats: SubmitStatsEntity?
)  {
    fun toDomainModel(): MatchedUser {
        return MatchedUser(
            submitStats = submitStats?.toDomainModel()
        )
    }

    companion object {
        fun fromDomainModel(domainModel: MatchedUser): MatchedUserEntity {
            return MatchedUserEntity(
                submitStats = domainModel.submitStats?.let { SubmitStatsEntity.fromDomainModel(it) }
            )
        }
    }
}
@kotlinx.serialization.Serializable
data class SubmitStatsEntity(
    val acSubmissionNum: List<SubmissionStatEntity>?,
    val totalSubmissionNum: List<SubmissionStatEntity>?
) : Serializable {
    fun toDomainModel(): com.devrachit.ken.domain.models.SubmitStats {
        return com.devrachit.ken.domain.models.SubmitStats(
            acSubmissionNum = acSubmissionNum?.map { it.toDomainModel() },
            totalSubmissionNum = totalSubmissionNum?.map { it.toDomainModel() }
        )
    }

    companion object {
        fun fromDomainModel(domainModel: com.devrachit.ken.domain.models.SubmitStats): SubmitStatsEntity {
            return SubmitStatsEntity(
                acSubmissionNum = domainModel.acSubmissionNum?.map { SubmissionStatEntity.fromDomainModel(it) },
                totalSubmissionNum = domainModel.totalSubmissionNum?.map { SubmissionStatEntity.fromDomainModel(it) }
            )
        }
    }
}
@kotlinx.serialization.Serializable
data class SubmissionStatEntity(
    val difficulty: String,
    val count: Int,
    val submissions: Int
) : Serializable {
    fun toDomainModel(): com.devrachit.ken.domain.models.SubmissionStat {
        return com.devrachit.ken.domain.models.SubmissionStat(
            difficulty = difficulty,
            count = count,
            submissions = submissions
        )
    }

    companion object {
        fun fromDomainModel(domainModel: com.devrachit.ken.domain.models.SubmissionStat): SubmissionStatEntity {
            return SubmissionStatEntity(
                difficulty = domainModel.difficulty,
                count = domainModel.count,
                submissions = domainModel.submissions
            )
        }
    }
}