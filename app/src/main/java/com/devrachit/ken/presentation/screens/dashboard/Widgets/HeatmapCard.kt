package com.devrachit.ken.presentation.screens.dashboard.Widgets

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter10Lh12Fw400
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh16Fw600
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HeatmapCard(
    modifier: Modifier = Modifier,
    currentTimestamp: Double,
    calenderDetails: String,
    activeYears: List<Int>,
    activeDays: Int,
    streak : Int

) {

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
            .wrapContentHeight()
            .clip(RoundedCornerShape(10.sdp))
            .border(
                border = BorderStroke(
                    width = 2.sdp,
                    color = colorResource(R.color.card_elevated)
                ),
                shape = RoundedCornerShape(36.sdp),
            )
            .padding( top=30.sdp,end = 10.sdp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.sdp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Activity Heatmap",
                modifier = Modifier.padding(bottom = 10.sdp, start = 10.sdp),
                color = colorResource(R.color.white),
                style = TextStyleInter20Lh24Fw700()
            )
            LazyRow(
                modifier = Modifier.padding(start = 0.sdp, bottom = 5.sdp)
            )
            {
                items(activeYears.size)
                {
                    Text(
                        text = activeYears[it].toString(),
                        modifier = Modifier
                            .padding(10.sdp)
                            .border(
                                border = BorderStroke(
                                    width = 2.sdp,
                                    color = colorResource(R.color.white).copy(alpha = 0.5f)
                                ), shape = RoundedCornerShape(16.sdp)
                            )
                            .padding(horizontal=10.sdp, vertical=5.sdp)
                        ,
                        color = colorResource(R.color.white),
                        style = TextStyleInter10Lh12Fw400()

                        )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                 modifier = Modifier.padding(bottom =20.sdp).fillMaxWidth()
            )
            {
                Text(
                    text = "Active Days: $activeDays",
                    modifier = Modifier.padding(start = 10.sdp).alpha(0.9f),
                    color = colorResource(R.color.white),
                    style = TextStyleInter10Lh12Fw400()
                )
                Text(
                    text = "Streak: $streak",
                    modifier = Modifier.padding( end=10.sdp).alpha(0.9f),
                    color = colorResource(R.color.white),
                    style = TextStyleInter10Lh12Fw400()
                )
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                HeatmapRevamp(
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
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(
    showBackground = true,
    backgroundColor = 0xFF121212
)
fun HeatmapCardPreview() {
    // Sample data
    val sampleTimestamp = 1745875744.831664
    val sampleCalendarData =
        "{\"1742284800\":5,\"1742371200\":10,\"1742457600\":7,\"1742544000\":3,\"1742630400\":8,\"1742716800\":12,\"1742803200\":6,\"1742889600\":9}"

    HeatmapCard(
        currentTimestamp = sampleTimestamp,
        calenderDetails = sampleCalendarData,
        activeYears = listOf(2023, 2022, 2021, 2020, 2019, 2018, 2017, 2016, 2015, 2014, 2013, 2012, 2011, 2010, 2009, 2008, 2007, 2006, 2005, 2004, 2003, 2002),
        activeDays = 100,
        streak = 30
    )
}