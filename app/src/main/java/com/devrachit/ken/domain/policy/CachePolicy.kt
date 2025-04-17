package com.devrachit.ken.domain.policy

import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CachePolicy @Inject constructor() {
    private val CACHE_TIMEOUT_MS = TimeUnit.MINUTES.toMillis(1) // 2 minutes cache validity
//    private val CACHE_TIMEOUT_MS = TimeUnit.HOURS.toMillis(2) // 2 minutes cache validity

    fun isCacheValid(lastFetchTime: Long?): Boolean {
        if (lastFetchTime == null) return false
        return System.currentTimeMillis() - lastFetchTime < CACHE_TIMEOUT_MS
    }
}