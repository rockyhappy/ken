package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun ConstraintsSection(content: String) {
    val yellowColor = colorResource(R.color.yellow_normal_500)
    val whiteColor = colorResource(R.color.white)
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.sdp)
            .clip(RoundedCornerShape(16.sdp))
            .border(
                border = BorderStroke(
                    width = 1.sdp,
                    color = yellowColor.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(16.sdp)
            )
            .padding(12.sdp)
    ) {
        // Constraints title
        Text(
            text = "Constraints",
            color = yellowColor,
            style = TextStyleInter12Lh16Fw400(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.sdp)
        )
        
        // Constraints content with enhanced styling
        val lines = content.split("\n")
        lines.forEach { line ->
            val trimmedLine = line.trim().removePrefix("*").trim()
            if (trimmedLine.isNotEmpty()) {
                Row(modifier = Modifier.padding(vertical = 3.sdp)) {
                    Text(
                        text = "• ",
                        color = yellowColor,
                        style = TextStyleInter12Lh16Fw400(),
                        fontWeight = FontWeight.Bold
                    )
                    
                    // Highlight numbers and operators in constraints
                    val annotatedText = buildAnnotatedString {
                        var currentIndex = 0
                        val numberPattern = "\\d+|<=|>=|<|>|=".toRegex()
                        
                        numberPattern.findAll(trimmedLine).forEach { matchResult ->
                            // Add text before match
                            if (matchResult.range.first > currentIndex) {
                                append(trimmedLine.substring(currentIndex, matchResult.range.first))
                            }
                            
                            // Add matched number/operator with styling
                            withStyle(
                                SpanStyle(
                                    color = yellowColor,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            ) {
                                append(matchResult.value)
                            }
                            
                            currentIndex = matchResult.range.last + 1
                        }
                        
                        // Add remaining text
                        if (currentIndex < trimmedLine.length) {
                            append(trimmedLine.substring(currentIndex))
                        }
                    }
                    
                    Text(
                        text = annotatedText,
                        color = whiteColor.copy(alpha = 0.85f),
                        style = TextStyleInter12Lh16Fw400()
                    )
                }
            }
        }
    }
}
