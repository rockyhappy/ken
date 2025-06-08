package com.devrachit.ken.widget.streakWidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.widget.RemoteViews
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import com.devrachit.ken.R
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.presentation.screens.dashboard.ActivityContent.MainActivity
import com.devrachit.ken.presentation.screens.dashboard.Widgets.ActivityData
import com.devrachit.ken.presentation.screens.dashboard.Widgets.DayModel
import com.devrachit.ken.presentation.screens.dashboard.Widgets.parseCalendarData
import com.devrachit.ken.utility.NetworkUtility.Resource
import com.devrachit.ken.utility.composeUtility.sdp
import com.devrachit.ken.utility.constants.Constants.Companion.DEFAULT_USERNAME
import com.devrachit.ken.widget.WidgetEntryPoint
import com.devrachit.ken.widget.WidgetUpdateReceiver
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Locale

class DimensionWidgetProvider : AppWidgetProvider() {
    private val widgetScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // Schedule periodic updates
        WidgetUpdateReceiver.schedulePeriodicUpdates(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        // Cancel periodic updates
        WidgetUpdateReceiver.cancelPeriodicUpdates(context)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        val dataStoreRepository = DataStoreRepository.getInstance(context.applicationContext)

        val appContext = context.applicationContext
        val hiltEntryPoint =
            EntryPointAccessors.fromApplication(appContext, WidgetEntryPoint::class.java)
        val currentTime = hiltEntryPoint.getCurrentTime()
        val userProfileCalenderData = hiltEntryPoint.getUserProfileCalenderUseCase()

        widgetScope.launch {
            var userName = "Loading..."
            try {
                userName = dataStoreRepository.readPrimaryUsername() ?: DEFAULT_USERNAME
                for (appWidgetId in appWidgetIds) {
                    val loadingViews =
                        RemoteViews(context.packageName, R.layout.segmented_progress_small)
                    loadingViews.setTextViewText(R.id.username, userName)
                    appWidgetManager.updateAppWidget(appWidgetId, loadingViews)

                    currentTime().collectLatest { currentTimeData ->
                        when (currentTimeData) {
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                userProfileCalenderData(userName).collectLatest { profileData ->
                                    when (profileData) {
                                        is Resource.Loading -> {}
                                        is Resource.Success -> {
                                            val currentTime =
                                                currentTimeData.data?.data?.currentTimestamp
                                            val profileCalenderData = profileData.data
                                            val rawActivityData =
                                                profileCalenderData?.submissionCalendar
                                            val dayModels =
                                                rawActivityData?.let { parseCalendarData(it) }
                                                    ?.map { (timestamp, contributions) ->
                                                    val instant =
                                                        Instant.ofEpochSecond(timestamp.toLong())
                                                    val localDate =
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                                            LocalDate.ofInstant(
                                                                instant,
                                                                ZoneOffset.UTC
                                                            )
                                                        } else {
                                                            instant.atZone(ZoneOffset.UTC)
                                                                .toLocalDate()
                                                        }

                                                    DayModel(
                                                        day = localDate.dayOfMonth,
                                                        month = localDate.month.getDisplayName(
                                                            TextStyle.SHORT,
                                                            Locale.getDefault()
                                                        ),
                                                        year = localDate.year,
                                                        contributions = contributions,
                                                        monthPosition = localDate.monthValue - 1,
                                                        dayName = localDate.dayOfWeek.getDisplayName(
                                                            TextStyle.SHORT,
                                                            Locale.getDefault()
                                                        )
                                                    )
                                                }?.sortedWith(
                                                    compareBy(
                                                        { it.year },
                                                        { it.monthPosition },
                                                        { it.day })
                                                )
                                            if (dayModels != null) {
                                                val activityData = ActivityData(dayModels)
                                                updateAppWidget(
                                                    context,
                                                    appWidgetManager,
                                                    appWidgetId,
                                                    activityData = activityData,
                                                    currentTimestamp = currentTime
                                                        ?: (System.currentTimeMillis() / 1000.0)
                                                )
                                            }

                                        }

                                        is Resource.Error -> {}
                                    }
                                }
                            }

                            is Resource.Error -> {}

                        }

                    }
                }
            } catch (e: Exception) {

            }
        }

    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {

        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        val dataStoreRepository = DataStoreRepository.getInstance(context.applicationContext)

        val appContext = context.applicationContext
        val hiltEntryPoint =
            EntryPointAccessors.fromApplication(appContext, WidgetEntryPoint::class.java)
        val currentTime = hiltEntryPoint.getCurrentTime()
        val userProfileCalenderData = hiltEntryPoint.getUserProfileCalenderUseCase()

        widgetScope.launch {
            var userName = "Loading..."
            try {
                userName = dataStoreRepository.readPrimaryUsername() ?: DEFAULT_USERNAME
                val loadingViews =
                    RemoteViews(context.packageName, R.layout.segmented_progress_small)
                loadingViews.setTextViewText(R.id.username, userName)
                appWidgetManager.updateAppWidget(appWidgetId, loadingViews)

                currentTime().collectLatest { currentTimeData ->
                    when (currentTimeData) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            userProfileCalenderData(userName).collectLatest { profileData ->
                                when (profileData) {
                                    is Resource.Loading -> {}
                                    is Resource.Success -> {
                                        val currentTime =
                                            currentTimeData.data?.data?.currentTimestamp
                                        val profileCalenderData = profileData.data
                                        val rawActivityData =
                                            profileCalenderData?.submissionCalendar
                                        val dayModels =
                                            rawActivityData?.let { parseCalendarData(it) }
                                                ?.map { (timestamp, contributions) ->
                                                val instant =
                                                    Instant.ofEpochSecond(timestamp.toLong())
                                                val localDate =
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                                        LocalDate.ofInstant(
                                                            instant,
                                                            ZoneOffset.UTC
                                                        )
                                                    } else {
                                                        instant.atZone(ZoneOffset.UTC)
                                                            .toLocalDate()
                                                    }

                                                DayModel(
                                                    day = localDate.dayOfMonth,
                                                    month = localDate.month.getDisplayName(
                                                        TextStyle.SHORT,
                                                        Locale.getDefault()
                                                    ),
                                                    year = localDate.year,
                                                    contributions = contributions,
                                                    monthPosition = localDate.monthValue - 1,
                                                    dayName = localDate.dayOfWeek.getDisplayName(
                                                        TextStyle.SHORT,
                                                        Locale.getDefault()
                                                    )
                                                )
                                            }?.sortedWith(
                                                compareBy(
                                                    { it.year },
                                                    { it.monthPosition },
                                                    { it.day })
                                            )
                                        if (dayModels != null) {
                                            val activityData = ActivityData(dayModels)
                                            updateAppWidget(
                                                context,
                                                appWidgetManager,
                                                appWidgetId,
                                                activityData = activityData,
                                                currentTimestamp = currentTime
                                                    ?: (System.currentTimeMillis() / 1000.0)
                                            )
                                        }

                                    }

                                    is Resource.Error -> {}
                                }
                            }
                        }

                        is Resource.Error -> {}


                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            activityData: ActivityData,
            currentTimestamp: Double,
        ) {
            val options = appWidgetManager.getAppWidgetOptions(appWidgetId)

            val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
            val maxWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)
            val maxHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)

            val views = RemoteViews(context.packageName, R.layout.dimension_widget_layout)

            val scale = 2.0f
            val density = context.resources.displayMetrics.density
            val maxBitmapSize = 1500
            val bitmapWidth = minOf((minWidth * scale * density).toInt(), maxBitmapSize)
            val bitmapHeight = minOf((minHeight * scale * density).toInt(), maxBitmapSize)

            val bitmap = createBitmap(width = bitmapWidth, height = bitmapHeight)
            val canvas = Canvas(bitmap)

            // Color definitions
            val heatmap1Color = context.getColor(R.color.heatmap1)
            val heatmap2Color = context.getColor(R.color.heatmap2)
            val heatmap3Color = context.getColor(R.color.heatmap3)
            val heatmap4Color = context.getColor(R.color.heatmap4)
            val grayColor = androidx.compose.ui.graphics.Color.Gray.copy(alpha = 0.2f).toArgb()

            // Calculate responsive cell size based on widget height
            val availableHeight = bitmapHeight - 60 * density // Reserve space for text
            val cellSize = (availableHeight / 9).toFloat() // Reduced from 8 to 9 for smaller cells
            val cellSpacing = cellSize * 0.15f // Reduced spacing from 0.2f to 0.15f
            val cornerRadius = cellSize * 0.25f // Slightly reduced corner radius

            // Calculate how many months can fit horizontally with optimized width
            val monthWidth =
                5.5f * (cellSize + cellSpacing) // Reduced from 6 to 5.5 for tighter packing
            val availableWidth = bitmapWidth - 30 * density // Reduced padding from 40 to 30
            val maxMonths = maxOf(1, (availableWidth / monthWidth).toInt())

            val currentDate = run {
                val instant = Instant.ofEpochSecond(currentTimestamp.toLong())
                instant.atZone(ZoneOffset.UTC).toLocalDate()
            }

            // Create paint for text with smaller size for compact layout
            val textPaint = Paint().apply {
                color = context.getColor(android.R.color.white)
                textSize = cellSize * 0.6f // Reduced from 0.8f to 0.6f for smaller text
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
            }

            // Calculate starting position with reduced padding
            val startX = 15 * density // Reduced from 20 to 15
            val startY = 35 * density // Reduced from 40 to 35

            // Calculate actual month width needed based on data
            var actualMaxWeeks = 0
            for (monthOffset in 0 until maxMonths) {
                val targetDate = currentDate.minusMonths(monthOffset.toLong())
                val firstDayOfMonth = LocalDate.of(targetDate.year, targetDate.month, 1)
                val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
                val daysInMonth = targetDate.lengthOfMonth()
                val maxDayToShow = if (monthOffset == 0) currentDate.dayOfMonth else daysInMonth
                val weeksNeeded = ((firstDayOfWeek + maxDayToShow - 1) / 7) + 1
                actualMaxWeeks = maxOf(actualMaxWeeks, weeksNeeded)
            }

            // Calculate layout dimensions
            val singleMonthWidth = actualMaxWeeks * (cellSize + cellSpacing)
            val monthSpacing = cellSpacing * 2f // Space between months
            val totalWidthForAllMonths =
                maxMonths * singleMonthWidth + (maxMonths - 1) * monthSpacing

            // Adjust number of months if they don't fit
            val finalMaxMonths = if (totalWidthForAllMonths <= availableWidth) {
                maxMonths
            } else {
                // Calculate how many months can actually fit
                var fittingMonths = 1
                while (fittingMonths < maxMonths) {
                    val totalWidth =
                        fittingMonths * singleMonthWidth + (fittingMonths - 1) * monthSpacing
                    if (totalWidth + singleMonthWidth + monthSpacing > availableWidth) break
                    fittingMonths++
                }
                fittingMonths
            }

            // Center the entire heatmap
            val totalUsedWidth =
                finalMaxMonths * singleMonthWidth + (finalMaxMonths - 1) * monthSpacing
            val remainingSpace = availableWidth - totalUsedWidth
            val adjustedStartX = startX + (remainingSpace / 2)

            // Draw months (current month first, then previous months)
            for (monthOffset in 0 until finalMaxMonths) {
                val targetDate = currentDate.minusMonths(monthOffset.toLong())

                // Calculate position from right to left for proper visual order
                val monthIndex = finalMaxMonths - 1 - monthOffset
                val xPosition = adjustedStartX + monthIndex * (singleMonthWidth + monthSpacing)

                // Draw month label
                val monthText =
                    targetDate.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                canvas.drawText(
                    monthText,
                    xPosition + singleMonthWidth / 2,
                    startY - 10 * density,
                    textPaint
                )

                // Get month data
                val monthData = activityData.dayModels.filter { dayModel ->
                    val localDate =
                        LocalDate.of(dayModel.year, dayModel.monthPosition + 1, dayModel.day)
                    localDate.month == targetDate.month && localDate.year == targetDate.year
                }
                val dayToActivityMap = monthData.associateBy { it.day }

                // Calculate month layout
                val firstDayOfMonth = LocalDate.of(targetDate.year, targetDate.month, 1)
                val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
                val daysInMonth = targetDate.lengthOfMonth()
                val maxDayToShow = if (monthOffset == 0) currentDate.dayOfMonth else daysInMonth

                // Draw cells for this month
                for (day in 1..maxDayToShow) {
                    val dayModel = dayToActivityMap[day]
                    val color = when {
                        dayModel == null -> grayColor
                        dayModel.contributions > 10 -> heatmap1Color
                        dayModel.contributions > 5 -> heatmap2Color
                        dayModel.contributions > 2 -> heatmap3Color
                        else -> heatmap4Color
                    }

                    val dayOfWeek = (firstDayOfWeek + day - 1) % 7
                    val weekNumber = (firstDayOfWeek + day - 1) / 7

                    val cellX = xPosition + weekNumber * (cellSize + cellSpacing)
                    val cellY = startY + dayOfWeek * (cellSize + cellSpacing)

                    val rect = RectF(
                        cellX,
                        cellY,
                        cellX + cellSize,
                        cellY + cellSize
                    )

                    val paint = Paint().apply {
                        this.color = color
                        style = Paint.Style.FILL
                        isAntiAlias = true
                    }
                    canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint)
                }

                // Break if we've processed all months that can fit
                if (monthOffset + 1 >= finalMaxMonths) break
            }

            // Set the bitmap to the ImageView
            views.setImageViewBitmap(R.id.widget_drawing, bitmap)

            // Format dimension text based on widget size
            val dimensionText = when {
                minWidth == maxWidth && minHeight == maxHeight -> {
                    "${minWidth}dp × ${minHeight}dp"
                }

                minWidth < 200 && minHeight < 100 -> {
                    "${minWidth}×${minHeight}dp"
                }

                else -> {
                    "Size: ${minWidth}dp × ${minHeight}dp"
                }
            }
            views.setTextViewText(R.id.username, dimensionText)

            // Adjust text size based on widget size
            val textSize = when {
                minWidth < 150 -> 8f
                minWidth < 250 -> 10f
                else -> 12f
            }
            views.setTextViewTextSize(
                R.id.username,
                TypedValue.COMPLEX_UNIT_SP,
                textSize
            )

            // Add click functionality to open the app
            val intent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun drawBlueSquareBox(
            canvas: Canvas,
            canvasWidth: Int,
            canvasHeight: Int,
            widgetWidth: Int,
            widgetHeight: Int
        ) {
            // Calculate rectangle dimensions: width = 70% of height
            val rectangleHeight = minOf(widgetWidth, widgetHeight).toFloat()
            val rectangleWidth = rectangleHeight * 0.7f

            // Scale the rectangle to fit within the canvas
            val scaleFactor = minOf(
                canvasWidth.toFloat() / rectangleWidth,
                canvasHeight.toFloat() / rectangleHeight
            )
            val scaledWidth = rectangleWidth * scaleFactor
            val scaledHeight = rectangleHeight * scaleFactor

            // Create paint for the rectangles
            val paint = Paint().apply {
                this.color = Color.WHITE
                style = Paint.Style.FILL
                isAntiAlias = true
            }

            // Check space availability for multiple rectangles
            val totalWidthForFourRectangles = scaledWidth * 4
            val totalWidthForThreeRectangles = scaledWidth * 3
            val totalWidthForTwoRectangles = scaledWidth * 2
            val canShowFourRectangles = canvasWidth >= totalWidthForFourRectangles
            val canShowThreeRectangles = canvasWidth >= totalWidthForThreeRectangles
            val canShowTwoRectangles = canvasWidth >= totalWidthForTwoRectangles

            when {
                canShowFourRectangles -> {
                    // Draw four rectangles side by side
                    val spacing = (canvasWidth - totalWidthForFourRectangles) / 5f
                    val topPosition = (canvasHeight - scaledHeight) / 2f

                    // First rectangle
                    val firstLeft = spacing
                    val firstRect = RectF(
                        firstLeft,
                        topPosition,
                        firstLeft + scaledWidth,
                        topPosition + scaledHeight
                    )
                    canvas.drawRect(firstRect, paint)

                    // Second rectangle
                    val secondLeft = firstLeft + scaledWidth + spacing
                    val secondRect = RectF(
                        secondLeft,
                        topPosition,
                        secondLeft + scaledWidth,
                        topPosition + scaledHeight
                    )
                    canvas.drawRect(secondRect, paint)

                    // Third rectangle
                    val thirdLeft = secondLeft + scaledWidth + spacing
                    val thirdRect = RectF(
                        thirdLeft,
                        topPosition,
                        thirdLeft + scaledWidth,
                        topPosition + scaledHeight
                    )
                    canvas.drawRect(thirdRect, paint)

                    // Fourth rectangle
                    val fourthLeft = thirdLeft + scaledWidth + spacing
                    val fourthRect = RectF(
                        fourthLeft,
                        topPosition,
                        fourthLeft + scaledWidth,
                        topPosition + scaledHeight
                    )
                    canvas.drawRect(fourthRect, paint)
                }

                canShowThreeRectangles -> {
                    // Draw three rectangles side by side
                    val spacing = (canvasWidth - totalWidthForThreeRectangles) / 4f
                    val topPosition = (canvasHeight - scaledHeight) / 2f

                    // First rectangle (left)
                    val firstRectLeft = spacing
                    val firstRectTop = topPosition
                    val firstRectRight = firstRectLeft + scaledWidth
                    val firstRectBottom = firstRectTop + scaledHeight
                    val firstRect =
                        RectF(firstRectLeft, firstRectTop, firstRectRight, firstRectBottom)
                    canvas.drawRect(firstRect, paint)

                    // Second rectangle (middle)
                    val secondRectLeft = firstRectRight + spacing
                    val secondRectTop = topPosition
                    val secondRectRight = secondRectLeft + scaledWidth
                    val secondRectBottom = secondRectTop + scaledHeight
                    val secondRect =
                        RectF(secondRectLeft, secondRectTop, secondRectRight, secondRectBottom)
                    canvas.drawRect(secondRect, paint)

                    // Third rectangle (right)
                    val thirdRectLeft = secondRectRight + spacing
                    val thirdRectTop = topPosition
                    val thirdRectRight = thirdRectLeft + scaledWidth
                    val thirdRectBottom = thirdRectTop + scaledHeight
                    val thirdRect =
                        RectF(thirdRectLeft, thirdRectTop, thirdRectRight, thirdRectBottom)
                    canvas.drawRect(thirdRect, paint)
                }

                canShowTwoRectangles -> {
                    // Draw two rectangles side by side
                    val spacing = (canvasWidth - totalWidthForTwoRectangles) / 3f
                    val topPosition = (canvasHeight - scaledHeight) / 2f

                    // First rectangle (left)
                    val firstRectLeft = spacing
                    val firstRectTop = topPosition
                    val firstRectRight = firstRectLeft + scaledWidth
                    val firstRectBottom = firstRectTop + scaledHeight
                    val firstRect =
                        RectF(firstRectLeft, firstRectTop, firstRectRight, firstRectBottom)
                    canvas.drawRect(firstRect, paint)

                    // Second rectangle (right)
                    val secondRectLeft = firstRectRight + spacing
                    val secondRectTop = topPosition
                    val secondRectRight = secondRectLeft + scaledWidth
                    val secondRectBottom = secondRectTop + scaledHeight
                    val secondRect =
                        RectF(secondRectLeft, secondRectTop, secondRectRight, secondRectBottom)
                    canvas.drawRect(secondRect, paint)
                }

                else -> {
                    // Draw single rectangle centered
                    val left = (canvasWidth - scaledWidth) / 2f
                    val top = (canvasHeight - scaledHeight) / 2f
                    val right = left + scaledWidth
                    val bottom = top + scaledHeight
                    val rect = RectF(left, top, right, bottom)
                    canvas.drawRect(rect, paint)
                }
            }
        }
    }
}
