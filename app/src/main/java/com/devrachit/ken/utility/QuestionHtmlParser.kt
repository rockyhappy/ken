package com.devrachit.ken.utility

import com.devrachit.ken.domain.models.QuestionDetails

object QuestionHtmlParser {
    
    fun parseQuestionDetails(html: String): QuestionDetails {
        try {
            val titleRegex = "<title[^>]*>([^<]+)</title>".toRegex()
            val title = titleRegex.find(html)?.groupValues?.get(1)
                ?.replace(" - LeetCode", "")
                ?.trim() ?: ""

            val descRegex = "<meta name=\"description\" content=\"([^\"]+)\"".toRegex()
            val description = descRegex.find(html)?.groupValues?.get(1)
                ?.replace("Can you solve this real interview question? ", "")
                ?.replace("&quot;", "\"")
                ?.replace("&lt;", "<")
                ?.replace("&gt;", ">")
                ?.replace("&amp;", "&")
                ?.replace("&#x27;", "'")
                ?.replace("\\n", "\n")
                ?.trim() ?: ""

            var difficulty = ""
            var acceptanceRate = ""
            var likes = 0
            var dislikes = 0
            var totalSubmissions = ""
            var totalAccepted = ""
            val topicTags = mutableListOf<String>()
            val hints = mutableListOf<String>()

            try {
                val jsonRegex = "<script id=\"__NEXT_DATA__\" type=\"application/json\">([^<]+)</script>".toRegex()
                val jsonMatch = jsonRegex.find(html)
                val jsonString = jsonMatch?.groupValues?.get(1)

                if (!jsonString.isNullOrEmpty()) {
                    difficulty = extractDifficulty(jsonString)
                    
                    val stats = extractStats(jsonString)
                    acceptanceRate = stats.acceptanceRate
                    totalSubmissions = stats.totalSubmissions
                    totalAccepted = stats.totalAccepted

                    likes = extractLikes(jsonString)
                    dislikes = extractDislikes(jsonString)
                    topicTags.addAll(extractTopicTags(jsonString))
                    hints.addAll(extractHints(jsonString))
                }
            } catch (e: Exception) {
                // Continue with basic parsing
            }

            return QuestionDetails(
                title = title,
                description = description,
                difficulty = difficulty,
                acceptanceRate = acceptanceRate,
                likes = likes,
                dislikes = dislikes,
                totalSubmissions = totalSubmissions,
                totalAccepted = totalAccepted,
                topicTags = topicTags.take(8),
                hints = hints,
                htmlContent = html
            )
        } catch (e: Exception) {
            return QuestionDetails(
                title = "Error parsing question",
                description = "Could not parse question details",
                htmlContent = html
            )
        }
    }

    private fun extractDifficulty(jsonString: String): String {
        val difficultyRegex = "\"difficulty\":\"(Easy|Medium|Hard)\"".toRegex()
        return difficultyRegex.find(jsonString)?.groupValues?.get(1) ?: ""
    }

    private data class Stats(
        val acceptanceRate: String = "",
        val totalSubmissions: String = "",
        val totalAccepted: String = ""
    )

    private fun extractStats(jsonString: String): Stats {
        val statsStringRegex = "\"stats\":\\s*\"((?:[^\"\\\\]|\\\\.)*)\"".toRegex()
        val statsStringMatch = statsStringRegex.find(jsonString)
        
        if (statsStringMatch != null) {
            val statsJsonString = statsStringMatch.groupValues[1]
                .replace("\\\\", "\\")
                .replace("\\\"", "\"")
            
            val acRateRegex = "\"acRate\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            val acceptanceRate = acRateRegex.find(statsJsonString)?.groupValues?.get(1) ?: ""
            
            val totalSubmissionRegex = "\"totalSubmission\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            val totalSubmissions = totalSubmissionRegex.find(statsJsonString)?.groupValues?.get(1) ?: ""
            
            val totalAcceptedRegex = "\"totalAccepted\"\\s*:\\s*\"([^\"]+)\"".toRegex()
            val totalAccepted = totalAcceptedRegex.find(statsJsonString)?.groupValues?.get(1) ?: ""
            
            return Stats(acceptanceRate, totalSubmissions, totalAccepted)
        }
        
        return Stats()
    }

    private fun extractLikes(jsonString: String): Int {
        val likesRegex = "\"likes\":(\\d+)".toRegex()
        return likesRegex.find(jsonString)?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }

    private fun extractDislikes(jsonString: String): Int {
        val dislikesRegex = "\"dislikes\":(\\d+)".toRegex()
        return dislikesRegex.find(jsonString)?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }

    private fun extractTopicTags(jsonString: String): List<String> {
        val tags = mutableListOf<String>()
        
        val tagRegex1 = "\"name\":\"([^\"]+)\",\"slug\":\"[^\"]+\",\"translatedName\":null".toRegex()
        tagRegex1.findAll(jsonString).forEach { match ->
            val tag = match.groupValues[1]
            if (tag.isNotEmpty() && !tags.contains(tag)) {
                tags.add(tag)
            }
        }
        
        if (tags.isEmpty()) {
            val tagRegex2 = "\"topicTags\":\\[([^\\]]+)".toRegex()
            val tagContent = tagRegex2.find(jsonString)?.groupValues?.get(1) ?: ""
            val namePattern = "\"name\":\"([^\"]+)\"".toRegex()
            namePattern.findAll(tagContent).forEach { match ->
                val tag = match.groupValues[1]
                if (!tags.contains(tag)) {
                    tags.add(tag)
                }
            }
        }
        
        return tags
    }

    private fun extractHints(jsonString: String): List<String> {
        val hints = mutableListOf<String>()
        val hintsRegex = "\"hints\":\\[([^\\]]+)".toRegex()
        val hintsContent = hintsRegex.find(jsonString)?.groupValues?.get(1) ?: ""
        
        val hintPattern = "\"([^\"]+)\"".toRegex()
        hintPattern.findAll(hintsContent).forEach { match ->
            val hint = match.groupValues[1]
            if (hint.isNotEmpty()) {
                hints.add(hint)
            }
        }
        
        return hints
    }
}
