package com.devrachit.ken.domain.models
import kotlinx.serialization.Serializable

@Serializable
data class ContestRatingHistogramResponse(
    val data: ContestRatingHistogramData
)
@Serializable
data class ContestRatingHistogramData(
    val contestRatingHistogram: List<RatingHistogramEntry>
)
@Serializable
data class RatingHistogramEntry(
    val userCount: Int,
    val ratingStart: Int,
    val ratingEnd: Int,
    val topPercentage: Double
)