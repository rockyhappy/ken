package com.devrachit.ken.widget

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MyAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = QuestionProgressWidget()
    
    private val TAG = "MyAppWidgetReceiver"
    private val widgetScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d(TAG, "Widget enabled - updating")
        
        // Create a simple blue bitmap first for testing
        widgetScope.launch {
            try {
                Log.d(TAG, "Updating all widgets")
                QuestionProgressWidget().updateAll(context)
            } catch (e: Exception) {
                Log.e(TAG, "Error updating widgets", e)
            }
        }
    }
    
    override fun onUpdate(
        context: Context,
        appWidgetManager: android.appwidget.AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d(TAG, "Widget update triggered for ${appWidgetIds.size} widgets")
        
        widgetScope.launch {
            try {
                QuestionProgressWidget().updateAll(context)
                Log.d(TAG, "Widget update completed")
            } catch (e: Exception) {
                Log.e(TAG, "Error during widget update", e)
            }
        }
    }
    
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d(TAG, "Widget disabled")
    }
}