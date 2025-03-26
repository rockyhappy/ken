package com.devrachit.ken.domain.repository.remote

import com.devrachit.ken.data.remote.queries.GraphqlQuery
import com.devrachit.ken.data.remote.services.LeetcodeApiService
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserInfoResponse
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import javax.inject.Inject


interface LeetcodeRemoteRepository {
    suspend fun fetchUserInfo(username: String): Resource<LeetCodeUserInfo>
}