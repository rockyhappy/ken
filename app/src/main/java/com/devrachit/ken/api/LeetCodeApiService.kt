package com.devrachit.ken.api

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LeetCodeApiService {
    @Headers("Content-Type: application/json")
    @POST("graphql")
    suspend fun getUserCalendar(@Body query: GraphQLQuery): UserCalendarResponse

    companion object {
        private const val BASE_URL = "https://leetcode.com/"

        fun create(): LeetCodeApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LeetCodeApiService::class.java)
        }
    }
}

data class GraphQLQuery(val query: String, val variables: Map<String, Any>)

data class UserCalendarResponse(
    val data: Data
) {
    data class Data(
        @SerializedName("matchedUser") val user: User?
    )

    data class User(
        @SerializedName("userCalendar") val calendar: Calendar
    )

    data class Calendar(
        @SerializedName("activeYears") val activeYears: List<Int>,
        @SerializedName("streak") val streak: Int,
        @SerializedName("totalActiveDays") val totalActiveDays: Int,
        @SerializedName("submissionCalendar") val submissionCalendar: String
    )
}