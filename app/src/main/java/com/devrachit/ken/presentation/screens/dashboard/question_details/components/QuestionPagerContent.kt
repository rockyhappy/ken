package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.QuestionDetails
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.utility.HtmlTextParser
import com.devrachit.ken.utility.composeUtility.sdp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestionPagerContent(
    questionDetails: QuestionDetails
) {
    val sections = parseSections(questionDetails.description)
    val tabs = mutableListOf("Description")
    
    val hasExamples = sections.any { it.type == SectionType.EXAMPLE }
    val hasConstraints = sections.any { it.type == SectionType.CONSTRAINTS }
    val hasFollowUp = sections.any { it.type == SectionType.FOLLOW_UP }
    val hasHints = questionDetails.hints.isNotEmpty()
    
    if (hasExamples) tabs.add("Examples")
    if (hasConstraints) tabs.add("Constraints")
    if (hasFollowUp) tabs.add("Follow Up")
    if (hasHints) tabs.add("Hints")
    
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.sdp, vertical = 10.sdp)
            .clip(RoundedCornerShape(36.sdp))
            .border(
                border = BorderStroke(
                    width = 2.sdp,
                    color = colorResource(R.color.card_elevated)
                ),
                shape = RoundedCornerShape(36.sdp)
            )
    ) {
        // Scrollable Tabs with proper rounded top corners
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = colorResource(R.color.card_elevated).copy(alpha = 0.3f),
            contentColor = colorResource(R.color.white),
            divider = {},
            edgePadding = 20.sdp,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 34.sdp, topEnd = 34.sdp))
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            style = TextStyleInter14Lh20Fw400(),
                            color = if (pagerState.currentPage == index) 
                                colorResource(R.color.white) 
                            else 
                                colorResource(R.color.white).copy(alpha = 0.6f)
                        )
                    }
                )
            }
        }
        
        // Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.sdp, max = 600.sdp)
        ) { page ->
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.sdp)
            ) {
                when (tabs[page]) {
                    "Description" -> {
                        val descriptionSections = sections.filter { 
                            it.type == SectionType.DESCRIPTION || it.type == SectionType.IMAGE 
                        }
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
                    }
                    "Examples" -> {
                        val exampleSections = sections.filter { it.type == SectionType.EXAMPLE }
                        exampleSections.forEach { section ->
                            ExampleSection(content = section.content)
                        }
                    }
                    "Constraints" -> {
                        val constraintsSections = sections.filter { it.type == SectionType.CONSTRAINTS }
                        constraintsSections.forEach { section ->
                            ConstraintsSection(content = section.content)
                        }
                    }
                    "Follow Up" -> {
                        val followUpSections = sections.filter { it.type == SectionType.FOLLOW_UP }
                        followUpSections.forEach { section ->
                            FollowUpSection(content = section.content)
                        }
                    }
                    "Hints" -> {
                        HintsSection(hints = questionDetails.hints)
                    }
                }
            }
        }
    }
}
