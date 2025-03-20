package com.devrachit.ken.domain.repository

import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.utility.NetworkUtility.Resource

interface LeetcodeRepository {
    suspend fun getUserInfo(username: String): Resource<LeetCodeUserInfo>
}