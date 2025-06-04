package com.devrachit.ken.widget.streakWidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.RemoteViews
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
            views.setTextViewText(R.id.dimension_text, dimensionText)

            // Adjust text size based on widget size
            val textSize = when {
                minWidth < 150 -> 10f
                minWidth < 250 -> 12f
                else -> 14f
            }
            views.setTextViewTextSize(
                R.id.dimension_text,
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
    }
}