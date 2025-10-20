package com.devrachit.ken.presentation.screens.dashboard.question_details.components

data class QuestionSection(val type: SectionType, val content: String)

enum class SectionType {
    DESCRIPTION, EXAMPLE, CONSTRAINTS, FOLLOW_UP
}

fun parseSections(description: String): List<QuestionSection> {
    val sections = mutableListOf<QuestionSection>()
    
    // Split by common patterns
    val parts = description.split(Regex("(?=Example \\d+:|Constraints:|Follow up:)"))
    
    parts.forEach { part ->
        val trimmed = part.trim()
        when {
            trimmed.startsWith("Example", ignoreCase = true) -> {
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
            }
        }
    }
    
    return sections
}
