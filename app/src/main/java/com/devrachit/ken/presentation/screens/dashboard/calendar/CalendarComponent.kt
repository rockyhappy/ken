package com.devrachit.ken.presentation.screens.dashboard.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle as JavaTextStyle
import java.util.Locale

@Composable
fun CalendarComponent(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = hiltViewModel(),
    onQuestionClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedChallenge by remember { mutableStateOf<com.devrachit.ken.domain.models.DailyChallenge?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.sdp))
            .background(colorResource(R.color.card_elevated))
            .border(
                width = 1.sdp,
                color = colorResource(R.color.blue_normal_500).copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.sdp)
            )
            .padding(16.sdp)
    ) {
        // Calendar Header with Month/Year and Navigation
        CalendarHeader(
            displayedMonth = uiState.displayedMonth,
            onPreviousMonth = { viewModel.navigateToPreviousMonth() },
            onNextMonth = { viewModel.navigateToNextMonth() }
        )

        Spacer(modifier = Modifier.height(16.sdp))

        // Day of week headers
        DayOfWeekHeader()

        Spacer(modifier = Modifier.height(8.sdp))

        // Calendar Grid
        CalendarGrid(
            displayedMonth = uiState.displayedMonth,
            currentDate = uiState.currentDate,
            dailyChallenges = uiState.dailyChallenges,
            onDateClick = { challenge ->
                selectedChallenge = challenge
            }
        )
    }

    // Show bottom sheet when a challenge is selected
    selectedChallenge?.let { challenge ->
        QuestionBottomSheet(
            challenge = challenge,
            onDismiss = { selectedChallenge = null },
            onOpenQuestion = { slug ->
                selectedChallenge = null
                onQuestionClick(slug)
            }
        )
    }
}

@Composable
private fun CalendarHeader(
    displayedMonth: java.time.YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPreviousMonth,
            modifier = Modifier.size(32.sdp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Previous Month",
                tint = colorResource(R.color.blue_normal_500),
                modifier = Modifier.size(24.sdp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = displayedMonth.month.getDisplayName(JavaTextStyle.FULL, Locale.getDefault()),
                color = colorResource(R.color.white),
                style = TextStyleInter16Lh24Fw700(),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = displayedMonth.year.toString(),
                color = colorResource(R.color.white).copy(alpha = 0.7f),
                style = TextStyleInter12Lh16Fw400()
            )
        }

        IconButton(
            onClick = onNextMonth,
            modifier = Modifier.size(32.sdp)
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Next Month",
                tint = colorResource(R.color.blue_normal_500),
                modifier = Modifier.size(24.sdp)
            )
        }
    }
}

@Composable
private fun DayOfWeekHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val daysOfWeek = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
        daysOfWeek.forEach { day ->
            Text(
                text = day,
                color = colorResource(R.color.white).copy(alpha = 0.5f),
                style = TextStyleInter12Lh16Fw400(),
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    displayedMonth: java.time.YearMonth,
    currentDate: LocalDate,
    dailyChallenges: Map<LocalDate, com.devrachit.ken.domain.models.DailyChallenge>,
    onDateClick: (com.devrachit.ken.domain.models.DailyChallenge) -> Unit
) {
    val firstDayOfMonth = displayedMonth.atDay(1)
    val lastDayOfMonth = displayedMonth.atEndOfMonth()
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Sunday = 0

    // Calculate total cells needed
    val daysInMonth = displayedMonth.lengthOfMonth()
    val totalCells = firstDayOfWeek + daysInMonth
    val rows = (totalCells + 6) / 7 // Ceiling division

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        repeat(rows) { weekIndex ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(7) { dayIndex ->
                    val cellIndex = weekIndex * 7 + dayIndex
                    val dayOfMonth = cellIndex - firstDayOfWeek + 1

                    if (dayOfMonth in 1..daysInMonth) {
                        val date = displayedMonth.atDay(dayOfMonth)
                        val challenge = dailyChallenges[date]
                        val isToday = date == currentDate

                        CalendarDayCell(
                            day = dayOfMonth,
                            isToday = isToday,
                            hasQuestion = challenge != null,
                            onClick = {
                                challenge?.let { onDateClick(it) }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        // Empty cell for days not in this month
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    day: Int,
    isToday: Boolean,
    hasQuestion: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                when {
                    isToday -> colorResource(R.color.blue_normal_500).copy(alpha = 0.2f)
                    hasQuestion -> colorResource(R.color.green_normal_500).copy(alpha = 0.15f)
                    else -> colorResource(R.color.bg_neutral).copy(alpha = 0.3f)
                }
            )
            .border(
                width = if (isToday) 2.dp else 0.dp,
                color = if (isToday) colorResource(R.color.blue_normal_500) else colorResource(R.color.bg_neutral),
                shape = CircleShape
            )
            .clickable(enabled = hasQuestion || isToday) { onClick() }
            .padding(4.sdp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.toString(),
                color = when {
                    isToday -> colorResource(R.color.blue_normal_500)
                    hasQuestion -> colorResource(R.color.green_normal_500)
                    else -> colorResource(R.color.white).copy(alpha = 0.7f)
                },
                style = TextStyleInter14Lh20Fw400(),
                fontWeight = if (isToday || hasQuestion) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            
            if (hasQuestion) {
                Box(
                    modifier = Modifier
                        .size(4.sdp)
                        .clip(CircleShape)
                        .background(colorResource(R.color.green_normal_500))
                )
            }
        }
    }
}
