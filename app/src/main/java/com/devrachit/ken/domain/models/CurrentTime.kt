package com.devrachit.ken.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class CurrentTimeResponse(
    val data: CurrentTimeData
)
@Serializable
data class CurrentTimeData(
    val currentTimestamp: Double
)