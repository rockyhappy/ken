package com.devrachit.ken.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.appwidget.AppWidgetManager
import android.util.Log

class WidgetUpdateReceiver : BroadcastReceiver() {
    
    companion object {
        const val ACTION_PERIODIC_UPDATE = "com.devrachit.ken.ACTION_UPDATE_WIDGET"
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
            val intervalMillis = 60 * 60 * 1000L // 1 hour
            
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
        if (intent.action == ACTION_PERIODIC_UPDATE) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(
                android.content.ComponentName(context, SimpleTextWidgetProvider::class.java)
            )
            
            if (appWidgetIds.isNotEmpty()) {
                // Update the widgets
                val updateIntent = Intent(context, SimpleTextWidgetProvider::class.java).apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                }
                context.sendBroadcast(updateIntent)
                Log.d(TAG, "Sent update broadcast to ${appWidgetIds.size} widgets")
            }
        }
    }
}