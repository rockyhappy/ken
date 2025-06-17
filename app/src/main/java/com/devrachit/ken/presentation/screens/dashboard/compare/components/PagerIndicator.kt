package com.devrachit.ken.presentation.screens.dashboard.compare.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import com.devrachit.ken.R
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun PagerIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int
) {
    when {
        pageCount<=1 ->{

        }
        pageCount <= 5 -> {
            // Show all dots for small counts
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.Center
            ) {
                for (i in 0 until pageCount) {
                    val isActive = i == currentPage
                    val indicatorColor = if (isActive) colorResource(R.color.white).copy(alpha = 0.8f) else colorResource(R.color.white).copy(alpha = 0.2f)
                    val indicatorSize = if (isActive) 16.sdp else 16.sdp

                    Box(
                        modifier = Modifier
                            .height(indicatorSize)
                            .width(indicatorSize)
                            .padding(vertical = 4.sdp, horizontal = 4.sdp)
                            .clip(RoundedCornerShape(50))
                            .background(indicatorColor)
                    )
                }
            }
        }
        pageCount <= 15 -> {
            // Show limited dots with ellipsis for medium counts
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val maxVisibleDots = 5
                val halfVisible = maxVisibleDots / 2

                for (i in 0 until maxVisibleDots) {
                    val actualIndex = when {
                        currentPage < halfVisible -> i
                        currentPage >= pageCount - halfVisible -> pageCount - maxVisibleDots + i
                        else -> currentPage - halfVisible + i
                    }

                    if (actualIndex in 0 until pageCount) {
                        val isActive = actualIndex == currentPage
                        val indicatorColor = if (isActive) colorResource(R.color.white).copy(alpha = 0.8f) else colorResource(R.color.white).copy(alpha = 0.2f)
                        val indicatorSize = if (isActive) 16.sdp else 16.sdp

                        Box(
                            modifier = Modifier
                                .height(indicatorSize)
                                .width(indicatorSize)
                                .padding(vertical = 4.sdp, horizontal = 4.sdp)
                                .clip(RoundedCornerShape(50))
                                .background(indicatorColor)
                        )
                    }
                }
            }
        }
        else -> {
            // Show text indicator for large counts
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentPage + 1} of $pageCount",
                    color = colorResource(R.color.white).copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = 16.sdp, vertical = 0.sdp)
                )
            }
        }
    }
}