package com.devrachit.ken.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserQuestionStatusResponse(
    val data: UserQuestionStatusData? = null,
    val errors: List<GraphQLError>? = null
)

@Serializable
data class UserQuestionStatusData(
    val allQuestionsCount: List<QuestionCount>? = null,
    val matchedUser: MatchedUser? = null
)

@Serializable
data class QuestionCount(
    val difficulty: String,
    val count: Int
)

@Serializable
data class MatchedUser(
    val submitStats: SubmitStats? = null
)

@Serializable
data class SubmitStats(
    val acSubmissionNum: List<SubmissionStat>? = null,
    val totalSubmissionNum: List<SubmissionStat>? = null
)

@Serializable
data class SubmissionStat(
    val difficulty: String,
    val count: Int,
    val submissions: Int
)