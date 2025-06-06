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
import android.os.Bundle
import android.util.TypedValue
import android.widget.RemoteViews
import androidx.core.graphics.createBitmap
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.ActivityContent.MainActivity

class DimensionWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        // This is called when the widget is resized
        updateAppWidget(context, appWidgetManager, appWidgetId)
    }

    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // Get the current widget options (dimensions)
            val options = appWidgetManager.getAppWidgetOptions(appWidgetId)
            val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
            val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
            val maxWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH)
            val maxHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT)

            // Create RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.dimension_widget_layout)

            // Use fallback dimensions if widget dimensions are 0 or too small
            val bitmapWidth = if (minWidth > 0) minWidth else 250
            val bitmapHeight = if (minHeight > 0) minHeight else 100
            val widgetWidth = if (minWidth > 0) minWidth else 250
            val widgetHeight = if (minHeight > 0) minHeight else 100

            // Create bitmap with rectangles
            val bitmap = createBitmap(width = bitmapWidth, height = bitmapHeight)
            val canvas = Canvas(bitmap)

            // Draw rectangles
            drawBlueSquareBox(
                canvas = canvas,
                canvasWidth = bitmapWidth,
                canvasHeight = bitmapHeight,
                widgetWidth = widgetWidth,
                widgetHeight = widgetHeight
            )

            // Set the bitmap to an ImageView (assuming there's an ImageView in the layout)
            // You might need to add an ImageView to your layout if it doesn't exist
            views.setImageViewBitmap(R.id.widget_drawing, bitmap)

            // Format the dimension text to show current size more clearly
            val dimensionText = when {
                minWidth == maxWidth && minHeight == maxHeight -> {
                    // Widget is at a fixed size
                    "${minWidth}dp × ${minHeight}dp"
                }

                minWidth < 200 && minHeight < 100 -> {
                    // Small widget - show compact format
                    "${minWidth}×${minHeight}dp"
                }

                else -> {
                    // Larger widget - show detailed format
                    "Size: ${minWidth}dp × ${minHeight}dp\nRange: ${minWidth}-${maxWidth} × ${minHeight}-${maxHeight}"
                }
            }
            views.setTextViewText(R.id.username , dimensionText)

            
            views.setTextViewText(R.id.username, dimensionText)
            
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
                    // Draw four rectangles spread equally across available space
                    val availableSpaceForSpacing = canvasWidth - totalWidthForFourRectangles
                    val spacing = availableSpaceForSpacing / 5f
                    val topPosition = (canvasHeight - scaledHeight) / 2f

                    // Calculate positions to spread rectangles equally
                    val positions = listOf(
                        spacing,
                        spacing + scaledWidth + spacing,
                        spacing + scaledWidth + spacing + scaledWidth + spacing,
                        spacing + scaledWidth + spacing + scaledWidth + spacing + scaledWidth + spacing
                    )

                    positions.forEach { left ->
                        val rect =
                            RectF(left, topPosition, left + scaledWidth, topPosition + scaledHeight)
                        canvas.drawRect(rect, paint)
                    }
                }

                canShowThreeRectangles -> {
                    // Draw three rectangles spread equally across available space
                    val availableSpaceForSpacing = canvasWidth - totalWidthForThreeRectangles
                    val spacing = availableSpaceForSpacing / 4f
                    val topPosition = (canvasHeight - scaledHeight) / 2f

                    val positions = listOf(
                        spacing,
                        spacing + scaledWidth + spacing,
                        spacing + scaledWidth + spacing + scaledWidth + spacing
                    )

                    positions.forEach { left ->
                        val rect =
                            RectF(left, topPosition, left + scaledWidth, topPosition + scaledHeight)
                        canvas.drawRect(rect, paint)
                    }
                }

                canShowTwoRectangles -> {
                    // Draw two rectangles spread equally across available space
                    val availableSpaceForSpacing = canvasWidth - totalWidthForTwoRectangles
                    val spacing = availableSpaceForSpacing / 3f
                    val topPosition = (canvasHeight - scaledHeight) / 2f

                    val positions = listOf(
                        spacing,
                        spacing + scaledWidth + spacing
                    )

                    positions.forEach { left ->
                        val rect =
                            RectF(left, topPosition, left + scaledWidth, topPosition + scaledHeight)
                        canvas.drawRect(rect, paint)
                    }
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
