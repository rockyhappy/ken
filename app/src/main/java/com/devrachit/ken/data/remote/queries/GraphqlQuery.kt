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
    }
}