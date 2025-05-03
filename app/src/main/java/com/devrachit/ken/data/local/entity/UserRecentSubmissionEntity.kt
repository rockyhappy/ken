package com.devrachit.ken.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.devrachit.ken.domain.models.RecentAcSubmission
import com.devrachit.ken.domain.models.RecentAcSubmissionData
import com.devrachit.ken.domain.models.UserRecentAcSubmissionResponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity(tableName = "user_recent_submissions")
data class UserRecentSubmissionEntity(
    @PrimaryKey val username: String,
    val recentSubmissionsJson: String, // Store as JSON string instead of List
    val lastFetchTime: Long
) {
    fun toDomainModel(): UserRecentAcSubmissionResponse {
        val submissions = if (recentSubmissionsJson.isEmpty()) {
            emptyList()
        } else {
            try {
                Json.decodeFromString<List<RecentSubmissionEntity>>(recentSubmissionsJson)
                    .map { it.toDomainModel() }
            } catch (e: Exception) {
                emptyList()
            }
        }
        
        return UserRecentAcSubmissionResponse(
            data = RecentAcSubmissionData(
                recentAcSubmissionList = submissions
            )
        )
    }

    companion object {
        fun fromDomainModel(username: String, domainModel: UserRecentAcSubmissionResponse, cacheTimestamp: Long = System.currentTimeMillis()): UserRecentSubmissionEntity {
            val submissionsEntities = domainModel.data.recentAcSubmissionList.map { 
                RecentSubmissionEntity.fromDomainModel(it) 
            }
            val submissionsJson = try {
                Json.encodeToString(submissionsEntities)
            } catch (e: Exception) {
                ""
            }
            
            return UserRecentSubmissionEntity(
                username = username,
                recentSubmissionsJson = submissionsJson,
                lastFetchTime = cacheTimestamp
            )
        }
    }
}

@Serializable
data class RecentSubmissionEntity(
    val id: String,
    val title: String,
    val titleSlug: String,
    val timestamp: String
) {
    fun toDomainModel(): RecentAcSubmission {
        return RecentAcSubmission(
            id = id,
            title = title,
            titleSlug = titleSlug,
            timestamp = timestamp
        )
    }

    companion object {
        fun fromDomainModel(domainModel: RecentAcSubmission): RecentSubmissionEntity {
            return RecentSubmissionEntity(
                id = domainModel.id,
                title = domainModel.title,
                titleSlug = domainModel.titleSlug,
                timestamp = domainModel.timestamp
            )
        }
    }
}