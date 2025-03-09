package com.devrachit.ken.widget

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import com.devrachit.ken.api.LeetCodeApiService
import com.devrachit.ken.data.LeetCodeRepository
import com.devrachit.ken.ui.components.LeetCodeHeatmap
import kotlinx.coroutines.launch

class LeetCodeWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = LeetCodeWidget()
}

class LeetCodeWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repository = LeetCodeRepository(LeetCodeApiService.create())

        provideContent {
            LeetCodeWidgetContent(repository)
        }
    }
}

@Composable
private fun LeetCodeWidgetContent(repository: LeetCodeRepository) {
    val scope = rememberCoroutineScope()
    var contributionsData = mutableMapOf<String, Int>()

    // Replace "your_leetcode_username" with the username you want to track
    val username = "rockyhappy"

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                contributionsData.putAll(repository.getUserHeatmap(username))
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    // Display the heatmap
    LeetCodeHeatmap(
        contributions = contributionsData,
        modifier = GlanceModifier.fillMaxSize().padding(8.dp) as Modifier
    )
}