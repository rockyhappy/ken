package com.devrachit.ken.widget.SegmentedProgressSmall



import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.devrachit.ken.R
import com.devrachit.ken.data.local.datastore.DataStoreRepository
import com.devrachit.ken.domain.models.UserQuestionStatusData
import com.devrachit.ken.domain.models.toQuestionProgressUiState
import com.devrachit.ken.domain.usecases.getUserQuestionStatus.GetUserQuestionStatusUseCase
import com.devrachit.ken.presentation.screens.dashboard.home.QuestionProgressUiState
import com.devrachit.ken.utility.NetworkUtility.Resource
import com.devrachit.ken.utility.constants.Constants.Companion.DEFAULT_USERNAME
import com.devrachit.ken.widget.WidgetEntryPoint
import com.devrachit.ken.widget.WidgetUpdateReceiver
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject



class SegmentedProgressWidgetSmallProvider : AppWidgetProvider() {

    private val TAG = "SimpleTextWidgetProvider"
    private val widgetScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "onUpdate called for ${appWidgetIds.size} widgets")

        val dataStoreRepository = DataStoreRepository.getInstance(context.applicationContext)

        val appContext = context.applicationContext
        val hiltEntryPoint =
            EntryPointAccessors.fromApplication(appContext, WidgetEntryPoint::class.java)
        val repo = hiltEntryPoint.getUserQuestionStatusUseCase()

        widgetScope.launch {
            var userName = "Loading..."
            try {
                userName = dataStoreRepository.readPrimaryUsername() ?: DEFAULT_USERNAME
                Log.d(TAG, "Username: $userName")
                for (appWidgetId in appWidgetIds) {
                    val loadingViews =
                        RemoteViews(context.packageName, R.layout.segmented_progress_small)
                    loadingViews.setTextViewText(R.id.username, userName)
                    appWidgetManager.updateAppWidget(appWidgetId, loadingViews)
                }
                repo(userName, forceRefresh = true).collectLatest { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            Log.e(TAG, "Error fetching user question status: ${resource.message}")
                        }

                        is Resource.Loading -> {
                            Log.d(TAG, "Loading user question status...")
                        }

                        is Resource.Success -> {
                            val questionProgress = resource.data?.toQuestionProgressUiState()
                            for (appWidgetId in appWidgetIds) {
                                updateWidgetWithData(
                                    context = context,
                                    appWidgetManager = appWidgetManager,
                                    appWidgetId = appWidgetId,
                                    username = userName,
                                    questionProgress = questionProgress ?: QuestionProgressUiState()
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching username", e)
            }

        }


    }

    // Add this to handle Widget creation
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d(TAG, "Widget enabled")

        // Schedule periodic updates
        WidgetUpdateReceiver.schedulePeriodicUpdates(context)
    }

    // Add this to handle Widget deletion
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(TAG, "Widget disabled")

        // Cancel periodic updates
        WidgetUpdateReceiver.cancelPeriodicUpdates(context)
    }

    @SuppressLint("RemoteViewLayout")
    private fun updateWidgetWithData(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        username: String,
        questionProgress: QuestionProgressUiState
    ) {
        try {
            val views = RemoteViews(context.packageName, R.layout.segmented_progress_small)

            // Create progress indicator bitmap
            val width = 500
            val height = 500
            val bitmap = createBitmap(width = width, height = height)
            val canvas = Canvas(bitmap)

            drawSegmentedProgressArc(
                context = context,
                canvas = canvas,
                width = width,
                height = height,
                solved = questionProgress.solved,
                attempting = questionProgress.attempting,
                total = questionProgress.total,
                easyTotalCount = questionProgress.easyTotalCount,
                easySolvedCount = questionProgress.easySolvedCount,
                mediumTotalCount = questionProgress.mediumTotalCount,
                mediumSolvedCount = questionProgress.mediumSolvedCount,
                hardTotalCount = questionProgress.hardTotalCount,
                hardSolvedCount = questionProgress.hardSolvedCount
            )

            // Draw text in the center of the circle
            drawCenteredText(
                context = context,
                canvas = canvas,
                width = width,
                height = height,
                solved = questionProgress.solved,
                total = questionProgress.total
            )

            // Set the bitmap to the ImageView
            views.setImageViewBitmap(R.id.widget_drawing, bitmap)

//            val textBitmapWidth = 350
//            val textBitmapHeight = 500
//            val bitmapText = createBitmap(width = textBitmapWidth, height = textBitmapHeight)
//            val canvasText = Canvas(bitmapText)
//            drawProgressText(
//                context = context,
//                canvas = canvasText,
//                width = textBitmapWidth,
//                height = textBitmapHeight,
//                username = username,
//                solved = questionProgress.solved,
//                attempting = questionProgress.attempting,
//                total = questionProgress.total,
//                easyTotalCount = questionProgress.easyTotalCount,
//                easySolvedCount = questionProgress.easySolvedCount,
//                mediumTotalCount = questionProgress.mediumTotalCount,
//                mediumSolvedCount = questionProgress.mediumSolvedCount,
//                hardTotalCount = questionProgress.hardTotalCount,
//                hardSolvedCount = questionProgress.hardSolvedCount
//            )
//            views.setImageViewBitmap(R.id.widget_drawing2, bitmapText)

            // Set text values in the widget layout
            views.setTextViewText(R.id.username, username)

            // Update easy stats
//            views.setTextViewText(R.id.easy_solved, questionProgress.easySolvedCount.toString())
//            views.setTextViewText(R.id.easy_total, "/${questionProgress.easyTotalCount}")

            // Update medium stats
//            views.setTextViewText(
//                R.id.medium_solved,
//                questionProgress.mediumSolvedCount.toString()
//            )
//            views.setTextViewText(R.id.medium_total, "/${questionProgress.mediumTotalCount}")

//            views.setTextViewText(R.id.hard_solved, questionProgress.hardSolvedCount.toString())
//            views.setTextViewText(R.id.hard_total, "/${questionProgress.hardTotalCount}")


            val openAppIntent =
                context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                            Intent.FLAG_ACTIVITY_SINGLE_TOP
                }

            if (openAppIntent != null) {
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    openAppIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                views.setOnClickPendingIntent(R.id.segmented_widget_large, pendingIntent)
                views.setOnClickPendingIntent(R.id.widget_drawing, pendingIntent)
//                views.setOnClickPendingIntent(R.id.widget_drawing2, pendingIntent)
            }

//            val refreshIntent = Intent(context, SegmentedProgressWidgetLargeProvider::class.java).apply {
//                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(appWidgetId))
//            }
//            val refreshPendingIntent = PendingIntent.getBroadcast(
//                context,
//                appWidgetId,
//                refreshIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )
//            views.setOnClickPendingIntent(R.id.widget_drawing, refreshPendingIntent)

            // Update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
            Log.d(TAG, "Widget updated successfully with real data")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating widget with data", e)
        }
    }

    private fun drawSegmentedProgressArc(
        context: Context,
        canvas: Canvas,
        width: Int,
        height: Int,
        solved: Int,
        attempting: Int,
        total: Int,
        easyTotalCount: Int,
        easySolvedCount: Int,
        mediumTotalCount: Int,
        mediumSolvedCount: Int,
        hardTotalCount: Int,
        hardSolvedCount: Int
    ) {
        val stroke_Width = 35f
        val easyBaseColor = ContextCompat.getColor(context, R.color.easy_base_blue)
        val easyFilledColor = ContextCompat.getColor(context, R.color.easy_filled_blue)
        val mediumBaseColor = ContextCompat.getColor(context, R.color.medium_base_yellow)
        val mediumFilledColor = ContextCompat.getColor(context, R.color.medium_filled_yellow)
        val hardBaseColor = ContextCompat.getColor(context, R.color.hard_base_red)
        val hardFilledColor = ContextCompat.getColor(context, R.color.hard_filled_red)

        val startAngle = 135f
        val gapAngle = 10f
        val totalSweepAngle = 250f

        val totalFloat = total.toFloat().takeIf { it > 0 } ?: 1f

        val easyBaseSweepAngle = (easyTotalCount.toFloat() / totalFloat) * totalSweepAngle
        val mediumBaseSweepAngle = (mediumTotalCount.toFloat() / totalFloat) * totalSweepAngle
        val hardBaseSweepAngle = (hardTotalCount.toFloat() / totalFloat) * totalSweepAngle

        val easyFilledSweepAngle = (easySolvedCount.toFloat() / totalFloat) * totalSweepAngle
        val mediumFilledSweepAngle =
            (mediumSolvedCount.toFloat() / totalFloat) * totalSweepAngle
        val hardFilledSweepAngle = (hardSolvedCount.toFloat() / totalFloat) * totalSweepAngle

        val paint = Paint().apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = stroke_Width
            strokeCap = Paint.Cap.ROUND
        }

        val padding = stroke_Width / 2
        val oval = RectF(padding, padding, width - padding, height - padding)

        paint.color = easyBaseColor
        canvas.drawArc(oval, startAngle, easyBaseSweepAngle, false, paint)

        paint.color = easyFilledColor
        canvas.drawArc(oval, startAngle, easyFilledSweepAngle, false, paint)

        paint.color = mediumBaseColor
        canvas.drawArc(
            oval, startAngle + easyBaseSweepAngle + gapAngle,
            mediumBaseSweepAngle, false, paint
        )

        paint.color = mediumFilledColor
        canvas.drawArc(
            oval, startAngle + easyBaseSweepAngle + gapAngle,
            mediumFilledSweepAngle, false, paint
        )

        paint.color = hardBaseColor
        canvas.drawArc(
            oval, startAngle + easyBaseSweepAngle + gapAngle +
                    mediumBaseSweepAngle + gapAngle, hardBaseSweepAngle, false, paint
        )

        paint.color = hardFilledColor
        canvas.drawArc(
            oval, startAngle + easyBaseSweepAngle + gapAngle +
                    mediumBaseSweepAngle + gapAngle, hardFilledSweepAngle, false, paint
        )
    }

    private fun drawCenteredText(
        context: Context,
        canvas: Canvas,
        width: Int,
        height: Int,
        solved: Int,
        total: Int
    ) {
        // Create paint for the text
        val mainTextPaint = Paint().apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 100f
            isFakeBoldText = true
            isAntiAlias = true
        }

        val smallTextPaint = Paint().apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 60f
            isAntiAlias = true
        }

        // Draw the solved count in large text
        val solvedText = solved.toString()
        val solvedX = width / 2f
        val solvedY = height / 2f
        canvas.drawText(solvedText, solvedX, solvedY, mainTextPaint)

        // Draw the total with a slash before it in smaller text
        val totalText = "/$total"
        val totalX = width / 2f
        val totalY = height / 2f + 80f  // Adjusted for proper spacing
        canvas.drawText(totalText, totalX, totalY, smallTextPaint)
    }

    private fun drawProgressText(
        context: Context,
        canvas: Canvas,
        width: Int,
        height: Int,
        solved: Int,
        attempting: Int,
        total: Int,
        username: String,
        easyTotalCount: Int,
        easySolvedCount: Int,
        mediumTotalCount: Int,
        mediumSolvedCount: Int,
        hardTotalCount: Int,
        hardSolvedCount: Int
    ) {
        val usernameTextPaint = Paint().apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = calculateTextSizeToFitWidth(username, 350f, 60f)
            isFakeBoldText = true
            isAntiAlias = true
        }
        val totalText = "$username"
        val totalX = width / 2f
        val totalY = 60f

        canvas.drawText(totalText, totalX, totalY, usernameTextPaint)

        val easyTextPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.easy_filled_blue)
            textAlign = Paint.Align.CENTER
            textSize = calculateTextSizeToFitWidth(username, 350f, 50f)
            isFakeBoldText = true
            isAntiAlias = true
        }

        canvas.drawText("Easy", totalX, 130f, easyTextPaint)
        val mediumTextPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.medium_filled_yellow)
            textAlign = Paint.Align.CENTER
            textSize = calculateTextSizeToFitWidth(username, 350f, 50f)
            isFakeBoldText = true
            isAntiAlias = true
        }

        canvas.drawText("Med.", totalX, 280f, mediumTextPaint)
        val hardTextPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.hard_filled_red)
            textAlign = Paint.Align.CENTER
            textSize = calculateTextSizeToFitWidth(username, 350f, 50f)
            isFakeBoldText = true
            isAntiAlias = true
        }

        canvas.drawText("Hard", totalX, 430f, hardTextPaint)
        val solvedTextPaint = Paint().apply {
            color =  Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = calculateTextSizeToFitWidth(username, 350f, 45f)
            isAntiAlias = true
        }
        val textX= width/2f
        val text2x = width/1.5f
        canvas.drawText(easySolvedCount.toString(), textX, 180f, solvedTextPaint)
        canvas.drawText(mediumSolvedCount.toString(), textX, 330f, solvedTextPaint)
        canvas.drawText(hardSolvedCount.toString(), textX, 480f, solvedTextPaint)
//        canvas.drawText("/$easySolvedCount", text2x, 180f, solvedTextPaint)
//        canvas.drawText("/$mediumSolvedCount", text2x, 330f, solvedTextPaint)
//        canvas.drawText("/$hardSolvedCount", text2x, 480f, solvedTextPaint)
    }
}

private fun calculateTextSizeToFitWidth(text: String, maxWidth: Float, startSize: Float): Float {
    val paint = Paint()
    var textSize = startSize
    paint.textSize = textSize

    while (paint.measureText(text) > maxWidth && textSize > 10f) {
        textSize -= 1f
        paint.textSize = textSize
    }

    return textSize
}
private fun calculateWidthSizeToFitWidth(width: Float, maxWidth: Float, startSize: Float)
{

}
private fun calculateStrokeWidthForArc(width: Int, height: Int): Float {
    val minDimension = width.coerceAtMost(height)
    return minDimension * 0.07f
}