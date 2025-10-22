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

//        val getUserContestRankingQuery = """
//    query userContestRankingInfo(${"$"}username: String!) {
//      userContestRanking(username: ${"$"}username) {
//        attendedContestsCount
//        rating
//        globalRanking
//        totalParticipants
//        topPercentage
//        badge {
//          name
//        }
//      }
//      userContestRankingHistory(username: ${"$"}username) {
//        attended
//        trendDirection
//        problemsSolved
//        totalProblems
//        finishTimeInSeconds
//        rating
//        ranking
//        contest {
//          title
//          startTime
//        }
//      }
//    }
//    """.trimIndent()
        val getUserContestRankingQuery = """
    query userContestRankingInfo(${"$"}username: String!) {
      userContestRanking(username: ${"$"}username) {
        attendedContestsCount
        rating
        globalRanking
        totalParticipants
        topPercentage
        badge {
          name
        }
      }
    }
    """.trimIndent()

        fun getUserContestRankingJsonRequest(username: String): JSONObject {
            return JSONObject().apply {
                put("query", getUserContestRankingQuery)
                put("operationName", "userContestRankingInfo")
                put("variables", JSONObject().put("username", username))
            }
        }

        val getRecentAcSubmissionsQuery = """
    query recentAcSubmissions(${"$"}username: String!, ${"$"}limit: Int!) {
      recentAcSubmissionList(username: ${"$"}username, limit: ${"$"}limit) {
        id
        title
        titleSlug
        timestamp
      }
    }
    """.trimIndent()

        fun getRecentAcSubmissionsJsonRequest(username: String, limit: Int? = 15): JSONObject {
            return JSONObject().apply {
                put("query", getRecentAcSubmissionsQuery)
                put("operationName", "recentAcSubmissions")
                put("variables", JSONObject().put("username", username).put("limit", limit))
            }
        }

        val getContestRatingHistogramQuery = """
    query contestRatingHistogram {
      contestRatingHistogram {
        userCount
        ratingStart
        ratingEnd
        topPercentage
      }
    }
    """.trimIndent()

        fun getContestRatingHistogramJsonRequest(): JSONObject {
            return JSONObject().apply {
                put("query", getContestRatingHistogramQuery)
                put("operationName", "contestRatingHistogram")
                put("variables", JSONObject())
            }
        }



        val getUserBadgesQuery = """
    query userBadges(${"$"}username: String!) {
      matchedUser(username: ${"$"}username) {
        badges {
          id
          name
          shortName
          displayName
          icon
          hoverText
          medal {
            slug
            config {
              iconGif
              iconGifBackground
            }
          }
          creationDate
          category
        }
        upcomingBadges {
          name
          icon
          progress
        }
      }
    }
    """.trimIndent()

        fun getUserBadgesJsonRequest(username: String): JSONObject {
            return JSONObject().apply {
                put("query", getUserBadgesQuery)
                put("operationName", "userBadges")
                put("variables", JSONObject().put("username", username))
            }
        }

        val getDailyCodingChallengeQuery = """
    query dailyCodingQuestionRecords(${"$"}year: Int!, ${"$"}month: Int!) {
      dailyCodingChallengeV2(year: ${"$"}year, month: ${"$"}month) {
        challenges {
          date
          userStatus
          link
          question {
            questionFrontendId
            title
            titleSlug
            difficulty
          }
        }
        weeklyChallenges {
          date
          userStatus
          link
          question {
            questionFrontendId
            title
            titleSlug
            isPaidOnly
            difficulty
          }
        }
      }
    }
    """.trimIndent()

        fun getDailyCodingChallengeJsonRequest(year: Int, month: Int): JSONObject {
            return JSONObject().apply {
                put("query", getDailyCodingChallengeQuery)
                put("operationName", "dailyCodingQuestionRecords")
                put("variables", JSONObject().put("year", year).put("month", month))
            }
        }

        val questionsQuery = """
            query problemsetQuestionList(${"$"}categorySlug: String, ${"$"}limit: Int, ${"$"}skip: Int, ${"$"}filters: QuestionListFilterInput) {
                problemsetQuestionList: questionList(
                    categorySlug: ${"$"}categorySlug
                    limit: ${"$"}limit
                    skip: ${"$"}skip
                    filters: ${"$"}filters
                ) {
                    total: totalNum
                    questions: data {
                        acRate
                        difficulty
                        freqBar
                        frontendQuestionId: questionFrontendId
                        isFavor
                        paidOnly: isPaidOnly
                        status
                        title
                        titleSlug
                        topicTags {
                            name
                            id
                            slug
                        }
                        hasSolution
                        hasVideoSolution
                    }
                }
            }
        """.trimIndent()

        fun getQuestionsJsonRequest(
            query: String = "",
            difficulty: String? = null,
            status: String? = null,
            tags: List<String> = emptyList(),
            limit: Int = 50,
            offset: Int = 0
        ): JSONObject {
            return JSONObject().apply {
                put("query", questionsQuery)
                put("variables", JSONObject().apply {
                    put("categorySlug", "")
                    put("limit", limit)
                    put("skip", offset)
                    put("filters", JSONObject().apply {
                        if (query.isNotEmpty()) {
                            put("searchKeywords", query)
                        }
                        if (difficulty != null) {
                            put("difficulty", difficulty.uppercase())
                        }
                        if (status != null) {
                            put("status", status.uppercase())
                        }
                        if (tags.isNotEmpty()) {
                            put("tags", org.json.JSONArray(tags))
                        }
                    })
                })
            }
        }

        val searchQuestionsQuery = """
    query problemsetQuestionListV2(${"$"}filters: QuestionFilterInput, ${"$"}limit: Int, ${"$"}searchKeyword: String, ${"$"}skip: Int, ${"$"}sortBy: QuestionSortByInput, ${"$"}categorySlug: String) {
  problemsetQuestionListV2(
    filters: ${"$"}filters
    limit: ${"$"}limit
    searchKeyword: ${"$"}searchKeyword
    skip: ${"$"}skip
    sortBy: ${"$"}sortBy
    categorySlug: ${"$"}categorySlug
  ) {
    questions {
      id
      titleSlug
      title
      translatedTitle
      questionFrontendId
      paidOnly
      difficulty
      topicTags {
        name
        slug
        nameTranslated
      }
      status
      isInMyFavorites
      frequency
      acRate
      contestPoint
    }
    totalLength
    finishedLength
    hasMore
  }
}
    """.trimIndent()

        fun getSearchQuestionsJsonRequest(searchKeyword: String, limit: Int = 50): JSONObject {
            return JSONObject().apply {
                put("query", searchQuestionsQuery)
                put("operationName", "problemsetQuestionListV2")
                put("variables", JSONObject().apply {
                    put("skip", 0)
                    put("limit", limit)
                    put("categorySlug", "")
                    put("filters", JSONObject().apply {
                        put("filterCombineType", "ALL")
                        put("statusFilter", JSONObject().apply {
                            put("questionStatuses", org.json.JSONArray())
                            put("operator", "IS")
                        })
                        put("difficultyFilter", JSONObject().apply {
                            put("difficulties", org.json.JSONArray())
                            put("operator", "IS")
                        })
                        put("languageFilter", JSONObject().apply {
                            put("languageSlugs", org.json.JSONArray())
                            put("operator", "IS")
                        })
                        put("topicFilter", JSONObject().apply {
                            put("topicSlugs", org.json.JSONArray())
                            put("operator", "IS")
                        })
                        put("acceptanceFilter", JSONObject())
                        put("frequencyFilter", JSONObject())
                        put("frontendIdFilter", JSONObject())
                        put("lastSubmittedFilter", JSONObject())
                        put("publishedFilter", JSONObject())
                        put("companyFilter", JSONObject().apply {
                            put("companySlugs", org.json.JSONArray())
                            put("operator", "IS")
                        })
                        put("positionFilter", JSONObject().apply {
                            put("positionSlugs", org.json.JSONArray())
                            put("operator", "IS")
                        })
                        put("contestPointFilter", JSONObject().apply {
                            put("contestPoints", org.json.JSONArray())
                            put("operator", "IS")
                        })
                        put("premiumFilter", JSONObject().apply {
                            put("premiumStatus", org.json.JSONArray())
                            put("operator", "IS")
                        })
                    })
                    put("searchKeyword", searchKeyword)
                    put("sortBy", JSONObject().apply {
                        put("sortField", "FRONTEND_ID")
                        put("sortOrder", "ASCENDING")
                    })
                })
            }
        }

    }
}