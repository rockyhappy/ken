package com.devrachit.ken.presentation.screens.dashboard.Widgets

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.devrachit.ken.R
import com.devrachit.ken.utility.composeUtility.sdp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HeatmapCard(
    modifier:Modifier = Modifier,
    currentTimestamp : Double,
    calenderDetails :String
)
{

    val rawActivityData = parseCalendarData(calenderDetails)
    // Convert the parsed calendar data into a list of DayModel objects
    val dayModels = rawActivityData.map { (timestamp, contributions) ->
        val instant = Instant.ofEpochSecond(timestamp.toLong())
        val localDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            LocalDate.ofInstant(instant, ZoneOffset.UTC)
        } else {
            LocalDate.from(
                instant.atZone(ZoneOffset.UTC)
            )
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
    val activityData = ActivityData(dayModels)


    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(200.sdp)
            .clip(RoundedCornerShape(10.sdp))
            .border(
                border = BorderStroke(
                    width = 2.sdp,
                    color = colorResource(R.color.card_elevated)
                ),
                shape = RoundedCornerShape(36.sdp),
            )
            .padding(top = 20.sdp, end = 10.sdp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            Heatmap(
                activityData = activityData,
                currentTimestamp = currentTimestamp,
                modifier = Modifier
                    .fillMaxWidth()
            )
        } else {
            Text(
                text = "Activity visualization requires Android 14 or higher",
                modifier = Modifier.padding(16.sdp)
            )
        }
    }
}