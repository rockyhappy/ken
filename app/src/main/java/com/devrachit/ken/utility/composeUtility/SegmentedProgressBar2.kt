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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.devrachit.ken.R

@Composable
fun SegmentedProgressIndicator2(
    solved: Int,
    attempting: Int,
    total: Int,
    modifier: Modifier = Modifier,
    strokeWidth: Float = if (LocalConfiguration.current.screenWidthDp > 600) 18f else 24f,
    easyTotalCount: Int = 0,
    easySolvedCount: Int = 0,
    mediumTotalCount: Int = 0,
    mediumSolvedCount: Int = 0,
    hardTotalCount: Int = 0,
    hardSolvedCount: Int = 0
) {

    val solvedColor = Color(0xFF4CAF50)
    val attemptingColor = Color(0xFF00BCD4)
    val remainingColor = Color(0xFFA53A3A)
    val baseRingColor = Color(0xFF6D5D1C)

    val easy_base_blue = colorResource(id = R.color.easy_base_blue)
    val easy_filled_blue = colorResource(id = R.color.easy_filled_blue)
    val medium_base_yellow = colorResource(id = R.color.medium_base_yellow)
    val medium_filled_yellow = colorResource(id = R.color.medium_filled_yellow)
    val hard_base_red = colorResource(id = R.color.hard_base_red)
    val hard_filled_red = colorResource(id = R.color.hard_filled_red)

    val startAngle = 135f
    val endAngle = 45f
    val gapAngle = 10f
    val easyBaseSweepAngle = (easyTotalCount/total.toFloat())  * 250f
    val mediumBaseSweepAngle = (mediumTotalCount/total.toFloat())  * 250f
    val hardBaseSweepAngle = (hardTotalCount/total.toFloat())  * 250f
    val easyFilledSweepAngle = (easySolvedCount/total.toFloat())  * 250f
    val mediumFilledSweepAngle = (mediumSolvedCount/total.toFloat())  * 250f
    val hardFilledSweepAngle = (hardSolvedCount/total.toFloat())  * 250f


    Box(
        modifier = modifier.size(130.sdp),
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
                color = easy_base_blue,
                startAngle = startAngle,
                sweepAngle = easyBaseSweepAngle,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                topLeft = topLeft,
                size = arcSize
            )
            drawArc(
                color = easy_filled_blue,
                startAngle = startAngle,
                sweepAngle = easyFilledSweepAngle,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                topLeft = topLeft,
                size = arcSize
            )
            drawArc(
                color = medium_base_yellow,
                startAngle = startAngle + easyBaseSweepAngle + gapAngle,
                sweepAngle = mediumBaseSweepAngle,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                topLeft = topLeft,
                size = arcSize
            )
            drawArc(
                color = medium_filled_yellow,
                startAngle = startAngle + easyBaseSweepAngle + gapAngle,
                sweepAngle = mediumFilledSweepAngle,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                topLeft = topLeft,
                size = arcSize
            )
            drawArc(
                color = hard_base_red,
                startAngle = startAngle+easyBaseSweepAngle+gapAngle+mediumBaseSweepAngle+gapAngle,
                sweepAngle = hardBaseSweepAngle,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                topLeft = topLeft,
                size = arcSize
            )
            drawArc(
                color = hard_filled_red,
                startAngle = startAngle+easyBaseSweepAngle+gapAngle+mediumBaseSweepAngle+gapAngle,
                sweepAngle = hardFilledSweepAngle,
                useCenter = false,
                style = Stroke(strokeWidth, cap = StrokeCap.Round),
                topLeft = topLeft,
                size = arcSize
            )


        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.sdp, end=8.sdp, top=16.sdp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("$solved")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    ) {
                        append("/$total")
                    }
                }
            )

            Spacer(modifier = Modifier.height(4.sdp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "✓",
                    color = solvedColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.sdp))
                Text(
                    text = "Solved",
                    color = solvedColor,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(4.sdp))

            Text(
                text = "$attempting Attempting",
                color = attemptingColor,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun SegmentedProgressIndicatorPreview2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.sdp),
    ) {
        SegmentedProgressIndicator2(
            solved = 2260,
            attempting = 11,
            total = 3521,
            easyTotalCount = 873,
            easySolvedCount = 90,
            mediumTotalCount = 1826,
            mediumSolvedCount = 162,
            hardTotalCount = 822,
            hardSolvedCount = 8,
            strokeWidth = 18f
        )
        SegmentedProgressIndicator2(
            solved = 1300,
            attempting = 25,
            total = 3200,
            easyTotalCount = 950,
            easySolvedCount = 800,
            mediumTotalCount = 1500,
            mediumSolvedCount = 400,
            hardTotalCount = 750,
            hardSolvedCount = 100,
            strokeWidth = 18f
        )
    }
}