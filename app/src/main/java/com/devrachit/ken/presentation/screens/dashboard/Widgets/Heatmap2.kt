package com.devrachit.ken.presentation.screens.dashboard.Widgets

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devrachit.ken.domain.models.UserCalendar
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import com.devrachit.ken.R
import com.devrachit.ken.utility.composeUtility.sdp

/**
 * A composable that displays a GitHub-style heatmap showing activity over the last 4 months
 */
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun Heatmap(
    activityData: ActivityData,
    currentTimestamp: Double,
    modifier: Modifier = Modifier
) {
    val heatmap1 = colorResource(id = R.color.heatmap1)
    val heatmap2 = colorResource(id = R.color.heatmap2)
    val heatmap3 = colorResource(id = R.color.heatmap3)
    val heatmap4 = colorResource(id = R.color.heatmap4)
    val heatmap5 = Color.Transparent

    val cellSize = 14.sdp
    val cellSpacing = 3.dp
    val cornerRadius = 4.dp
    val currentDate = remember(currentTimestamp) {
        val instant = Instant.ofEpochSecond(currentTimestamp.toLong())
        LocalDate.ofInstant(instant, ZoneOffset.UTC)
    }


    val currentDayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val currentMonth = currentDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val currentDayOfMonth = currentDate.dayOfMonth


    val dayInfo = "Current day: $currentDayOfWeek, $currentMonth $currentDayOfMonth"
    val currentMonthData = activityData.dayModels.filter { dayModel ->
        val localDate = LocalDate.of(dayModel.year, dayModel.monthPosition + 1, dayModel.day)
        localDate.month == currentDate.month && localDate.year == currentDate.year
    }
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text=currentMonthData.toString()
        )
        Canvas(modifier = Modifier.fillMaxSize()) {

            val dayToActivityMap = currentMonthData.associateBy { it.day }
            

            val firstDayOfMonth = LocalDate.of(currentDate.year, currentDate.month, 1)
            val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 0 = Sunday, 1 = Monday, etc.
            

            for (i in 1..currentDayOfMonth) {
                val dayModel = dayToActivityMap[i]
                val color = when {
                    dayModel == null -> Color.Gray.copy(alpha = 0.2f)
                    dayModel.contributions > 10 -> heatmap1
                    dayModel.contributions > 5 -> heatmap2
                    dayModel.contributions > 2 -> heatmap3
                    else -> heatmap4
                }
                

                val dayOfWeek = (firstDayOfWeek + i - 1) % 7
                val weekNumber = (firstDayOfWeek + i - 1) / 7
                
                val xOffset = weekNumber * (cellSize.toPx() + cellSpacing.toPx())
                val yOffset = dayOfWeek * (cellSize.toPx() + cellSpacing.toPx())
                
                drawRoundRect(
                    color = color,
                    size = Size(width = cellSize.toPx(), height = cellSize.toPx()),
                    topLeft = Offset(xOffset, yOffset),
                    cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
                )
            }
        }
    }
}


/**
 * Parse the submission calendar JSON string to a map of timestamps to activity counts
 */
fun parseCalendarData(submissionCalendar: String): Map<String, Int> {
    return try {
        val jsonObject = Json.decodeFromString<JsonObject>(submissionCalendar)
        jsonObject.mapKeys { it.key }
            .mapValues { entry -> entry.value.jsonPrimitive.content.toInt() }
    } catch (e: Exception) {
        emptyMap()
    }
}

/**
 * Preview the heatmap with sample data
 */
@Preview(showBackground = true)
@Composable
fun GithubStyleHeatmapPreview() {
    // Sample data provided by the user
    val sampleCalendarString =
        "{\"1735689600\": 3, \"1735862400\": 6, \"1735948800\": 8, \"1736208000\": 3, \"1736294400\": 1, \"1736380800\": 8, \"1736467200\": 4, \"1736553600\": 16, \"1736640000\": 8, \"1736726400\": 10, \"1736812800\": 2, \"1736899200\": 2, \"1736985600\": 5, \"1737072000\": 1, \"1737158400\": 7, \"1737244800\": 3, \"1737331200\": 3, \"1737417600\": 3, \"1737504000\": 2, \"1738368000\": 4, \"1738454400\": 12, \"1738800000\": 1, \"1739059200\": 1, \"1739145600\": 4, \"1739491200\": 2, \"1739750400\": 7, \"1739836800\": 1, \"1740009600\": 1, \"1740355200\": 3, \"1740441600\": 2, \"1740614400\": 6, \"1740700800\": 2, \"1740787200\": 7, \"1741046400\": 1, \"1741219200\": 1, \"1742774400\": 10, \"1743811200\": 1, \"1744761600\": 2, \"1714176000\": 5, \"1714348800\": 1, \"1714521600\": 2, \"1714608000\": 2, \"1714694400\": 3, \"1714780800\": 1, \"1714867200\": 9, \"1714953600\": 1, \"1715040000\": 2, \"1715126400\": 1, \"1715212800\": 3, \"1715299200\": 1, \"1715385600\": 1, \"1715472000\": 7, \"1715558400\": 2, \"1715644800\": 2, \"1715731200\": 1, \"1715817600\": 1, \"1715904000\": 1, \"1715990400\": 1, \"1716249600\": 1, \"1716336000\": 1, \"1716681600\": 2, \"1717113600\": 1, \"1717200000\": 1, \"1717632000\": 3, \"1718150400\": 1, \"1719187200\": 2, \"1720915200\": 1, \"1721606400\": 2, \"1723334400\": 1, \"1724889600\": 6, \"1730505600\": 6, \"1730592000\": 1, \"1730764800\": 3, \"1730937600\": 7, \"1731024000\": 4, \"1731628800\": 1, \"1731715200\": 3, \"1731974400\": 4, \"1732060800\": 1, \"1732147200\": 6, \"1732492800\": 2, \"1732665600\": 1, \"1732752000\": 5, \"1733011200\": 10, \"1733097600\": 3, \"1733184000\": 2, \"1733270400\": 3, \"1733356800\": 4, \"1733443200\": 6, \"1733616000\": 18, \"1733702400\": 7, \"1733788800\": 3, \"1733875200\": 9, \"1733961600\": 2, \"1734048000\": 18, \"1734134400\": 7, \"1734220800\": 1, \"1734566400\": 7, \"1734652800\": 1, \"1734739200\": 10, \"1734825600\": 13, \"1735171200\": 13, \"1735257600\": 6, \"1735430400\": 11, \"1735516800\": 4, \"1735603200\": 2}"
    val rawActivityData = parseCalendarData(sampleCalendarString)
    // Convert the parsed calendar data into a list of DayModel objects
    val dayModels = rawActivityData.map { (timestamp, contributions) ->
        val instant = Instant.ofEpochSecond(timestamp.toLong())
        val localDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            LocalDate.ofInstant(instant, ZoneOffset.UTC)
        } else {
            TODO("VERSION.SDK_INT < UPSIDE_DOWN_CAKE")
        }

        DayModel(
            day = localDate.dayOfMonth,
            month = localDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            year = localDate.year,
            contributions = contributions,
            monthPosition = localDate.monthValue - 1,
            dayName = localDate.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        )
    }.sortedWith(compareBy({ it.year }, { it.monthPosition }, { it.day }))
    // Create ActivityData object with sorted dayModels
    val activityData = ActivityData(dayModels)

    val currentTimestamp = 1745748048.972641

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bg_neutral)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            Heatmap(
                activityData = activityData,
                currentTimestamp = currentTimestamp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            )
        }
    }

}

data class ActivityData(
    val dayModels: List<DayModel>
)

data class DayModel(
    val day: Int,
    val month: String,
    val year: Int,
    val dayName: String,
    val contributions: Int,
    val monthPosition: Int
)
