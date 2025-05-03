package com.devrachit.ken.presentation.screens.dashboard.Widgets

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.devrachit.ken.domain.models.UserCalendar
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.text.SimpleDateFormat
import java.util.*

/**
 * A composable that displays a GitHub-style heatmap of activity for the last 4 months
 *
 * @param activityData Map of timestamps (seconds since epoch as strings) to activity counts
 * @param currentTimestamp Current timestamp in seconds since epoch
 */
@Composable
fun ActivityHeatmap(
    activityData: Map<String, Int>,
    currentTimestamp: Double,
    modifier: Modifier = Modifier
) {
    // Convert the string keys to Long timestamps
    val parsedData = remember(activityData) {
        activityData.mapKeys { (key, _) -> key.toLong() }
    }

    // Convert the current timestamp to a Date
    val currentDate = remember(currentTimestamp) {
        Date((currentTimestamp * 1000).toLong())
    }

    // Calculate the date 4 months ago
    val fourMonthsAgo = remember(currentDate) {
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(Calendar.MONTH, -4)
        calendar.time
    }

    // Filter the data to show only the last 4 months
    val filteredData = remember(parsedData, fourMonthsAgo) {
        parsedData.filter { (timestamp, _) ->
            val date = Date(timestamp * 1000)
            date.after(fourMonthsAgo) || date == fourMonthsAgo
        }
    }

    // Generate a continuous series of dates for the last 4 months
    val allDates = remember(fourMonthsAgo, currentDate) {
        val dates = mutableListOf<Date>()
        val calendar = Calendar.getInstance()
        calendar.time = fourMonthsAgo

        // Reset time to beginning of day
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val endCalendar = Calendar.getInstance()
        endCalendar.time = currentDate
        endCalendar.set(Calendar.HOUR_OF_DAY, 23)
        endCalendar.set(Calendar.MINUTE, 59)
        endCalendar.set(Calendar.SECOND, 59)

        while (calendar.time.before(endCalendar.time) || calendar.time == endCalendar.time) {
            dates.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        dates
    }

    // Organize dates into a grid format (7 rows for days of week, columns for weeks)
    val calendar = Calendar.getInstance()

    // First, organize by week
    val weeks = remember(allDates) {
        val result = mutableListOf<List<Date?>>()
        val weekMap = mutableMapOf<Int, MutableList<Pair<Int, Date>>>() // Week number -> List of (day of week, date) pairs

        // Group dates by week
        for (date in allDates) {
            calendar.time = date
            val weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 0-based (0 = Sunday)

            if (!weekMap.containsKey(weekOfYear)) {
                weekMap[weekOfYear] = mutableListOf()
            }
            weekMap[weekOfYear]?.add(Pair(dayOfWeek, date))
        }

        // Sort weeks and convert to grid format
        val sortedWeeks = weekMap.keys.sorted()
        for (weekNum in sortedWeeks) {
            val daysInWeek = weekMap[weekNum] ?: continue
            val weekArray = arrayOfNulls<Date>(7)

            // Place each day in its correct position in the week
            for ((dayOfWeek, date) in daysInWeek) {
                weekArray[dayOfWeek] = date
            }

            result.add(weekArray.toList())
        }

        result
    }

    // Format for month labels
    val monthFormat = remember { SimpleDateFormat("MMM", Locale.getDefault()) }

    // Generate month labels at the appropriate positions
    val monthLabels = remember(weeks) {
        val labels = mutableListOf<Pair<String, Int>>() // Month name, position (week number)
        var lastMonth = -1
        var weekOffset = 0

        for (weekIndex in weeks.indices) {
            val week = weeks[weekIndex]
            // Look for the first date in the week where the day is 1 (first of month)
            for (date in week) {
                if (date != null) {
                    calendar.time = date

                    // If it's the first week or the first day of a month
                    if (weekIndex == 0 || calendar.get(Calendar.DAY_OF_MONTH) == 1) {
                        val monthName = monthFormat.format(date)
                        val month = calendar.get(Calendar.MONTH)

                        // Add extra spacing between months for visual separation
                        if (lastMonth != -1 && month != lastMonth) {
                            weekOffset += 1
                        }
                        lastMonth = month

                        // Avoid duplicates
                        if (labels.isEmpty() || labels.last().first != monthName) {
                            labels.add(Pair(monthName, weekIndex + weekOffset))
                        }
                        break
                    }
                }
            }
        }

        labels
    }

    // Find maximum activity count for color scaling
    val maxActivity = remember(filteredData) {
        val max = filteredData.values.maxOrNull() ?: 10
        // Log maximum activity to debug color issues
        try { 
            Log.d("ActivityHeatmap", "Max activity count: $max from ${filteredData.size} entries")
        } catch (e: Exception) {
            // Ignore log errors
        }
        max
    }

    Column(modifier = modifier.padding(16.dp)) {
        // Heatmap title
        Text(
            text = "Activity Heatmap",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Month labels
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, bottom = 4.dp) // Align with grid below
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        monthLabels.forEach { (month, index) ->
                            Text(
                                text = month,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .padding(start = (index * 14).dp)
                                    .zIndex(1f),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    // Day of week labels
                    Column(
                        modifier = Modifier.width(24.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        val days = listOf("S", "M", "T", "W", "T", "F", "S")
                        days.forEach { day ->
                            Text(
                                text = day,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(end = 4.dp, top = 4.dp, bottom = 4.dp),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }

                    // Heatmap grid
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    ) {
                        // Use Canvas for drawing the grid of squares
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val cellSize = 12.dp.toPx()
                            val cellPadding = 2.dp.toPx()

                            // Track current month for visual separation
                            var currentMonth = -1
                            var monthOffsetMap = mutableMapOf<Int, Float>()
                            var totalOffset = 0f
                            var monthSeparators = mutableListOf<Pair<Float, Float>>() // x position, height

                            for (weekIndex in weeks.indices) {
                                val week = weeks[weekIndex]
                                for (dayIndex in week.indices) {
                                    val date = week[dayIndex]

                                    if (date != null) {
                                        calendar.time = date
                                        calendar.set(Calendar.HOUR_OF_DAY, 0)
                                        calendar.set(Calendar.MINUTE, 0)
                                        calendar.set(Calendar.SECOND, 0)
                                        calendar.set(Calendar.MILLISECOND, 0)

                                        val timestamp = calendar.timeInMillis / 1000
                                        val activityCount = filteredData[timestamp] ?: 0

                                        // Add month separation
                                        val month = calendar.get(Calendar.MONTH)
                                        var xOffset = 0f

                                        if (currentMonth != -1 && month != currentMonth) {
                                            // Add separation between months
                                            totalOffset += cellPadding * 5  // Wider separation
                                            monthOffsetMap[weekIndex] = totalOffset
                                            
                                            // Store position for drawing separator line
                                            val sepX = weekIndex * (cellSize + cellPadding) + totalOffset - (cellPadding * 2.5f)
                                            monthSeparators.add(Pair(sepX, 7 * (cellSize + cellPadding)))
                                        }
                                        currentMonth = month

                                        // Apply accumulated offset
                                        xOffset = monthOffsetMap.filter { it.key <= weekIndex }
                                            .values.sum()

                                        // Calculate position
                                        val x = weekIndex * (cellSize + cellPadding) + xOffset
                                        val y = dayIndex * (cellSize + cellPadding)

                                        // Draw the cell
                                        drawRect(
                                            color = getColorForActivity(activityCount, maxActivity),
                                            topLeft = Offset(x, y),
                                            size = Size(cellSize, cellSize)
                                        )

                                        // Debug color log for first few cells
                                        if (weekIndex < 3 && dayIndex < 3) {
                                            try {
                                                Log.d("ActivityHeatmap", "Cell[$weekIndex,$dayIndex] date=${Date(timestamp*1000)}, timestamp=$timestamp, count=$activityCount, color=${getColorForActivity(activityCount, maxActivity)}")
                                            } catch (e: Exception) {
                                                // Ignore log errors
                                            }
                                        }
                                    }
                                }
                            }
                            
                            // Draw month separators
                            for ((x, height) in monthSeparators) {
                                drawLine(
                                    color = Color.LightGray.copy(alpha = 0.5f),
                                    start = Offset(x, 0f),
                                    end = Offset(x, height),
                                    strokeWidth = cellPadding / 2
                                )
                            }
                        }
                    }
                }
            }

            // Color legend
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Less",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(end = 4.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                // Show sample cells with increasing intensity
                Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    val levels = 5
                    repeat(levels) { i ->
                        val activityLevel = if (i == 0) 0 else (i * maxActivity) / (levels - 1)
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(getColorForActivity(activityLevel, maxActivity))
                        )
                    }
                }

                Text(
                    text = "More",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // Activity statistics
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "${filteredData.size}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Active days",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${filteredData.values.sum()}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Total activities",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

/**
 * Maps an activity count to a color based on intensity
 */
private fun getColorForActivity(count: Int, maxCount: Int): Color {
    // Always show a color for non-zero values
    return when {
        count == 0 -> Color(0xFFEBEDF0) // Light gray for no activity
        maxCount <= 4 -> {
            // For small maximum values, use discrete color steps
            when (count) {
                1 -> Color(0xFF9BE9A8) // Light green
                2 -> Color(0xFF40C463) // Medium green
                3 -> Color(0xFF30A14E) // Darker green
                else -> Color(0xFF216E39) // Darkest green
            }
        }
        else -> {
            // For larger maximum values, use proportional color scaling
            when {
                count < maxCount / 4 + 1 -> Color(0xFF9BE9A8) // Light green
                count < maxCount / 2 + 1 -> Color(0xFF40C463) // Medium green
                count < 3 * maxCount / 4 + 1 -> Color(0xFF30A14E) // Darker green
                else -> Color(0xFF216E39) // Darkest green
            }
        }
    }
}

/**
 * Parse the submission calendar JSON string to a map of timestamps to activity counts
 */
fun parseSubmissionCalendar(submissionCalendar: String): Map<String, Int> {
    return try {
        val jsonObject = Json.decodeFromString<JsonObject>(submissionCalendar)
        val result = jsonObject.mapKeys { it.key }
            .mapValues { entry -> entry.value.jsonPrimitive.content.toInt() }

        // Debug log
        Log.d("ActivityHeatmap", "Parsed ${result.size} entries from submission calendar")
        // Log a few samples
        result.entries.take(5).forEach {
            Log.d("ActivityHeatmap", "Sample entry: ${Date(it.key.toLong() * 1000)} => ${it.value}")
        }

        result
    } catch (e: Exception) {
        Log.e("ActivityHeatmap", "Error parsing submission calendar", e)
        emptyMap()
    }
}

/**
 * Preview for the ActivityHeatmap using sample data from the API
 */
@Preview(showBackground = true)
@Composable
fun ActivityHeatmapPreview() {
    // Sample submission calendar data from the API
    val sampleCalendarString = "{\"1735689600\": 3, \"1735862400\": 6, \"1735948800\": 8, \"1736208000\": 3, \"1736294400\": 1, \"1736380800\": 8, \"1736467200\": 4, \"1736553600\": 16, \"1736640000\": 8, \"1736726400\": 10, \"1736812800\": 2, \"1736899200\": 2, \"1736985600\": 5, \"1737072000\": 1, \"1737158400\": 7, \"1737244800\": 3, \"1737331200\": 3, \"1737417600\": 3, \"1737504000\": 2, \"1738368000\": 4, \"1738454400\": 12, \"1738800000\": 1, \"1739059200\": 1, \"1739145600\": 4, \"1739491200\": 2, \"1739750400\": 7, \"1739836800\": 1, \"1740009600\": 1, \"1740355200\": 3, \"1740441600\": 2, \"1740614400\": 6, \"1740700800\": 2, \"1740787200\": 7, \"1741046400\": 1, \"1741219200\": 1, \"1742774400\": 10, \"1743811200\": 1, \"1744761600\": 2, \"1714176000\": 5, \"1714348800\": 1, \"1714521600\": 2, \"1714608000\": 2, \"1714694400\": 3, \"1714780800\": 1, \"1714867200\": 9, \"1714953600\": 1, \"1715040000\": 2, \"1715126400\": 1, \"1715212800\": 3, \"1715299200\": 1, \"1715385600\": 1, \"1715472000\": 7, \"1715558400\": 2, \"1715644800\": 2, \"1715731200\": 1, \"1715817600\": 1, \"1715904000\": 1, \"1715990400\": 1, \"1716249600\": 1, \"1716336000\": 1, \"1716681600\": 2, \"1717113600\": 1, \"1717200000\": 1, \"1717632000\": 3, \"1718150400\": 1, \"1719187200\": 2, \"1720915200\": 1, \"1721606400\": 2, \"1723334400\": 1, \"1724889600\": 6, \"1730505600\": 6, \"1730592000\": 1, \"1730764800\": 3, \"1730937600\": 7, \"1731024000\": 4, \"1731628800\": 1, \"1731715200\": 3, \"1731974400\": 4, \"1732060800\": 1, \"1732147200\": 6, \"1732492800\": 2, \"1732665600\": 1, \"1732752000\": 5, \"1733011200\": 10, \"1733097600\": 3, \"1733184000\": 2, \"1733270400\": 3, \"1733356800\": 4, \"1733443200\": 6, \"1733616000\": 18, \"1733702400\": 7, \"1733788800\": 3, \"1733875200\": 9, \"1733961600\": 2, \"1734048000\": 18, \"1734134400\": 7, \"1734220800\": 1, \"1734566400\": 7, \"1734652800\": 1, \"1734739200\": 10, \"1734825600\": 13, \"1735171200\": 13, \"1735257600\": 6, \"1735430400\": 11, \"1735516800\": 4, \"1735603200\": 2}"
    val activityData = parseSubmissionCalendar(sampleCalendarString)
    
    // Current timestamp from the API
    val currentTimestamp = 1745748048.972641
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        ActivityHeatmap(
            activityData = activityData,
            currentTimestamp = currentTimestamp,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
    }
}

/**
 * Composable to display user calendar data including heatmap and streak information
 */
@Composable
fun UserCalendarView(
    userCalendar: UserCalendar,
    currentTimestamp: Double,
    modifier: Modifier = Modifier
) {
    val activityData = remember(userCalendar.submissionCalendar) {
        parseSubmissionCalendar(userCalendar.submissionCalendar)
    }
    
    Column(modifier = modifier.fillMaxWidth()) {
        // Display streak information
        Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "${userCalendar.streak}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Current Streak",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${userCalendar.totalActiveDays}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Total Active Days",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        
        // Display heatmap
        ActivityHeatmap(
            activityData = activityData,
            currentTimestamp = currentTimestamp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Preview the full user calendar view with sample data
 */
@Preview(showBackground = true)
@Composable
fun UserCalendarViewPreview() {
    val sampleCalendarString = "{\"1735689600\": 3, \"1735862400\": 6, \"1735948800\": 8, \"1736208000\": 3, \"1736294400\": 1, \"1736380800\": 8, \"1736467200\": 4, \"1736553600\": 16, \"1736640000\": 8, \"1736726400\": 10, \"1736812800\": 2, \"1736899200\": 2, \"1736985600\": 5, \"1737072000\": 1, \"1737158400\": 7, \"1737244800\": 3, \"1737331200\": 3, \"1737417600\": 3, \"1737504000\": 2, \"1738368000\": 4, \"1738454400\": 12, \"1738800000\": 1, \"1739059200\": 1, \"1739145600\": 4, \"1739491200\": 2, \"1739750400\": 7, \"1739836800\": 1, \"1740009600\": 1, \"1740355200\": 3, \"1740441600\": 2, \"1740614400\": 6, \"1740700800\": 2, \"1740787200\": 7, \"1741046400\": 1, \"1741219200\": 1, \"1742774400\": 10, \"1743811200\": 1, \"1744761600\": 2, \"1714176000\": 5, \"1714348800\": 1, \"1714521600\": 2, \"1714608000\": 2, \"1714694400\": 3, \"1714780800\": 1, \"1714867200\": 9, \"1714953600\": 1, \"1715040000\": 2, \"1715126400\": 1, \"1715212800\": 3, \"1715299200\": 1, \"1715385600\": 1, \"1715472000\": 7, \"1715558400\": 2, \"1715644800\": 2, \"1715731200\": 1, \"1715817600\": 1, \"1715904000\": 1, \"1715990400\": 1, \"1716249600\": 1, \"1716336000\": 1, \"1716681600\": 2, \"1717113600\": 1, \"1717200000\": 1, \"1717632000\": 3, \"1718150400\": 1, \"1719187200\": 2, \"1720915200\": 1, \"1721606400\": 2, \"1723334400\": 1, \"1724889600\": 6, \"1730505600\": 6, \"1730592000\": 1, \"1730764800\": 3, \"1730937600\": 7, \"1731024000\": 4, \"1731628800\": 1, \"1731715200\": 3, \"1731974400\": 4, \"1732060800\": 1, \"1732147200\": 6, \"1732492800\": 2, \"1732665600\": 1, \"1732752000\": 5, \"1733011200\": 10, \"1733097600\": 3, \"1733184000\": 2, \"1733270400\": 3, \"1733356800\": 4, \"1733443200\": 6, \"1733616000\": 18, \"1733702400\": 7, \"1733788800\": 3, \"1733875200\": 9, \"1733961600\": 2, \"1734048000\": 18, \"1734134400\": 7, \"1734220800\": 1, \"1734566400\": 7, \"1734652800\": 1, \"1734739200\": 10, \"1734825600\": 13, \"1735171200\": 13, \"1735257600\": 6, \"1735430400\": 11, \"1735516800\": 4, \"1735603200\": 2}"
    
    val sampleUserCalendar = UserCalendar(
        activeYears = listOf(2022, 2023, 2024, 2025),
        streak = 18,
        totalActiveDays = 106,
        dccBadges = emptyList(),
        submissionCalendar = sampleCalendarString
    )
    
    val currentTimestamp = 1745748048.972641
    
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            UserCalendarView(
                userCalendar = sampleUserCalendar,
                currentTimestamp = currentTimestamp,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
        }
    }
}