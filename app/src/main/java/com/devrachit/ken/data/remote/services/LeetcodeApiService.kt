package com.devrachit.ken.data.remote.services

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LeetcodeApiService {

    @Headers("Content-Type: application/json", "Referer: https://leetcode.com/")
    @POST("graphql")
    suspend fun fetchUser(@Body requestBody: RequestBody): ResponseBody

    @Headers("Content-Type: application/json", "Referer: https://leetcode.com/")
    @POST("graphql")
    suspend fun fetchUserQuestionCount(@Body requestBody: RequestBody): ResponseBody

    @Headers("Content-Type: application/json", "Referer: https://leetcode.com/")
    @POST("graphql")
    suspend fun fetchUserHeatMap(@Body requestBody: RequestBody): ResponseBody

    @Headers("Content-Type: application/json", "Referer: https://leetcode.com/")
    @POST("graphql")
    suspend fun fetchCurrentTime(@Body requestBody: RequestBody): ResponseBody

    @Headers("Content-Type: application/json", "Referer: https://leetcode.com/")
    @POST("graphql")
    suspend fun fetUserProfileCalender(@Body requestBody: RequestBody): ResponseBody
    
    @Headers("Content-Type: application/json", "Referer: https://leetcode.com/")
    @POST("graphql")
    suspend fun fetchRecentSubmissionList(@Body requestBody: RequestBody): ResponseBody

}
