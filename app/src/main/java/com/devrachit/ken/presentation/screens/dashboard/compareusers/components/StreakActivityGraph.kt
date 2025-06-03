package com.devrachit.ken.presentation.screens.dashboard.compareusers.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.UserCalendar
import com.devrachit.ken.utility.composeUtility.sdp
import kotlin.math.max

@Composable
fun StreakActivityGraph(
    modifier: Modifier = Modifier,
    user1Name: String,
    user1Calendar: UserCalendar?,
    user2Name: String,
    user2Calendar: UserCalendar?,
    color1: Color = colorResource(R.color.easy_filled_blue),
    color2: Color = colorResource(R.color.medium_filled_yellow)
) {
//    Card(
//        modifier = modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(
//            containerColor = colorResource(R.color.bg_neutral)
//        ),
//        border = CardDefaults.outlinedCardBorder(),
//        shape = RoundedCornerShape(12.sdp)
//    ) {
        Column(
            modifier = modifier
                .border(
                    border= BorderStroke(
                        width = 2.sdp,
                        color = colorResource(R.color.card_elevated)
                    ),
                    shape = RoundedCornerShape(36.sdp)
                ).padding(16.sdp)
        ) {
            Text(
                text = "Streak & Activity Comparison",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.padding(top=20.sdp,bottom = 16.sdp)
            )

            // Streak Comparison
            StreakComparisonSection(
                title = "Current Streak",
                user1Name = user1Name,
                user1Value = user1Calendar?.streak ?: 0,
                user2Name = user2Name,
                user2Value = user2Calendar?.streak ?: 0,
                color1 = color1,
                color2 = color2,
                unit = "days"
            )

            Spacer(modifier = Modifier.height(16.sdp))

            // Active Days Comparison
            StreakComparisonSection(
                title = "Total Active Days",
                user1Name = user1Name,
                user1Value = user1Calendar?.totalActiveDays ?: 0,
                user2Name = user2Name,
                user2Value = user2Calendar?.totalActiveDays ?: 0,
                color1 = color1,
                color2 = color2,
                unit = "days"
            )

            Spacer(modifier = Modifier.height(16.sdp))

            // Active Years Comparison
            ActiveYearsComparison(
                user1Name = user1Name,
                user1Years = user1Calendar?.activeYears ?: emptyList(),
                user2Name = user2Name,
                user2Years = user2Calendar?.activeYears ?: emptyList(),
                color1 = color1,
                color2 = color2
            )
        }
//    }
}

@Composable
private fun StreakComparisonSection(
    title: String,
    user1Name: String,
    user1Value: Int,
    user2Name: String,
    user2Value: Int,
    color1: Color,
    color2: Color,
    unit: String
) {
    Column {
        Text(
            text = title,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            ),
            modifier = Modifier.padding(bottom = 8.sdp)
        )

        // Visual comparison bars
        val maxValue = max(user1Value, user2Value).coerceAtLeast(1)
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.sdp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User 1
            UserValueBar(
                modifier = Modifier.weight(1f),
                username = user1Name,
                value = user1Value,
                maxValue = maxValue,
                color = color1,
                unit = unit
            )

            // VS indicator
            Box(
                modifier = Modifier
                    .size(24.sdp)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "VS",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                )
            }

            // User 2
            UserValueBar(
                modifier = Modifier.weight(1f),
                username = user2Name,
                value = user2Value,
                maxValue = maxValue,
                color = color2,
                unit = unit
            )
        }

        // Winner indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.sdp),
            horizontalArrangement = Arrangement.Center
        ) {
            val difference = user1Value - user2Value
            val winnerText = when {
                difference > 0 -> "$user1Name leads by $difference $unit"
                difference < 0 -> "$user2Name leads by ${-difference} $unit"
                else -> "Tied at $user1Value $unit"
            }
            
            Text(
                text = winnerText,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
private fun UserValueBar(
    modifier: Modifier = Modifier,
    username: String,
    value: Int,
    maxValue: Int,
    color: Color,
    unit: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Username
        Text(
            text = username,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            ),
            modifier = Modifier.padding(bottom = 4.sdp),
            maxLines = 1
        )

        // Value bar
        val fillPercentage = if (maxValue > 0) value.toFloat() / maxValue else 0f
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.sdp)
                .background(
                    color.copy(alpha = 0.2f),
                    RoundedCornerShape(6.sdp)
                )
                .border(
                    width = 1.sdp,
                    color = color.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(6.sdp)
                )
        ) {
            // Filled portion
            Box(
                modifier = Modifier
                    .fillMaxWidth(fillPercentage)
                    .fillMaxHeight()
                    .background(
                        color,
                        RoundedCornerShape(6.sdp)
                    )
                    .align(Alignment.CenterStart)
            )

            // Value text
            Text(
                text = value.toString(),
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Unit label
        Text(
            text = unit,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 10.sp,
                color = color
            ),
            modifier = Modifier.padding(top = 4.sdp)
        )
    }
}

@Composable
private fun ActiveYearsComparison(
    user1Name: String,
    user1Years: List<Int>,
    user2Name: String,
    user2Years: List<Int>,
    color1: Color,
    color2: Color
) {
    Column {
        Text(
            text = "Active Years",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            ),
            modifier = Modifier.padding(bottom = 8.sdp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.sdp)
        ) {
            // User 1 years
            YearsList(
                modifier = Modifier.weight(1f),
                username = user1Name,
                years = user1Years,
                color = color1
            )

            // User 2 years
            YearsList(
                modifier = Modifier.weight(1f),
                username = user2Name,
                years = user2Years,
                color = color2
            )
        }
    }
}

@Composable
private fun YearsList(
    modifier: Modifier = Modifier,
    username: String,
    years: List<Int>,
    color: Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$username (${years.size} years)",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            ),
            modifier = Modifier.padding(bottom = 4.sdp),
            maxLines = 1
        )

        // Years display
        if (years.isNotEmpty()) {
            val displayYears = if (years.size > 4) {
                years.take(3) + listOf(-1) // -1 represents "..."
            } else {
                years
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.sdp),
                modifier = Modifier.fillMaxWidth()
            ) {
                displayYears.forEach { year ->
                    if (year == -1) {
                        Text(
                            text = "...",
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier
                                .background(
                                    color.copy(alpha = 0.3f),
                                    RoundedCornerShape(4.sdp)
                                )
                                .padding(horizontal = 4.sdp, vertical = 2.sdp)
                        )
                    } else {
                        Text(
                            text = year.toString(),
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = 10.sp,
                                color = Color.White
                            ),
                            modifier = Modifier
                                .background(
                                    color.copy(alpha = 0.6f),
                                    RoundedCornerShape(4.sdp)
                                )
                                .padding(horizontal = 4.sdp, vertical = 2.sdp)
                        )
                    }
                }
            }
        } else {
            Text(
                text = "No data",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            )
        }
    }
}