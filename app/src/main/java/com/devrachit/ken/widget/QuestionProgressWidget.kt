package com.devrachit.ken.widget

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.devrachit.ken.R
import com.devrachit.ken.utility.composeUtility.sdp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class QuestionProgressWidget : GlanceAppWidget() {

    companion object {
        const val PROGRESS_IMAGE_NAME = "leetcode_progress"
        const val WIDTH_PX = 300  // Smaller size might help
        const val HEIGHT_PX = 300
        private const val TAG = "QuestionProgressWidget"
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        try {
            // Generate bitmap synchronously to ensure it's ready
            withContext(Dispatchers.IO) {
                renderProgressIndicatorSynchronously(context)
            }
            
            provideContent {
                Content()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error providing glance content", e)
            provideContent {
                Column(
                    modifier = GlanceModifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalAlignment = Alignment.Vertical.CenterVertically
                ) {
                    Text("Error: ${e.message}", )
                }
            }
        }
    }

    @Composable
    fun Content() {
        val context = LocalContext.current
        val bitmap = loadProgressBitmap(context)
        
        Log.d(TAG, "Content composable - bitmap is ${if (bitmap == null) "NULL" else "available"}")
        
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
//                .background(ColorProvider(android.graphics.Color.parseColor("#242424")))
                .padding(16.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "LeetCode Progress",
                style = TextStyle(
//                    color = ColorProvider(android.graphics.Color.WHITE),
                    fontWeight = FontWeight.Bold
                ),
                modifier = GlanceModifier.padding(bottom = 8.sdp)
            )
            
            // Progress Image
            if (bitmap != null) {
                Log.d(TAG, "Displaying bitmap with dimensions ${bitmap.width}x${bitmap.height}")
                Image(
                    provider = ImageProvider(bitmap),
                    contentDescription = "LeetCode progress chart",
                    modifier = GlanceModifier.size(160.dp)
                )
            } else {
                Log.e(TAG, "Bitmap is null in Content composable")
                Text(
                    text = "Loading progress data... (Bitmap not found)",
                    style = TextStyle(
//                        color = ColorProvider(android.graphics.Color.WHITE)
                    )
                )
            }
            
            // Summary text
            Row(
                modifier = GlanceModifier.fillMaxWidth().padding(top = 8.sdp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "1300/3200 Problems Solved",
                    style = TextStyle(
//                        color = ColorProvider(android.graphics.Color.WHITE)
                    )
                )
            }
        }
    }
    
    // Function to load the bitmap with error checking
    private fun loadProgressBitmap(context: Context): Bitmap? {
        val file = File(context.cacheDir, "$PROGRESS_IMAGE_NAME.png")
        Log.d(TAG, "Loading bitmap from: ${file.absolutePath}")
        Log.d(TAG, "File exists: ${file.exists()}, Size: ${if (file.exists()) file.length() else 0}")
        
        return if (file.exists() && file.length() > 0) {
            try {
                WidgetUtils.loadBitmapFromCache(context, PROGRESS_IMAGE_NAME)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading bitmap", e)
                null
            }
        } else {
            Log.e(TAG, "Bitmap file doesn't exist or is empty")
            null
        }
    }
    
    // Make this synchronous to ensure bitmap is created before widget displays
    private fun renderProgressIndicatorSynchronously(context: Context) {
        Log.d(TAG, "Starting to render progress indicator synchronously")
        
        try {
            // Delete any existing cached image first
            val file = File(context.cacheDir, "$PROGRESS_IMAGE_NAME.png")
            if (file.exists()) {
                val deleted = file.delete()
                Log.d(TAG, "Deleted existing file: $deleted")
            }
            
            // Create a simple test bitmap if the normal rendering doesn't work
            val bitmap = Bitmap.createBitmap(WIDTH_PX, HEIGHT_PX, Bitmap.Config.ARGB_8888)
            bitmap.eraseColor(android.graphics.Color.BLUE)
            
            // Save this test bitmap
            WidgetUtils.saveBitmapToFile(context, bitmap, PROGRESS_IMAGE_NAME)
            
            Log.d(TAG, "Created and saved test bitmap, file exists: ${file.exists()}, size: ${file.length()}")
            
            /* Uncomment this after testing with the solid color bitmap
            // Render a real progress indicator bitmap
            WidgetUtils.renderComposableToBitmap(
                context = context,
                widthPx = WIDTH_PX,
                heightPx = HEIGHT_PX,
                solved = 1300,
                attempting = 25,
                total = 3200,
                easyTotalCount = 950,
                easySolvedCount = 800,
                mediumTotalCount = 1500,
                mediumSolvedCount = 400,
                hardTotalCount = 750,
                hardSolvedCount = 100
            ) { bitmap ->
                WidgetUtils.saveBitmapToFile(context, bitmap, PROGRESS_IMAGE_NAME)
                Log.d(TAG, "Saved rendered bitmap to file")
            }
            */
        } catch (e: Exception) {
            Log.e(TAG, "Error rendering progress indicator", e)
        }
    }
}