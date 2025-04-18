package com.devrachit.ken.widget

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.devrachit.ken.R

class SimpleTextWidgetProvider : AppWidgetProvider() {
    
    private val TAG = "SimpleTextWidgetProvider"

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "onUpdate called for ${appWidgetIds.size} widgets")
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    @SuppressLint("RemoteViewLayout")
    private fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        try {
            val views = RemoteViews(context.packageName, R.layout.simple_text_widget)
            
            // Create a larger bitmap for better quality
            val width = 600
            val height = 600
            val bitmap = createBitmap(width = width, height = height)
            val canvas = Canvas(bitmap)
            
            // Hard-coded values for demonstration (you can make these dynamic later)
            val solved = 1300
            val attempting = 25
            val total = 3200
            val easyTotalCount = 950
            val easySolvedCount = 800
            val mediumTotalCount = 1500
            val mediumSolvedCount = 400
            val hardTotalCount = 750
            val hardSolvedCount = 100
            
            // Draw the segmented arc similar to SegmentedProgressIndicator2
            drawSegmentedProgressArc(
                context = context,
                canvas = canvas, 
                width = width,
                height = height,
                solved = solved,
                attempting = attempting,
                total = total,
                easyTotalCount = easyTotalCount,
                easySolvedCount = easySolvedCount,
                mediumTotalCount = mediumTotalCount,
                mediumSolvedCount = mediumSolvedCount,
                hardTotalCount = hardTotalCount,
                hardSolvedCount = hardSolvedCount
            )

            // Add text in the center
            drawCenteredText(
                context = context,
                canvas = canvas,
                width = width,
                height = height,
                solved = solved,
                total = total
            )

            // Set the bitmap to the ImageView
            views.setImageViewBitmap(R.id.widget_drawing, bitmap)
            
            // Set a descriptive text
//            views.setTextViewText(R.id.widget_text, "LeetCode Progress")

            appWidgetManager.updateAppWidget(appWidgetId, views)
            Log.d(TAG, "Widget updated successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error updating widget", e)
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
        
        // Get color resources
        val easyBaseColor = ContextCompat.getColor(context, R.color.easy_base_blue)
        val easyFilledColor = ContextCompat.getColor(context, R.color.easy_filled_blue)
        val mediumBaseColor = ContextCompat.getColor(context, R.color.medium_base_yellow)
        val mediumFilledColor = ContextCompat.getColor(context, R.color.medium_filled_yellow)
        val hardBaseColor = ContextCompat.getColor(context, R.color.hard_base_red)
        val hardFilledColor = ContextCompat.getColor(context, R.color.hard_filled_red)
        
        // Define the arc parameters
        val startAngle = 135f
        val gapAngle = 10f
        val totalSweepAngle = 250f
        
        // Calculate segment angles based on total problems
        val easyBaseSweepAngle = (easyTotalCount.toFloat() / total) * totalSweepAngle
        val mediumBaseSweepAngle = (mediumTotalCount.toFloat() / total) * totalSweepAngle
        val hardBaseSweepAngle = (hardTotalCount.toFloat() / total) * totalSweepAngle
        
        // Calculate filled angles based on solved problems
        val easyFilledSweepAngle = (easySolvedCount.toFloat() / total) * totalSweepAngle
        val mediumFilledSweepAngle = (mediumSolvedCount.toFloat() / total) * totalSweepAngle
        val hardFilledSweepAngle = (hardSolvedCount.toFloat() / total) * totalSweepAngle
        
        // Create the paint object for all arcs
        val paint = Paint().apply {
            style = Paint.Style.STROKE
            isAntiAlias = true
            strokeWidth = stroke_Width
            strokeCap = Paint.Cap.ROUND
        }
        
        // Create the arc bounds with padding for the stroke width
        val padding = stroke_Width / 2
        val oval = RectF(padding, padding, width - padding, height - padding)
        
        // Easy segment - Base arc
        paint.color = easyBaseColor
        canvas.drawArc(oval, startAngle, easyBaseSweepAngle, false, paint)
        
        // Easy segment - Filled arc
        paint.color = easyFilledColor
        canvas.drawArc(oval, startAngle, easyFilledSweepAngle, false, paint)
        
        // Medium segment - Base arc
        paint.color = mediumBaseColor
        canvas.drawArc(oval, startAngle + easyBaseSweepAngle + gapAngle, 
                     mediumBaseSweepAngle, false, paint)
        
        // Medium segment - Filled arc
        paint.color = mediumFilledColor
        canvas.drawArc(oval, startAngle + easyBaseSweepAngle + gapAngle, 
                     mediumFilledSweepAngle, false, paint)
                     
        // Hard segment - Base arc
        paint.color = hardBaseColor
        canvas.drawArc(oval, startAngle + easyBaseSweepAngle + gapAngle + 
                    mediumBaseSweepAngle + gapAngle, hardBaseSweepAngle, false, paint)
        
        // Hard segment - Filled arc
        paint.color = hardFilledColor
        canvas.drawArc(oval, startAngle + easyBaseSweepAngle + gapAngle + 
                     mediumBaseSweepAngle + gapAngle, hardFilledSweepAngle, false, paint)
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
            textSize = 140f
            isFakeBoldText = true
            isAntiAlias = true
        }
        
        val smallTextPaint = Paint().apply {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = 80f
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
        val totalY = height / 2f + 100f
        canvas.drawText(totalText, totalX, totalY, smallTextPaint)
        
        // Draw "Solved" text
        val solvedLabelX = width / 2f
        val solvedLabelY = height / 2f + 180f
        val solvedLabelPaint = Paint().apply {
            color = ContextCompat.getColor(context, R.color.solved_color)
            textAlign = Paint.Align.CENTER
            textSize = 55f
            isAntiAlias = true
        }
        canvas.drawText("✔ Solved", solvedLabelX, solvedLabelY, solvedLabelPaint)
    }
}