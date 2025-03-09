package com.devrachit.ken.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Composable
fun LeetCodeHeatmap(
    contributions: Map<String, Int>,
    modifier: Modifier = Modifier,
    cellSize: Float = 10f,
    cellSpacing: Float = 2f
) {
    val today = LocalDate.now()
    val startDate = today.minus(365, ChronoUnit.DAYS)

    val maxContribution = contributions.values.maxOrNull() ?: 0

    Column(modifier = modifier) {
        Text(
            text = "LeetCode Contributions",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Canvas(modifier = Modifier.fillMaxWidth().height(130.dp)) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Calculate weeks
            val weeks = ChronoUnit.WEEKS.between(startDate, today).toInt() + 1

            // Draw heatmap cells
            var currentDate = startDate
            while (!currentDate.isAfter(today)) {
                val dayOfWeek = currentDate.dayOfWeek.value % 7  // 0-6, Sunday is 0
                val weeksSince = ChronoUnit.WEEKS.between(startDate, currentDate).toInt()

                val x = weeksSince * (cellSize + cellSpacing)
                val y = dayOfWeek * (cellSize + cellSpacing)

                val contributionCount = contributions[currentDate.toString()] ?: 0
                val intensity = if (maxContribution > 0) contributionCount.toFloat() / maxContribution else 0f

                // Color based on contribution intensity
                val color = when {
                    contributionCount == 0 -> Color(0xFFEBEDF0)
                    intensity < 0.25 -> Color(0xFF9BE9A8)
                    intensity < 0.5 -> Color(0xFF40C463)
                    intensity < 0.75 -> Color(0xFF30A14E)
                    else -> Color(0xFF216E39)
                }

                drawRect(
                    color = color,
                    topLeft = Offset(x, y),
                    size = Size(cellSize, cellSize)
                )

                currentDate = currentDate.plusDays(1)
            }
        }
    }
}