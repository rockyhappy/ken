package com.devrachit.ken.presentation.screens.dashboard.compare.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserCalendar
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp
import kotlin.math.max

data class UserStreakData(
    val username: String,
    val displayName: String,
    val streak: Int,
    val activeDays: Int,
    val activeYears: List<Int>
)

@Composable
fun StreakActivityGraphs(
    modifier: Modifier = Modifier,
    userCalendarData: Map<String, UserCalendar>,
    userDetails: Map<String, LeetCodeUserInfo>
) {
    val streakData = userCalendarData.mapNotNull { (username, calendar) ->
        val userInfo = userDetails[username]
        val displayName = userInfo?.profile?.realName?.takeIf { it.isNotBlank() } 
            ?: username.takeIf { it.isNotBlank() } 
            ?: "Unknown"
        
        UserStreakData(
            username = username,
            displayName = displayName,
            streak = calendar.streak,
            activeDays = calendar.totalActiveDays,
            activeYears = calendar.activeYears
        )
    }.sortedByDescending { it.streak } // Sort by streak descending

    if (streakData.isNotEmpty()) {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.sdp)
        ) {
            // Streak Comparison
            StreakComparisonGraph(
                title = "Current Streak Comparison",
                userData = streakData,
                getValue = { it.streak },
                unit = "days",
                color = colorResource(R.color.easy_filled_blue)
            )

            // Active Days Comparison
            StreakComparisonGraph(
                title = "Total Active Days Comparison",
                userData = streakData.sortedByDescending { it.activeDays },
                getValue = { it.activeDays },
                unit = "days",
                color = colorResource(R.color.medium_filled_yellow)
            )

            // Active Years Comparison
            ActiveYearsGraph(
                title = "Active Years Comparison",
                userData = streakData.sortedByDescending { it.activeYears.size }
            )
        }
    }
}

@Composable
private fun StreakComparisonGraph(
    title: String,
    userData: List<UserStreakData>,
    getValue: (UserStreakData) -> Int,
    unit: String,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.bg_neutral)
        ),
        border = BorderStroke(
            width = 2.sdp,
            color = colorResource(R.color.card_elevated)
        ),
        shape = RoundedCornerShape(36.sdp),
    ) {
        Column(
            modifier = Modifier.padding(16.sdp)
        ) {
            Text(
                text = title,
                style = TextStyleInter16Lh24Fw700(),
                color = colorResource(R.color.white),
                modifier = Modifier.padding(bottom = 16.sdp)
            )

            if (userData.isNotEmpty()) {
                val maxValue = userData.maxOfOrNull { getValue(it) } ?: 1
                val topUsers = userData.take(10) // Show top 10 users

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.sdp),
                    contentPadding = PaddingValues(horizontal = 4.sdp)
                ) {
                    items(topUsers) { user ->
                        UserStreakBar(
                            username = user.displayName,
                            value = getValue(user),
                            maxValue = maxValue,
                            color = color,
                            unit = unit,
                            isTopPerformer = user == topUsers.first()
                        )
                    }
                }

                // Show winner
                val winner = topUsers.first()
                Text(
                    text = "🏆 ${winner.displayName} leads with ${getValue(winner)} $unit",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = color
                    ),
                    modifier = Modifier.padding(top = 12.sdp)
                )
            } else {
                Text(
                    text = "No data available",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                )
            }
        }
    }
}

@Composable
private fun UserStreakBar(
    username: String,
    value: Int,
    maxValue: Int,
    color: Color,
    unit: String,
    isTopPerformer: Boolean = false
) {
    val fillPercentage = if (maxValue > 0) value.toFloat() / maxValue else 0f
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.sdp)
    ) {
        // Username with crown for top performer
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isTopPerformer) {
                Text(
                    text = "👑",
                    style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                    modifier = Modifier.padding(end = 2.sdp)
                )
            }
            Text(
                text = if (username.length > 8) username.take(6) + ".." else username,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 10.sp,
                    fontWeight = if (isTopPerformer) FontWeight.Bold else FontWeight.Normal,
                    color = if (isTopPerformer) color else Color.White
                ),
                maxLines = 1,
                modifier = Modifier.padding(bottom = 4.sdp)
            )
        }

        // Bar
        Box(
            modifier = Modifier
                .width(60.sdp)
                .height(120.sdp)
                .background(
                    color.copy(alpha = 0.2f),
                    RoundedCornerShape(6.sdp)
                )
                .border(
                    width = if (isTopPerformer) 2.sdp else 1.sdp,
                    color = if (isTopPerformer) color else color.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(6.sdp)
                )
        ) {
            // Filled portion
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fillPercentage)
                    .background(
                        if (isTopPerformer) color else color.copy(alpha = 0.8f),
                        RoundedCornerShape(6.sdp)
                    )
                    .align(Alignment.BottomCenter)
            )

            // Value text
            Text(
                text = value.toString(),
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Unit
        Text(
            text = unit,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 9.sp,
                color = color
            ),
            modifier = Modifier.padding(top = 4.sdp)
        )
    }
}

@Composable
private fun ActiveYearsGraph(
    title: String,
    userData: List<UserStreakData>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.bg_neutral)
        ),
        border = BorderStroke(
            width = 2.sdp,
            color = colorResource(R.color.card_elevated)
        ),
        shape = RoundedCornerShape(36.sdp)
    ) {
        Column(
            modifier = Modifier.padding(16.sdp)
        ) {
            Text(
                text = title,
                style = TextStyleInter16Lh24Fw700(),
                color = colorResource(R.color.white),
                modifier = Modifier.padding(bottom = 16.sdp)
            )

            if (userData.isNotEmpty()) {
                val topUsers = userData.take(6) // Show top 6 users for better spacing

                // Get the overall year range across all users
                val allYears = userData.flatMap { it.activeYears }
                val minYear = allYears.minOrNull() ?: 2020
                val maxYear = allYears.maxOrNull() ?: 2024
                val yearRange = (minYear..maxYear).toList()

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.sdp)
                ) {
                    // Year labels header
                    YearRangeHeader(yearRange = yearRange)

                    // User timelines
                    topUsers.forEach { user ->
                        UserTimelineRow(
                            user = user,
                            yearRange = yearRange,
                            isTopPerformer = user == topUsers.first()
                        )
                    }
                }

                // Winner announcement
                val winner = topUsers.first()
                Text(
                    text = "🏆 ${winner.displayName} leads with ${winner.activeYears.size} active years",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = colorResource(R.color.hard_filled_red)
                    ),
                    modifier = Modifier.padding(top = 12.sdp)
                )
            }
        }
    }
}

@Composable
private fun YearRangeHeader(yearRange: List<Int>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 100.sdp, end = 16.sdp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        yearRange.forEach { year ->
            Text(
                text = year.toString().takeLast(2), // Show last 2 digits (e.g., "24" for 2024)
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 9.sp,
                    color = Color.White.copy(alpha = 0.6f)
                ),
                modifier = Modifier.width(16.sdp)
            )
        }
    }
}

@Composable
private fun UserTimelineRow(
    user: UserStreakData,
    yearRange: List<Int>,
    isTopPerformer: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User info section
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(90.sdp)
        ) {
            if (isTopPerformer) {
                Text(
                    text = "👑",
                    style = androidx.compose.ui.text.TextStyle(fontSize = 12.sp),
                    modifier = Modifier.padding(end = 4.sdp)
                )
            }

            Column {
                Text(
                    text = if (user.displayName.length > 10) user.displayName.take(8) + ".." else user.displayName,
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 12.sp,
                        fontWeight = if (isTopPerformer) FontWeight.Bold else FontWeight.Normal,
                        color = if (isTopPerformer) colorResource(R.color.hard_filled_red) else Color.White
                    ),
                    maxLines = 1
                )
                Text(
                    text = "${user.activeYears.size} years",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 10.sp,
                        color = colorResource(R.color.hard_filled_red).copy(alpha = 0.8f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.width(10.sdp))

        // Timeline section
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            yearRange.forEach { year ->
                Box(
                    modifier = Modifier
                        .size(16.sdp)
                        .background(
                            color = if (user.activeYears.contains(year)) {
                                if (isTopPerformer) colorResource(R.color.hard_filled_red)
                                else colorResource(R.color.hard_filled_red).copy(alpha = 0.7f)
                            } else {
                                Color.White.copy(alpha = 0.1f)
                            },
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                        .border(
                            width = if (user.activeYears.contains(year)) 2.sdp else 1.sdp,
                            color = if (user.activeYears.contains(year)) {
                                if (isTopPerformer) colorResource(R.color.hard_filled_red)
                                else colorResource(R.color.hard_filled_red).copy(alpha = 0.8f)
                            } else {
                                Color.White.copy(alpha = 0.2f)
                            },
                            shape = androidx.compose.foundation.shape.CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (user.activeYears.contains(year)) {
                        Box(
                            modifier = Modifier
                                .size(6.sdp)
                                .background(
                                    Color.White,
                                    androidx.compose.foundation.shape.CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}
