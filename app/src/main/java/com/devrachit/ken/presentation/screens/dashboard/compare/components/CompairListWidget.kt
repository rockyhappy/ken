package com.devrachit.ken.presentation.screens.dashboard.compare.components

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import com.devrachit.ken.R
import com.devrachit.ken.domain.models.LeetCodeUserInfo
import com.devrachit.ken.domain.models.UserProfile
import com.devrachit.ken.presentation.screens.dashboard.Widgets.ActivityData
import com.devrachit.ken.presentation.screens.dashboard.Widgets.DayModel
import com.devrachit.ken.presentation.screens.dashboard.Widgets.HeatmapRevamp
import com.devrachit.ken.presentation.screens.dashboard.Widgets.KinkWidget
import com.devrachit.ken.presentation.screens.dashboard.Widgets.parseCalendarData
import com.devrachit.ken.presentation.screens.dashboard.home.QuestionProgressUiState
import com.devrachit.ken.ui.theme.TextStyleInter14Lh18Fw400
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw600
import com.devrachit.ken.ui.theme.TextStyleInter24Lh36Fw700
import com.devrachit.ken.utility.composeUtility.sdp
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.Paint
import android.graphics.RectF
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import coil.compose.AsyncImage
import com.devrachit.ken.presentation.screens.dashboard.Widgets.HeatmapRevamp2
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun CompareSinglePersonWidget(
    modifier: Modifier = Modifier,
    username: String,
    userInfo: LeetCodeUserInfo,
    userQuestionProfile: QuestionProgressUiState,
    currentTimestamp: Double,
    calenderDetails: String,
    activeYears: List<Int>,
    activeDays: Int,
    streak: Int,
    onViewProfile: (String) -> Unit = {},
    onCompareWith: (String) -> Unit = {},
    onRemoveUser: (String) -> Unit = {}
) {
    val rawActivityData = parseCalendarData(calenderDetails)
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
    
    // State for dropdown menu
    val expanded = remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier
            .background(colorResource(R.color.bg_neutral))
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
            .padding(top = 20.sdp, start = 20.sdp, end = 20.sdp),
        horizontalAlignment = androidx.compose.ui.Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            AsyncImage(
                model = userInfo.profile?.userAvatar,
                placeholder = painterResource(R.drawable.logo),
                contentDescription = "Example image for demonstration purposes",
                modifier = Modifier
//                    .padding(top = 30.sdp, start = 10.sdp)
                    .size(70.sdp)
                    .clip(RoundedCornerShape(16.sdp))
            )
            Column(
                modifier = Modifier
                    .padding(start = 10.sdp)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = username,
                    style = TextStyleInter20Lh24Fw600(),
                    modifier = Modifier.padding(top = 8.sdp, start = 10.sdp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
                Text(
                    text = userInfo.profile?.realName.toString(),
                    style = TextStyleInter14Lh18Fw400(),
                    color = Color.White,
                    modifier = Modifier
                        .padding(top = 10.sdp, start = 10.sdp)
                        .alpha(0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Menu",
                tint = Color.White,
                modifier = Modifier
                    .size(32.sdp)
                    .clickable { expanded.value = true }
                    .padding(4.sdp)
                    .align(Alignment.Top)
                    .background(colorResource(R.color.bg_neutral))
            )
        }
        
        // Dropdown menu
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
                .background(colorResource(R.color.bg_neutral))
                .clip(RoundedCornerShape(8.dp))
        ) {
            DropdownMenuItem(
                text = { Text("View Profile", color = Color.White) },
                onClick = {
                    onViewProfile(username)
                    expanded.value = false
                }
            )
            DropdownMenuItem(
                text = { Text("Compare with", color = Color.White) },
                onClick = {
                    onCompareWith(username)
                    expanded.value = false
                }
            )
            DropdownMenuItem(
                text = { Text("Remove", color = Color.White) },
                onClick = {
                    onRemoveUser(username)
                    expanded.value = false
                }
            )
        }
        
        Row(
            modifier = Modifier
                .padding(top = 20.sdp)
                .fillMaxWidth()
                .border(
                    border = BorderStroke(
                        width = 1.sdp,
                        color = colorResource(R.color.card_elevated)
                    ),
                    shape = RoundedCornerShape(26.sdp),
                )
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val arcBitmap = createArcBitmap(
                solved = userQuestionProfile.solved,
                total = userQuestionProfile.total,
                easyTotalCount = userQuestionProfile.easyTotalCount,
                easySolvedCount = userQuestionProfile.easySolvedCount,
                mediumTotalCount = userQuestionProfile.mediumTotalCount,
                mediumSolvedCount = userQuestionProfile.mediumSolvedCount,
                hardTotalCount = userQuestionProfile.hardTotalCount,
                hardSolvedCount = userQuestionProfile.hardSolvedCount
            )
            Image(
                bitmap = arcBitmap.asImageBitmap(),
                contentDescription = "Progress Arc",
                modifier = Modifier.padding(top = 20.sdp).size(100.sdp)
            )
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                // Easy problems
                KinkWidget(
                    colorResourceId = R.color.easy_filled_blue,
                    tag = "Easy",
                    totalCount = userQuestionProfile.easyTotalCount,
                    attemptedCount = userQuestionProfile.easySolvedCount,
                )

                // Medium problems
                KinkWidget(
                    colorResourceId = R.color.medium_filled_yellow,
                    tag = "Med.",
                    totalCount = userQuestionProfile.mediumTotalCount,
                    attemptedCount = userQuestionProfile.mediumSolvedCount,
                )

                // Hard problems
                KinkWidget(
                    colorResourceId = R.color.hard_filled_red,
                    tag = "Hard",
                    totalCount = userQuestionProfile.hardTotalCount,
                    attemptedCount = userQuestionProfile.hardSolvedCount,
                )
            }

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            HeatmapRevamp2(
                activityData = activityData,
                currentTimestamp = currentTimestamp,
                modifier = Modifier
                    .padding(top = 16.sdp, bottom = 16.sdp)
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(
                            width = 1.sdp,
                            color = colorResource(R.color.card_elevated)
                        ),
                        shape = RoundedCornerShape(26.sdp),
                    )
                    .padding(top = 16.sdp, end = 16.sdp)
            )
        } else {
            Text(
                text = "Activity visualization requires Android 14 or higher",
                modifier = Modifier.padding(16.sdp)
            )
        }
    }
}

@Composable
fun createArcBitmap(
    solved: Int,
    total: Int,
    easyTotalCount: Int,
    easySolvedCount: Int,
    mediumTotalCount: Int,
    mediumSolvedCount: Int,
    hardTotalCount: Int,
    hardSolvedCount: Int
): Bitmap {
    val context = LocalContext.current
    val size = 130
    val bitmap = createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    
    val strokeWidth = 10f
    val startAngle = 135f
    val gapAngle = 10f
    val totalSweepAngle = 250f
    
    val totalFloat = total.toFloat().takeIf { it > 0 } ?: 1f
    
    // Calculate sweep angles for each segment based on proportion
    val easyBaseSweepAngle = (easyTotalCount.toFloat() / totalFloat) * totalSweepAngle
    val mediumBaseSweepAngle = (mediumTotalCount.toFloat() / totalFloat) * totalSweepAngle
    val hardBaseSweepAngle = (hardTotalCount.toFloat() / totalFloat) * totalSweepAngle
    
    val easyFilledSweepAngle = (easySolvedCount.toFloat() / totalFloat) * totalSweepAngle
    val mediumFilledSweepAngle = (mediumSolvedCount.toFloat() / totalFloat) * totalSweepAngle
    val hardFilledSweepAngle = (hardSolvedCount.toFloat() / totalFloat) * totalSweepAngle
    
    val paint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        this.strokeWidth = strokeWidth
        strokeCap = Paint.Cap.ROUND
    }
    
    val padding = strokeWidth / 2
    val oval = RectF(padding, padding, size - padding, size - padding)
    
    // Draw base arcs (background)
    paint.color = ContextCompat.getColor(context, R.color.easy_base_blue)
    canvas.drawArc(oval, startAngle, easyBaseSweepAngle, false, paint)
    
    paint.color = ContextCompat.getColor(context, R.color.medium_base_yellow)
    canvas.drawArc(oval, startAngle + easyBaseSweepAngle + gapAngle, mediumBaseSweepAngle, false, paint)
    
    paint.color = ContextCompat.getColor(context, R.color.hard_base_red)
    canvas.drawArc(oval, startAngle + easyBaseSweepAngle + gapAngle + mediumBaseSweepAngle + gapAngle, hardBaseSweepAngle, false, paint)
    
    // Draw filled arcs (progress)
    paint.color = ContextCompat.getColor(context, R.color.easy_filled_blue)
    canvas.drawArc(oval, startAngle, easyFilledSweepAngle, false, paint)
    
    paint.color = ContextCompat.getColor(context, R.color.medium_filled_yellow)
    canvas.drawArc(oval, startAngle + easyBaseSweepAngle + gapAngle, mediumFilledSweepAngle, false, paint)
    
    paint.color = ContextCompat.getColor(context, R.color.hard_filled_red)
    canvas.drawArc(oval, startAngle + easyBaseSweepAngle + gapAngle + mediumBaseSweepAngle + gapAngle, hardFilledSweepAngle, false, paint)
    
    // Draw center text
    val mainTextPaint = Paint().apply {
        color = AndroidColor.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 20f
        isAntiAlias = true
        isFakeBoldText = true
    }
    
    val smallTextPaint = Paint().apply {
        color = AndroidColor.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 12f
        isAntiAlias = true
    }
    
    canvas.drawText(solved.toString(), size / 2f, size / 2f - 5f, mainTextPaint)
    canvas.drawText("/$total", size / 2f, size / 2f + 15f, smallTextPaint)

    return bitmap
}

@Preview(showBackground = true)
@Composable
fun CompareSinglePersonWidgetPreview() {
    val mockUserInfo = LeetCodeUserInfo(
        username = "john_doe",
        profile = UserProfile(
            ranking = 45000,
            realName = "John Doe",
            reputation = 1500,
            userAvatar = "https://example.com/avatar.png"
        )
    )

    val mockQuestionProfile = QuestionProgressUiState(
        solved = 150,
        total = 3521,
        easySolvedCount = 75,
        easyTotalCount = 873,
        mediumSolvedCount = 60,
        mediumTotalCount = 1826,
        hardSolvedCount = 15,
        hardTotalCount = 822
    )

    val mockCalendarData = "{\"1742284800\":5,\"1742371200\":10,\"1742457600\":7,\"1742544000\":3,\"1742630400\":8,\"1742716800\":12,\"1742803200\":6,\"1742889600\":9,\"1743033600\":4,\"1743120000\":8,\"1743206400\":11,\"1743292800\":6,\"1743379200\":7,\"1743465600\":9,\"1743552000\":13}"

    CompareSinglePersonWidget(
        username = "john_doe",
        userInfo = mockUserInfo,
        userQuestionProfile = mockQuestionProfile,
        currentTimestamp = 1745875744.831664,
        calenderDetails = mockCalendarData,
        activeYears = listOf(2024, 2023, 2022),
        activeDays = 85,
        streak = 12
    )
}
