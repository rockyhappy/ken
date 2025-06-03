package com.devrachit.ken.presentation.screens.dashboard.compareusers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun ComparisonProgressGraph(
    title: String,
    user1Name: String,
    user1Value: Int,
    user1Total: Int,
    user2Name: String,
    user2Value: Int,
    user2Total: Int,
    color1: Color,
    color2: Color,
    modifier: Modifier = Modifier
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
            modifier = Modifier.padding(16.sdp)
        ) {
            Text(
                text = title,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.8f)
                ),
                modifier = Modifier.padding(start=8.sdp,bottom = 16.sdp)
            )

            // User comparison bars
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.sdp)
            ) {
                // User 1
                ProgressColumn(
                    modifier = Modifier.weight(1f),
                    username = user1Name,
                    solved = user1Value,
                    total = user1Total,
                    color = color1
                )

                // User 2
                ProgressColumn(
                    modifier = Modifier.weight(1f),
                    username = user2Name,
                    solved = user2Value,
                    total = user2Total,
                    color = color2
                )
            }

            // Comparison summary
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.sdp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val user1Percentage = if (user1Total > 0) (user1Value.toFloat() / user1Total * 100).toInt() else 0
                val user2Percentage = if (user2Total > 0) (user2Value.toFloat() / user2Total * 100).toInt() else 0
                val difference = user1Value - user2Value

                Text(
                    text = when {
                        difference > 0 -> "$user1Name leads by $difference"
                        difference < 0 -> "$user2Name leads by ${-difference}"
                        else -> "Tied at $user1Value"
                    },
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
            }
//        }
    }
}

@Composable
private fun ProgressColumn(
    modifier: Modifier = Modifier,
    username: String,
    solved: Int,
    total: Int,
    color: Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Username
        Text(
            text = username,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            ),
            modifier = Modifier.padding(bottom = 8.sdp),
            maxLines = 1
        )

        // Progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.sdp)
                .background(
                    color.copy(alpha = 0.2f),
                    RoundedCornerShape(8.sdp)
                )
                .border(
                    width = 1.sdp,
                    color = color.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.sdp)
                )
        ) {
            // Filled portion
            val percentage = if (total > 0) solved.toFloat() / total else 0f
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(percentage)
                    .background(
                        color,
                        RoundedCornerShape(8.sdp)
                    )
                    .align(Alignment.BottomCenter)
            )

            // Value text
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = solved.toString(),
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "/ $total",
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
            }
        }

        // Percentage
        val percentage = if (total > 0) (solved.toFloat() / total * 100).toInt() else 0
        Text(
            text = "$percentage%",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = color
            ),
            modifier = Modifier.padding(top = 8.sdp)
        )
    }
}