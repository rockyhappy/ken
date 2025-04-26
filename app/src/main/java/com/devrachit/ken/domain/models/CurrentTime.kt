package com.devrachit.ken.domain.models

data class CurrentTimeResponse(
    val data: CurrentTimeData
)

data class CurrentTimeData(
    val currentTimestamp: Double
)