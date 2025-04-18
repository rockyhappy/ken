package com.devrachit.ken.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.view.View
import androidx.compose.ui.platform.ComposeView
import com.devrachit.ken.utility.composeUtility.SegmentedProgressIndicator2
import androidx.core.graphics.createBitmap
import java.io.File
import java.io.FileOutputStream

/**
 * Utility functions for widget rendering
 */
object WidgetUtils {

    /**
     * Renders a Composable to a bitmap for use in widgets
     */
    fun renderComposableToBitmap(
        context: Context,
        widthPx: Int,
        heightPx: Int,
        solved: Int,
        attempting: Int,
        total: Int,
        easyTotalCount: Int,
        easySolvedCount: Int,
        mediumTotalCount: Int,
        mediumSolvedCount: Int,
        hardTotalCount: Int,
        hardSolvedCount: Int,
        onBitmapReady: (Bitmap) -> Unit
    ) {
        val composeView = ComposeView(context).apply {
            setContent {
                SegmentedProgressIndicator2(
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
            }
        }

        composeView.measure(
            View.MeasureSpec.makeMeasureSpec(widthPx, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(heightPx, View.MeasureSpec.EXACTLY)
        )
        composeView.layout(0, 0, widthPx, heightPx)

        val bitmap = createBitmap(widthPx, heightPx)
        val canvas = Canvas(bitmap)
        composeView.draw(canvas)

        onBitmapReady(bitmap)
    }
    fun saveBitmapToFile(context: Context, bitmap: Bitmap, fileName: String): File {
        val file = File(context.cacheDir, "$fileName.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return file
    }
    fun loadBitmapFromCache(context: Context, fileName: String): Bitmap? {
        val file = File(context.cacheDir, "$fileName.png")
        return if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
    }

}