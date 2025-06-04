package com.devrachit.ken.presentation.screens.dashboard.compare.components

import android.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.compare.QuestionGraphData
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw600
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

@Composable
fun QuestionProgressGraphs(
    modifier: Modifier = Modifier,
    easyData: List<QuestionGraphData>,
    mediumData: List<QuestionGraphData>,
    hardData: List<QuestionGraphData>
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.sdp)
    ) {
        if (easyData.isNotEmpty()) {
            QuestionGraphCard(
                title = "Easy Questions Progress",
                data = easyData,
                color = Color.parseColor("#1CBABA") ,
                totalCount = easyData.firstOrNull()?.totalCount ?: 0
            )
        }
        
        if (mediumData.isNotEmpty()) {
            QuestionGraphCard(
                title = "Medium Questions Progress",
                data = mediumData,
                color = Color.parseColor("#FFB700"), // Orange for medium
                totalCount = mediumData.firstOrNull()?.totalCount ?: 0
            )
        }
        
        if (hardData.isNotEmpty()) {
            QuestionGraphCard(
                title = "Hard Questions Progress",
                data = hardData,
                color = Color.parseColor("#F63737"), // Red for hard
                totalCount = hardData.firstOrNull()?.totalCount ?: 0
            )
        }
    }
}

@Composable
private fun QuestionGraphCard(
    title: String,
    data: List<QuestionGraphData>,
    color: Int,
    totalCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.sdp)
            .border(
                border = BorderStroke(
                    width = 2.sdp,
                    color = colorResource(R.color.card_elevated)
                ),
                shape = RoundedCornerShape(36.sdp),
            )
        ,
        shape = RoundedCornerShape(36.sdp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.bg_neutral)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.sdp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.sdp)
        ) {
            Text(
                text = title,
                style = TextStyleInter16Lh24Fw700(),
                color = colorResource(R.color.white),
                modifier = Modifier.padding(top = 8.sdp,start = 8.sdp,bottom = 8.sdp)
            )
            
            Text(
                text = "Total: $totalCount questions",
                style = TextStyleInter14Lh20Fw600(),
                color = colorResource(R.color.white),
                modifier = Modifier.padding(start = 8.sdp,bottom = 16.sdp)
            )
            
            if (data.size <= 3) {
                // Use Bar Chart for fewer users
                BarChartComposable(
                    data = data,
                    color = color,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Use Line Chart for more users
                LineChartComposable(
                    data = data,
                    color = color,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun BarChartComposable(
    data: List<QuestionGraphData>,
    color: Int,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(false)
                setBackgroundColor(Color.TRANSPARENT)
                
                // Configure X-axis
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    textColor = Color.WHITE
                    axisLineColor = Color.WHITE
                    textSize = 9f
                    labelRotationAngle = -45f
                    setLabelCount(data.size, false)
                    valueFormatter = IndexAxisValueFormatter(data.map { 
                        // Truncate long names to prevent overlap
                        if (it.displayName.length > 8) {
                            it.displayName.take(8) + "..."
                        } else {
                            it.displayName
                        }
                    })
                }
                
                // Configure Y-axis
                axisLeft.apply {
                    textColor = Color.WHITE
                    axisLineColor = Color.WHITE
                    granularity = 1f
                    axisMinimum = 0f
                    textSize = 9f
                }
                axisRight.isEnabled = false
                
                // Configure legend
                legend.apply {
                    isEnabled = false
                }
            }
        },
        update = { chart ->
            val entries = data.mapIndexed { index, item ->
                BarEntry(index.toFloat(), item.solvedCount.toFloat())
            }
            
            val dataSet = BarDataSet(entries, "Solved Questions").apply {
                this.color = color
                valueTextColor = Color.WHITE
                valueTextSize = 9f
                // Add subtle styling for better appearance
                barBorderWidth = 1f
                barBorderColor = Color.WHITE
                setDrawValues(true)
            }
            
            val barData = BarData(listOf<IBarDataSet>(dataSet)).apply {
                // Slightly wider bars for better visual presence
                barWidth = 0.7f
            }
            chart.data = barData
            
            chart.setExtraOffsets(10f, 10f, 10f, 10f)
            chart.invalidate()
        }
    )
}

@Composable
private fun LineChartComposable(
    data: List<QuestionGraphData>,
    color: Int,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(false)
                setBackgroundColor(Color.TRANSPARENT)
                
                // Configure X-axis
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    textColor = Color.WHITE
                    axisLineColor = Color.WHITE
                    textSize = 9f
                    labelRotationAngle = -45f
                    setLabelCount(minOf(data.size, 10), false)
                    valueFormatter = IndexAxisValueFormatter(data.map { 
                        // Truncate long names to prevent overlap
                        if (it.displayName.length > 8) {
                            it.displayName.take(8) + "..."
                        } else {
                            it.displayName
                        }
                    })
                }
                
                // Configure Y-axis
                axisLeft.apply {
                    textColor = Color.WHITE
                    axisLineColor = Color.WHITE
                    granularity = 1f
                    axisMinimum = 0f
                    textSize = 9f
                }
                axisRight.isEnabled = false
                
                // Configure legend
                legend.apply {
                    isEnabled = false
                }
            }
        },
        update = { chart ->
            val entries = data.mapIndexed { index, item ->
                Entry(index.toFloat(), item.solvedCount.toFloat())
            }
            
            val dataSet = LineDataSet(entries, "Solved Questions").apply {
                this.color = color
                setCircleColor(color)
                lineWidth = 2f
                circleRadius = 4f
                valueTextColor = Color.WHITE
                valueTextSize = 9f
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            
            val lineData = LineData(listOf<ILineDataSet>(dataSet))
            chart.data = lineData
            chart.invalidate()
        }
    )
}
