package com.devrachit.ken.presentation.screens.dashboard.compareusers.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.compareusers.UserComparisonData
import com.devrachit.ken.presentation.screens.dashboard.home.QuestionProgressUiState
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun ComparisonChart(
    user1Data: UserComparisonData,
    user2Data: UserComparisonData,
    user1Name: String,
    user2Name: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.sdp),
    ) {
        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.sdp)
        ) {
            LegendItem(
                color = colorResource(R.color.easy_filled_blue),
                label = user1Name,
                modifier = Modifier.weight(1f)
            )
            LegendItem(
                color = colorResource(R.color.medium_filled_yellow),
                label = user2Name,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.sdp))

        // Chart sections
        ComparisonBarChart(
            title = "Easy Problems",
            user1Value = user1Data.questionProgress.easySolvedCount,
            user1Total = user1Data.questionProgress.easyTotalCount,
            user2Value = user2Data.questionProgress.easySolvedCount,
            user2Total = user2Data.questionProgress.easyTotalCount,
            user1Color = colorResource(R.color.easy_filled_blue),
            user2Color = colorResource(R.color.medium_filled_yellow)
        )

        Spacer(modifier = Modifier.height(12.sdp))

        ComparisonBarChart(
            title = "Medium Problems",
            user1Value = user1Data.questionProgress.mediumSolvedCount,
            user1Total = user1Data.questionProgress.mediumTotalCount,
            user2Value = user2Data.questionProgress.mediumSolvedCount,
            user2Total = user2Data.questionProgress.mediumTotalCount,
            user1Color = colorResource(R.color.easy_filled_blue),
            user2Color = colorResource(R.color.medium_filled_yellow)
        )

        Spacer(modifier = Modifier.height(12.sdp))

        ComparisonBarChart(
            title = "Hard Problems",
            user1Value = user1Data.questionProgress.hardSolvedCount,
            user1Total = user1Data.questionProgress.hardTotalCount,
            user2Value = user2Data.questionProgress.hardSolvedCount,
            user2Total = user2Data.questionProgress.hardTotalCount,
            user1Color = colorResource(R.color.easy_filled_blue),
            user2Color = colorResource(R.color.medium_filled_yellow)
        )

        Spacer(modifier = Modifier.height(16.sdp))

        // Overall comparison
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.sdp)
        ) {
            ComparisonSummaryCard(
                title = user1Name,
                totalSolved = user1Data.questionProgress.solved,
                totalProblems = user1Data.questionProgress.total,
                color = colorResource(R.color.easy_filled_blue),
                modifier = Modifier.fillMaxWidth(0.5f)
            )

            ComparisonSummaryCard(
                title = user2Name,
                totalSolved = user2Data.questionProgress.solved,
                totalProblems = user2Data.questionProgress.total,
                color = colorResource(R.color.medium_filled_yellow),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.sdp)
                .background(color, RoundedCornerShape(2.sdp))
        )

        Text(
            text = label,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
            ),
            modifier = Modifier.padding(start = 8.sdp),
            maxLines = 1
        )
    }
}

@Composable
private fun ComparisonBarChart(
    title: String,
    user1Value: Int,
    user1Total: Int,
    user2Value: Int,
    user2Total: Int,
    user1Color: Color,
    user2Color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            ),
            modifier = Modifier.padding(bottom = 8.sdp)
        )

        val maxTotal = maxOf(user1Total, user2Total).coerceAtLeast(1)
        val user1Percentage = if (user1Total > 0) user1Value.toFloat() / user1Total else 0f
        val user2Percentage = if (user2Total > 0) user2Value.toFloat() / user2Total else 0f

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.sdp)
        ) {
            val barHeight = 14.dp.toPx()
            val spacing = 12.dp.toPx()

            // User 1 bar
            drawComparisonBar(
                value = user1Value,
                total = user1Total,
                percentage = user1Percentage,
                color = user1Color,
                y = 0f,
                barHeight = barHeight,
                canvasSize = size
            )

            // User 2 bar
            drawComparisonBar(
                value = user2Value,
                total = user2Total,
                percentage = user2Percentage,
                color = user2Color,
                y = barHeight + spacing,
                barHeight = barHeight,
                canvasSize = size
            )
        }

        // Values
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$user1Value/$user1Total",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 12.sp,
                    color = user1Color
                )
            )
            Text(
                text = "$user2Value/$user2Total",
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 12.sp,
                    color = user2Color
                )
            )
        }
    }
}

private fun DrawScope.drawComparisonBar(
    value: Int,
    total: Int,
    percentage: Float,
    color: Color,
    y: Float,
    barHeight: Float,
    canvasSize: Size
) {
    val barWidth = canvasSize.width
    val filledWidth = barWidth * percentage

    // Background bar
    drawRoundRect(
        color = color.copy(alpha = 0.2f),
        topLeft = Offset(0f, y),
        size = Size(barWidth, barHeight),
        cornerRadius = CornerRadius(barHeight / 2)
    )

    // Filled bar
    if (filledWidth > 0) {
        drawRoundRect(
            color = color,
            topLeft = Offset(0f, y),
            size = Size(filledWidth, barHeight),
            cornerRadius = CornerRadius(barHeight / 2)
        )
    }
}

@Composable
private fun ComparisonSummaryCard(
    title: String,
    totalSolved: Int,
    totalProblems: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(top = 12.sdp, start = 12.sdp, end = 12.sdp, bottom = 12.sdp)
            .border(
                border = BorderStroke(
                    width = 1.sdp,
                    color = colorResource(R.color.card_elevated)
                ),
                shape = RoundedCornerShape(16.sdp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text = title,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.8f)
            ),
            maxLines = 1,
            modifier=Modifier.padding(top=12.sdp,bottom=8.sdp)
        )

        Text(
            text = totalSolved.toString(),
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        )

        Text(
            text = "/ $totalProblems",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f)
            )
        )

        val percentage =
            if (totalProblems > 0) (totalSolved.toFloat() / totalProblems * 100).toInt() else 0
        Text(
            text = "$percentage%",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = color
            ),
            modifier = Modifier.padding(bottom=12.sdp)
        )
    }
}

@Preview
@Composable
fun ComparisonChartPreview() {
    // Create mock data for preview
    val user1Data = UserComparisonData(
        questionProgress = QuestionProgressUiState(
            easySolvedCount = 10,
            easyTotalCount = 20,
            mediumSolvedCount = 5,
            mediumTotalCount = 15,
            hardSolvedCount = 2,
            hardTotalCount = 5,
            solved = 17,
            total = 40
        )
    )
    val user2Data = UserComparisonData(
        questionProgress = QuestionProgressUiState(
            easySolvedCount = 8,
            easyTotalCount = 20,
            mediumSolvedCount = 7,
            mediumTotalCount = 15,
            hardSolvedCount = 3,
            hardTotalCount = 5,
            solved = 18,
            total = 40
        )
    )

    ComparisonChart(
        user1Data = user1Data,
        user2Data = user2Data,
        user1Name = "User 1",
        user2Name = "User 2"
    )
}

@Preview
@Composable
fun ComparisonSummaryCardPreview() {
    ComparisonSummaryCard(
        title = "John Doe",
        totalSolved = 125,
        totalProblems = 200,
        color = Color(0xFF4CAF50)
    )
}

@Preview
@Composable
fun ComparisonBarChartPreview() {
    ComparisonBarChart(
        title = "Easy Problems",
        user1Value = 15,
        user1Total = 20,
        user2Value = 12,
        user2Total = 20,
        user1Color = Color(0xFF2196F3),
        user2Color = Color(0xFFFFC107)
    )
}

@Preview
@Composable
fun LegendItemPreview() {
    LegendItem(
        color = Color(0xFF2196F3),
        label = "User 1"
    )
}
