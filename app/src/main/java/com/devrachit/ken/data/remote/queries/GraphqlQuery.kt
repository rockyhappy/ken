package com.devrachit.ken.data.remote.queries

import org.json.JSONObject

class GraphqlQuery {


    companion object {

        val userExistsQuery = """
     query userInfo(${"$"}username: String!) {
         matchedUser(username: ${"$"}username) {
             contestBadge {
                 name
                 expired
                 hoverText
                 icon
             }
             username
             githubUrl
             twitterUrl
             linkedinUrl
             profile {
                 ranking
                 userAvatar
                 realName
                 aboutMe
                 school
                 websites
                 countryName
                 company
                 jobTitle
                 skillTags
                 postViewCount
                 postViewCountDiff
                 reputation
                 reputationDiff
                 solutionCount
                 solutionCountDiff
                 categoryDiscussCount
                 categoryDiscussCountDiff
                 certificationLevel
             }
         }
     }
 """.trimIndent()


        fun getUserExistsJsonRequest(username: String): JSONObject {
            return JSONObject().apply {
                put("query", userExistsQuery)
                put("operationName", "userInfo")
                put("variables", JSONObject().put("username", username))
            }
        }


        val userQuestionCountQuery = """
    query userSessionProgress(${"$"}username: String!) {
      allQuestionsCount {
        difficulty
        count
      }
      matchedUser(username: ${"$"}username) {
        submitStats {
          acSubmissionNum {
            difficulty
            count
            submissions
          }
          totalSubmissionNum {
            difficulty
            count
            submissions
          }
        }
      }
    }
    """.trimIndent()

        fun getUserQuestionCountJsonRequest(username: String): JSONObject {
            return JSONObject().apply {
                put("query", userQuestionCountQuery)
                put("operationName", "userSessionProgress")
                put("variables", JSONObject().put("username", username))
            }
        }

        val currentDataQuery = """
        query currentTimestamp {
          currentTimestamp
        }
    """.trimIndent()

        fun getCurrentDataJsonRequest(): JSONObject {
            return JSONObject().apply {
                put("query", currentDataQuery)
                put("operationName", "currentTimestamp")
                put("variables", JSONObject())
            }
        }

        val getUserProfileCalenderQuery = """
    query userProfileCalendar(${"$"}username: String!, ${"$"}year: Int) {
      matchedUser(username: ${"$"}username) {
        userCalendar(year: ${"$"}year) {
          activeYears
          streak
          totalActiveDays
          dccBadges {
            timestamp
            badge {
              name
              icon
            }
          }
          submissionCalendar
        }
      }
    }
    """.trimIndent()

        fun getUserProfileCalendarJsonRequest(username: String, year: Int? = null): JSONObject {
            val variables = JSONObject().put("username", username)
            if (year != null) {
                variables.put("year", year)
            }
            return JSONObject().apply {
                put("query", getUserProfileCalenderQuery)
                put("operationName", "userProfileCalendar")
                put("variables", variables)
            }
        }
    }
}