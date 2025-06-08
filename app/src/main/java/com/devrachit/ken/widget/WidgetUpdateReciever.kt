package com.devrachit.ken.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.util.Log
import com.devrachit.ken.widget.SegmentedProgressLarge.SegmentedProgressWidgetLargeProvider
import com.devrachit.ken.widget.SegmentedProgressSmall.SegmentedProgressWidgetSmallProvider
import com.devrachit.ken.widget.streakWidget.DimensionWidgetProvider

class WidgetUpdateReceiver : BroadcastReceiver() {
    
    companion object {
        const val ACTION_PERIODIC_UPDATE = "com.devrachit.ken.ACTION_UPDATE_WIDGET"
        const val ACTION_ON_OPEN_APP_UPDATE = "com.devrachit.ken.ACTION_UPDATE_WIDGET_ON_OPEN"
        private const val TAG = "WidgetUpdateReceiver"
        
        // Schedule a periodic update
        fun schedulePeriodicUpdates(context: Context) {
            val intent = Intent(context, WidgetUpdateReceiver::class.java).apply {
                action = ACTION_PERIODIC_UPDATE
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            // Get AlarmManager and schedule updates every hour
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intervalMillis = 15 * 60 * 1000L // 1 hour
            
            // Start the alarm
            alarmManager.setRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis() + intervalMillis,
                intervalMillis,
                pendingIntent
            )
            
            Log.d(TAG, "Periodic updates scheduled for LeetCode widget")
        }
        
        // Cancel periodic updates
        fun cancelPeriodicUpdates(context: Context) {
            val intent = Intent(context, WidgetUpdateReceiver::class.java).apply {
                action = ACTION_PERIODIC_UPDATE
            }
            
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            
            // Cancel the alarm
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            
            Log.d(TAG, "Periodic updates canceled for LeetCode widget")
        }
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_PERIODIC_UPDATE || intent.action == ACTION_ON_OPEN_APP_UPDATE) {
            // Update the SimpleTextWidget
            val simpleTextIntent = Intent(context, SimpleTextWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                val ids = AppWidgetManager.getInstance(context)
                    .getAppWidgetIds(ComponentName(context, SimpleTextWidgetProvider::class.java))
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            }
            context.sendBroadcast(simpleTextIntent)
            
            // Update the SegmentedProgressWidgetLarge
            val segmentedProgressLargeIntent =
                Intent(context, SegmentedProgressWidgetLargeProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                val ids = AppWidgetManager.getInstance(context)
                    .getAppWidgetIds(ComponentName(context, SegmentedProgressWidgetLargeProvider::class.java))
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            }
            context.sendBroadcast(segmentedProgressLargeIntent)

            // Update the SegmentedProgressWidgetSmall
            val segmentedProgressSmallIntent =
                Intent(context, SegmentedProgressWidgetSmallProvider::class.java).apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    val ids = AppWidgetManager.getInstance(context)
                        .getAppWidgetIds(
                            ComponentName(
                                context,
                                SegmentedProgressWidgetSmallProvider::class.java
                            )
                        )
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                }
            context.sendBroadcast(segmentedProgressSmallIntent)

            // Update the DimensionWidget
            val dimensionWidgetIntent = Intent(context, DimensionWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                val ids = AppWidgetManager.getInstance(context)
                    .getAppWidgetIds(ComponentName(context, DimensionWidgetProvider::class.java))
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
            }
            context.sendBroadcast(dimensionWidgetIntent)
        }
        
    }
}
