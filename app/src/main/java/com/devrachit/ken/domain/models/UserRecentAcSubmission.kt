package com.devrachit.ken.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class UserRecentAcSubmissionResponse(
    val data: RecentAcSubmissionData
)

@Serializable
data class RecentAcSubmissionData(
    val recentAcSubmissionList: List<RecentAcSubmission>
)

@Serializable
data class RecentAcSubmission(
    val id: String,
    val title: String,
    val titleSlug: String,
    val timestamp: String
)