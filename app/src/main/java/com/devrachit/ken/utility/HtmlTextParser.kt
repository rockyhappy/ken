package com.devrachit.ken.utility

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

object HtmlTextParser {
    
    fun parseHtmlToAnnotatedString(
        html: String,
        defaultColor: Color = Color.White,
        codeBackgroundColor: Color = Color(0xFF2D2D2D)
    ): AnnotatedString {
        return buildAnnotatedString {
            parseNode(html, defaultColor, codeBackgroundColor)
        }
    }
    
    private fun AnnotatedString.Builder.parseNode(
        text: String,
        defaultColor: Color,
        codeBackgroundColor: Color
    ) {
        var currentIndex = 0
        val tagRegex = "<(/?)([a-zA-Z]+)>".toRegex()
        val matches = tagRegex.findAll(text).toList()
        
        if (matches.isEmpty()) {
            // No HTML tags, just append the text
            append(text)
            return
        }
        
        val tagStack = mutableListOf<Pair<String, Int>>() // Tag name and start index
        
        matches.forEachIndexed { index, match ->
            val beforeTag = text.substring(currentIndex, match.range.first)
            if (beforeTag.isNotEmpty()) {
                append(beforeTag)
            }
            
            val isClosing = match.groupValues[1] == "/"
            val tagName = match.groupValues[2].lowercase()
            
            if (!isClosing) {
                // Opening tag
                tagStack.add(tagName to length)
            } else {
                // Closing tag - find matching opening tag
                val openingTagIndex = tagStack.indexOfLast { it.first == tagName }
                if (openingTagIndex != -1) {
                    val (tag, startIndex) = tagStack.removeAt(openingTagIndex)
                    val endIndex = length
                    
                    // Apply style based on tag
                    when (tag) {
                        "b", "strong" -> {
                            addStyle(
                                SpanStyle(fontWeight = FontWeight.Bold),
                                startIndex,
                                endIndex
                            )
                        }
                        "i", "em" -> {
                            addStyle(
                                SpanStyle(fontStyle = FontStyle.Italic),
                                startIndex,
                                endIndex
                            )
                        }
                        "code", "c" -> {
                            addStyle(
                                SpanStyle(
                                    fontFamily = FontFamily.Monospace,
                                    background = codeBackgroundColor,
                                    color = Color(0xFFE06C75)
                                ),
                                startIndex,
                                endIndex
                            )
                        }
                        "u" -> {
                            addStyle(
                                SpanStyle(
                                    textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                                ),
                                startIndex,
                                endIndex
                            )
                        }
                    }
                }
            }
            
            currentIndex = match.range.last + 1
        }
        
        // Append remaining text after last tag
        if (currentIndex < text.length) {
            append(text.substring(currentIndex))
        }
    }
    
    // Simple version for basic parsing
    fun parseSimpleHtml(html: String): AnnotatedString {
        return buildAnnotatedString {
            var remainingText = html
            var iterations = 0
            val maxIterations = 10000 // Safety limit to prevent infinite loops
            
            while (remainingText.isNotEmpty()) {
                // Safety check to prevent infinite loops
                if (iterations++ > maxIterations) {
                    append("\n[Content truncated - too complex to parse]")
                    break
                }
                
                when {
                    remainingText.startsWith("<b>") || remainingText.startsWith("<strong>") -> {
                        val tag = if (remainingText.startsWith("<b>")) "<b>" else "<strong>"
                        val closeTag = if (tag == "<b>") "</b>" else "</strong>"
                        remainingText = remainingText.removePrefix(tag)
                        
                        val endIndex = remainingText.indexOf(closeTag)
                        if (endIndex != -1) {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(remainingText.substring(0, endIndex))
                            }
                            remainingText = remainingText.substring(endIndex + closeTag.length)
                        } else {
                            append(remainingText)
                            remainingText = ""
                        }
                    }
                    
                    remainingText.startsWith("<i>") || remainingText.startsWith("<em>") -> {
                        val tag = if (remainingText.startsWith("<i>")) "<i>" else "<em>"
                        val closeTag = if (tag == "<i>") "</i>" else "</em>"
                        remainingText = remainingText.removePrefix(tag)
                        
                        val endIndex = remainingText.indexOf(closeTag)
                        if (endIndex != -1) {
                            withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                                append(remainingText.substring(0, endIndex))
                            }
                            remainingText = remainingText.substring(endIndex + closeTag.length)
                        } else {
                            append(remainingText)
                            remainingText = ""
                        }
                    }
                    
                    remainingText.startsWith("<code>") || remainingText.startsWith("<c>") -> {
                        val tag = if (remainingText.startsWith("<code>")) "<code>" else "<c>"
                        val closeTag = if (tag == "<code>") "</code>" else "</c>"
                        remainingText = remainingText.removePrefix(tag)
                        
                        val endIndex = remainingText.indexOf(closeTag)
                        if (endIndex != -1) {
                            withStyle(
                                SpanStyle(
                                    fontFamily = FontFamily.Monospace,
                                    background = Color(0xFF2D2D2D),
                                    color = Color(0xFFE06C75)
                                )
                            ) {
                                append(remainingText.substring(0, endIndex))
                            }
                            remainingText = remainingText.substring(endIndex + closeTag.length)
                        } else {
                            append(remainingText)
                            remainingText = ""
                        }
                    }
                    
                    remainingText.startsWith("<u>") -> {
                        remainingText = remainingText.removePrefix("<u>")
                        val endIndex = remainingText.indexOf("</u>")
                        if (endIndex != -1) {
                            withStyle(
                                SpanStyle(
                                    textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                                )
                            ) {
                                append(remainingText.substring(0, endIndex))
                            }
                            remainingText = remainingText.substring(endIndex + 4)
                        } else {
                            append(remainingText)
                            remainingText = ""
                        }
                    }
                    
                    else -> {
                        // Find next tag or append remaining
                        val nextTag = Regex("<[^>]+>").find(remainingText)
                        if (nextTag != null) {
                            append(remainingText.substring(0, nextTag.range.first))
                            remainingText = remainingText.substring(nextTag.range.first)
                        } else {
                            append(remainingText)
                            remainingText = ""
                        }
                    }
                }
            }
        }
    }
}
