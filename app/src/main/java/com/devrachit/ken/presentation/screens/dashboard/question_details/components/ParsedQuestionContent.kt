package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun ParsedQuestionContent(
    description: String,
    hints: List<String>
) {
    // Split description into sections
    val sections = parseSections(description)
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.card_elevated).copy(alpha = 0.2f))
            .padding(20.sdp)
    ) {
        sections.forEach { section ->
            when (section.type) {
                SectionType.DESCRIPTION -> {
                    Text(
                        text = section.content,
                        color = colorResource(R.color.white).copy(alpha = 0.9f),
                        style = TextStyleInter14Lh20Fw400(),
                        modifier = Modifier.padding(bottom = 16.sdp)
                    )
                }
                SectionType.EXAMPLE -> {
                    ExampleSection(content = section.content)
                }
                SectionType.CONSTRAINTS -> {
                    ConstraintsSection(content = section.content)
                }
                SectionType.FOLLOW_UP -> {
                    FollowUpSection(content = section.content)
                }
                SectionType.IMAGE -> {
                    QuestionImageSection(imageUrl = section.content)
                }
            }
        }
        
        // Display hints at the end if available
        if (hints.isNotEmpty()) {
            HintsSection(hints = hints)
        }
    }
}
