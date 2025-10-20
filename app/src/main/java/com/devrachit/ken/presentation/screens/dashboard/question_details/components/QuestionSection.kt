package com.devrachit.ken.presentation.screens.dashboard.question_details.components

data class QuestionSection(val type: SectionType, val content: String)

enum class SectionType {
    DESCRIPTION, EXAMPLE, CONSTRAINTS, FOLLOW_UP, IMAGE
}

fun parseSections(description: String): List<QuestionSection> {
    val sections = mutableListOf<QuestionSection>()
    
    // Extract image URLs - supports multiple formats:
    // 1. [https://url.jpg] - LeetCode format
    // 2. [text](https://url.jpg) - Markdown format
    // 3. https://url.jpg - Direct URL
    val imageRegex = """\[?(https?://[^\s\]]+\.(?:jpg|jpeg|png|gif|svg|webp)[^\s\]]*)\]?""".toRegex(RegexOption.IGNORE_CASE)
    val imageUrls = mutableListOf<String>()
    
    try {
        imageRegex.findAll(description).forEach { match ->
            val url = match.groupValues[1]
            if (url.isNotEmpty() && !imageUrls.contains(url)) {
                imageUrls.add(url)
            }
        }
    } catch (e: Exception) {
        // If regex fails on malformed content, continue without images
        e.printStackTrace()
    }
    
    // Remove image URLs and their brackets from description for cleaner text parsing
    var cleanedDescription = description
    imageUrls.forEach { url ->
        cleanedDescription = cleanedDescription
            .replace("[$url]", "")
            .replace(url, "")
            .replace("[]", "")
            .replace("()", "")
    }
    
    // Split by common patterns
    val parts = cleanedDescription.split(Regex("(?=Example \\d+:|Constraints:|Follow up:)"))
    
    var descriptionAdded = false
    
    parts.forEachIndexed { index, part ->
        val trimmed = part.trim()
        when {
            trimmed.startsWith("Example", ignoreCase = true) -> {
                // Add images before the first example if available and description was added
                if (imageUrls.isNotEmpty() && descriptionAdded && sections.none { it.type == SectionType.IMAGE }) {
                    imageUrls.forEach { url ->
                        sections.add(QuestionSection(SectionType.IMAGE, url))
                    }
                }
                sections.add(QuestionSection(SectionType.EXAMPLE, trimmed))
            }
            trimmed.startsWith("Constraints:", ignoreCase = true) -> {
                val content = trimmed.removePrefix("Constraints:").trim()
                sections.add(QuestionSection(SectionType.CONSTRAINTS, content))
            }
            trimmed.startsWith("Follow up:", ignoreCase = true) -> {
                val content = trimmed.removePrefix("Follow up:").trim()
                sections.add(QuestionSection(SectionType.FOLLOW_UP, content))
            }
            trimmed.isNotEmpty() -> {
                sections.add(QuestionSection(SectionType.DESCRIPTION, trimmed))
                descriptionAdded = true
                
                // Add images right after description if this is the first description section
                if (index == 0 && imageUrls.isNotEmpty()) {
                    imageUrls.forEach { url ->
                        sections.add(QuestionSection(SectionType.IMAGE, url))
                    }
                }
            }
        }
    }
    
    return sections
}
