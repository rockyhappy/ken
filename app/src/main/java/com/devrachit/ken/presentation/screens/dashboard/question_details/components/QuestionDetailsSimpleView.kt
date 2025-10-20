package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.QuestionDetails
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.utility.HtmlTextParser
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionDetailsSimpleView(
    questionDetails: QuestionDetails,
    questionSlug: String,
    onBackClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bg_neutral))
    ) {
        // Header with back button
        QuestionDetailsHeader(
            questionTitle = questionDetails.title,
            questionSlug = questionSlug,
            onBackClick = onBackClick
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.sdp, vertical = 16.sdp)
        ) {
            // Title with difficulty badge
            QuestionTitle(
                title = questionDetails.title,
                difficulty = questionDetails.difficulty
            )
            
            Spacer(modifier = Modifier.height(16.sdp))
            
            // Likes, Dislikes and Rating
            QuestionEngagementStats(questionDetails = questionDetails)
            
            Spacer(modifier = Modifier.height(20.sdp))
            
            // Description Section
            val sections = parseSections(questionDetails.description)
            val descriptionSections = sections.filter { 
                it.type == SectionType.DESCRIPTION || it.type == SectionType.IMAGE 
            }
            
            SectionHeader(title = "Description")
            Spacer(modifier = Modifier.height(8.sdp))
            
            descriptionSections.forEach { section ->
                when (section.type) {
                    SectionType.DESCRIPTION -> {
                        Text(
                            text = HtmlTextParser.parseSimpleHtml(section.content),
                            color = colorResource(R.color.white).copy(alpha = 0.9f),
                            style = TextStyleInter14Lh20Fw400(),
                            modifier = Modifier.padding(bottom = 16.sdp)
                        )
                    }
                    SectionType.IMAGE -> {
                        QuestionImageSection(imageUrl = section.content)
                    }
                    else -> {}
                }
            }
            
            Spacer(modifier = Modifier.height(12.sdp))
            
            // Examples Section
            val exampleSections = sections.filter { it.type == SectionType.EXAMPLE }
            if (exampleSections.isNotEmpty()) {
                SectionHeader(title = "Examples")
                Spacer(modifier = Modifier.height(8.sdp))
                exampleSections.forEach { section ->
                    ExampleSection(content = section.content)
                }
                Spacer(modifier = Modifier.height(12.sdp))
            }
            
            // Constraints Section
            val constraintsSections = sections.filter { it.type == SectionType.CONSTRAINTS }
            if (constraintsSections.isNotEmpty()) {
                SectionHeader(title = "Constraints")
                Spacer(modifier = Modifier.height(8.sdp))
                constraintsSections.forEach { section ->
                    ConstraintsSection(content = section.content)
                }
                Spacer(modifier = Modifier.height(12.sdp))
            }
            
            // Follow Up Section
            val followUpSections = sections.filter { it.type == SectionType.FOLLOW_UP }
            if (followUpSections.isNotEmpty()) {
                SectionHeader(title = "Follow Up")
                Spacer(modifier = Modifier.height(8.sdp))
                followUpSections.forEach { section ->
                    FollowUpSection(content = section.content)
                }
                Spacer(modifier = Modifier.height(12.sdp))
            }
            
            // Collapsible Hints Section
            if (questionDetails.hints.isNotEmpty()) {
                CollapsibleHintsSection(hints = questionDetails.hints)
                Spacer(modifier = Modifier.height(20.sdp))
            }
            
            // Acceptance Rate and Stats
            QuestionStatsSection(questionDetails = questionDetails)
            
            Spacer(modifier = Modifier.height(16.sdp))
            
            // Topics Section
            QuestionTagsSection(questionDetails = questionDetails)
            
            Spacer(modifier = Modifier.height(100.sdp))
        }
    }
}

@Composable
private fun QuestionTitle(
    title: String,
    difficulty: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = colorResource(R.color.white),
            style = TextStyleInter16Lh24Fw700(),
            modifier = Modifier.weight(1f)
        )
        
        Spacer(modifier = Modifier.width(12.sdp))
        
        // Difficulty Badge
        val difficultyColor = when (difficulty) {
            "Easy" -> colorResource(R.color.green_normal_500)
            "Medium" -> colorResource(R.color.yellow_normal_500)
            "Hard" -> colorResource(R.color.red_normal_500)
            else -> colorResource(R.color.white)
        }
        
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.sdp))
                .background(difficultyColor.copy(alpha = 0.2f))
                .padding(horizontal = 12.sdp, vertical = 6.sdp)
        ) {
            Text(
                text = difficulty,
                color = difficultyColor,
                style = TextStyleInter12Lh16Fw400(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun QuestionEngagementStats(questionDetails: QuestionDetails) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.sdp))
            .background(colorResource(R.color.card_elevated).copy(alpha = 0.3f))
            .padding(16.sdp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Likes
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "👍",
                style = TextStyleInter16Lh24Fw700()
            )
            Text(
                text = questionDetails.likes.toString(),
                color = colorResource(R.color.green_normal_500),
                style = TextStyleInter14Lh20Fw400(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.sdp)
            )
            Text(
                text = "Likes",
                color = colorResource(R.color.white).copy(alpha = 0.6f),
                style = TextStyleInter12Lh16Fw400(),
                modifier = Modifier.padding(top = 2.sdp)
            )
        }
        
        // Dislikes
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "👎",
                style = TextStyleInter16Lh24Fw700()
            )
            Text(
                text = questionDetails.dislikes.toString(),
                color = colorResource(R.color.red_normal_500),
                style = TextStyleInter14Lh20Fw400(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.sdp)
            )
            Text(
                text = "Dislikes",
                color = colorResource(R.color.white).copy(alpha = 0.6f),
                style = TextStyleInter12Lh16Fw400(),
                modifier = Modifier.padding(top = 2.sdp)
            )
        }
        
        // Rating Ratio
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "⭐",
                style = TextStyleInter16Lh24Fw700()
            )
            val total = questionDetails.likes + questionDetails.dislikes
            val rating = if (total > 0) {
                String.format("%.1f", (questionDetails.likes.toFloat() / total) * 5)
            } else "N/A"
            Text(
                text = rating,
                color = colorResource(R.color.yellow_normal_500),
                style = TextStyleInter14Lh20Fw400(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.sdp)
            )
            Text(
                text = "Rating",
                color = colorResource(R.color.white).copy(alpha = 0.6f),
                style = TextStyleInter12Lh16Fw400(),
                modifier = Modifier.padding(top = 2.sdp)
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        color = colorResource(R.color.blue_normal_500),
        style = TextStyleInter14Lh20Fw400(),
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun CollapsibleHintsSection(hints: List<String>) {
    var isExpanded by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.sdp))
            .border(
                border = BorderStroke(
                    width = 1.sdp,
                    color = colorResource(R.color.green_normal_500).copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(16.sdp)
            )
            .background(colorResource(R.color.card_elevated).copy(alpha = 0.2f))
    ) {
        // Header - always visible
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(16.sdp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "💡 ",
                    style = TextStyleInter16Lh24Fw700()
                )
                Text(
                    text = "Hints (${hints.size})",
                    color = colorResource(R.color.green_normal_500),
                    style = TextStyleInter14Lh20Fw400(),
                    fontWeight = FontWeight.Bold
                )
            }
            
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp 
                              else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = colorResource(R.color.green_normal_500)
            )
        }
        
        // Expandable content
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 16.sdp, 
                    end = 16.sdp, 
                    bottom = 16.sdp
                )
            ) {
                hints.forEachIndexed { index, hint ->
                    if (index > 0) {
                        Spacer(modifier = Modifier.height(12.sdp))
                    }
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.sdp))
                            .background(colorResource(R.color.bg_neutral).copy(alpha = 0.5f))
                            .padding(12.sdp)
                    ) {
                        Text(
                            text = "Hint ${index + 1}",
                            color = colorResource(R.color.green_normal_500),
                            style = TextStyleInter12Lh16Fw400(),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 6.sdp)
                        )
                        Text(
                            text = HtmlTextParser.parseSimpleHtml(hint),
                            color = colorResource(R.color.white).copy(alpha = 0.88f),
                            style = TextStyleInter12Lh16Fw400()
                        )
                    }
                }
            }
        }
    }
}
