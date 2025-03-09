package com.devrachit.ken.data

import com.devrachit.ken.api.GraphQLQuery
import com.devrachit.ken.api.LeetCodeApiService
import com.devrachit.ken.api.UserCalendarResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LeetCodeRepository(private val apiService: LeetCodeApiService) {

    suspend fun getUserHeatmap(username: String): Map<String, Int> {
        return withContext(Dispatchers.IO) {
            val query = """
                query userProfileCalendar(${'$'}username: String!) {
                  matchedUser(username: ${'$'}username) {
                    userCalendar {
                      activeYears
                      streak
                      totalActiveDays
                      submissionCalendar
                    }
                  }
                }
            """.trimIndent()

            val variables = mapOf("username" to username)
            val response = apiService.getUserCalendar(GraphQLQuery(query, variables))

            parseSubmissionCalendar(response.data.user?.calendar?.submissionCalendar ?: "{}")
        }
    }

    private fun parseSubmissionCalendar(calendarJson: String): Map<String, Int> {
        val result = mutableMapOf<String, Int>()
        val jsonObject = JSONObject(calendarJson)

        jsonObject.keys().forEach { timestamp ->
            val count = jsonObject.getInt(timestamp)
            // Convert timestamp to date string
            val date = java.time.Instant.ofEpochSecond(timestamp.toLong())
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate()
                .toString()
            result[date] = count
        }

        return result
    }
}