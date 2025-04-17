package com.devrachit.ken.utility.composeUtility

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SegmentedProgressIndicator(
    solved: Int,
    attempting: Int,
    total: Int,
    modifier: Modifier = Modifier,
    strokeWidth: Float = 12f
) {
    val solvedColor = Color(0xFF4CAF50)
    val attemptingColor = Color(0xFF00BCD4)
    val remainingColor = Color(0xFFA53A3A) 
    val baseRingColor = Color(0xFF6D5D1C)  
    
    val remaining = total - solved - attempting

    val gapAngle = 20f
    val availableAngle = 360f - (3 * gapAngle)
    
    val solvedAngle = availableAngle * solved / total
    val attemptingAngle = availableAngle * attempting / total
    val remainingAngle = availableAngle * remaining / total

    Box(
        modifier = modifier.size(180.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val diameterOffset = strokeWidth / 2
            val arcSize = Size(
                width = size.width - strokeWidth,
                height = size.height - strokeWidth
            )
            val topLeft = Offset(diameterOffset, diameterOffset)
            
            drawArc(
                color = baseRingColor,
                startAngle = 0f,
                sweepAngle = 60f,
                useCenter = false,
                style = Stroke(strokeWidth / 2, cap = StrokeCap.Round),
                topLeft = topLeft,
                size = arcSize
            )

            var startAngle = -90f

            drawArc(
                color = Color(0xFFFFEB3B),  
                startAngle = startAngle,
                sweepAngle = solvedAngle,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                topLeft = topLeft,
                size = arcSize
            )
            startAngle += solvedAngle + gapAngle

            drawArc(
                color = attemptingColor,
                startAngle = startAngle,
                sweepAngle = attemptingAngle,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                topLeft = topLeft,
                size = arcSize
            )
            startAngle += attemptingAngle + gapAngle

            drawArc(
                color = remainingColor,
                startAngle = startAngle,
                sweepAngle = remainingAngle,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                topLeft = topLeft,
                size = arcSize
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(8.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        color = Color.White,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold
                    )) {
                        append("$solved")
                    }
                    withStyle(style = SpanStyle(
                        color = Color.White,
                        fontSize = 18.sp
                    )) {
                        append("/$total")
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "✓",
                    color = solvedColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Solved",
                    color = solvedColor,
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "$attempting Attempting",
                color = attemptingColor,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun SegmentedProgressIndicatorPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        SegmentedProgressIndicator(
            solved = 260,
            attempting = 11,
            total = 3520
        )
    }
}