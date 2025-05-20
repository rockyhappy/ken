package com.devrachit.ken.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devrachit.ken.domain.models.BadgeName
import com.devrachit.ken.domain.models.UserContestRanking
import com.devrachit.ken.domain.models.UserContestRankingData
import com.devrachit.ken.domain.models.UserContestRankingResponse

@Entity(tableName = "user_contest_ranking")
data class UserContestRankingEntity(
    @PrimaryKey
    val username: String,
    val attendedContestsCount: Int,
    val rating: Double,
    val globalRanking: Int,
    val totalParticipants: Int,
    val topPercentage: Double,
    val badgeName: String?,
    val lastFetchTime: Long = System.currentTimeMillis()
) {
    fun toDomainModel(): UserContestRankingResponse {
        return UserContestRankingResponse(
            data = UserContestRankingData(
                userContestRanking = UserContestRanking(
                    attendedContestsCount = attendedContestsCount,
                    rating = rating,
                    globalRanking = globalRanking,
                    totalParticipants = totalParticipants,
                    topPercentage = topPercentage,
                    badge = BadgeName(name = badgeName)
                )
            )
        )
    }

    companion object {
        fun fromDomainModel(username: String, response: UserContestRankingResponse, cacheTimestamp: Long = System.currentTimeMillis()): UserContestRankingEntity {
            val ranking = response.data.userContestRanking
            return UserContestRankingEntity(
                username = username,
                attendedContestsCount = ranking.attendedContestsCount,
                rating = ranking.rating,
                globalRanking = ranking.globalRanking,
                totalParticipants = ranking.totalParticipants,
                topPercentage = ranking.topPercentage,
                badgeName = ranking.badge?.name ,
                lastFetchTime = cacheTimestamp
            )
        }
    }
}