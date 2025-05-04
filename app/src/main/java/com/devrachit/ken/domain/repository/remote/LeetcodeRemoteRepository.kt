package com.devrachit.ken.domain.repository.remote

import com.devrachit.ken.data.remote.queries.GraphqlQuery
import com.devrachit.ken.data.remote.services.LeetcodeApiService
import com.devrachit.ken.domain.models.ContestRatingHistogramResponse
import com.devrachit.ken.domain.models.CurrentTimeResponse
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserContestRankingResponse
import com.devrachit.ken.domain.models.UserInfoResponse
import com.devrachit.ken.domain.models.UserProfileCalendarResponse
import com.devrachit.ken.domain.models.UserQuestionStatusData
import com.devrachit.ken.domain.models.UserRecentAcSubmissionResponse
import com.devrachit.ken.utility.NetworkUtility.Resource
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import javax.inject.Inject


interface LeetcodeRemoteRepository {
    suspend fun fetchUserInfo(username: String): Resource<LeetCodeUserInfo>
    suspend fun fetchUserRankingInfo(username: String): Resource<UserQuestionStatusData>
    suspend fun fetchCurrentData(): Resource<CurrentTimeResponse>
    suspend fun fetchUserProfileCalender(username : String): Resource<UserProfileCalendarResponse>
    suspend fun fetchUserRecentAcSubmissions(username: String, limit: Int?= 15): Resource<UserRecentAcSubmissionResponse>
    suspend fun fetchContestRankingHistogram(): Resource<ContestRatingHistogramResponse>
    suspend fun fetchUserContestRanking(username : String): Resource<UserContestRankingResponse>
    suspend fun fetchUserBadges(username: String): Resource<Any?>
}