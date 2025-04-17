package com.devrachit.ken.utility.composeUtility

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext

fun Modifier.shadowEffect(): Modifier = this.drawBehind {
    val shadowColor = Color.Black.copy(alpha = 0.1f)
    val horizontalShadowSize = 30.dp.toPx()
    val verticalShadowSize = 30.dp.toPx()
    drawRoundRect(
        brush = Brush.radialGradient(
            colors = listOf(shadowColor, Color.Transparent),
            center = Offset(size.width * 0.1f, size.height / 2),
            radius = (size.width.coerceAtLeast(size.height)) * 0.7f
        ),
        size = Size(size.width + 2 * horizontalShadowSize, size.height +2* verticalShadowSize),
        topLeft = Offset(-horizontalShadowSize, -verticalShadowSize),
        cornerRadius = CornerRadius(46.dp.toPx(), 46.dp.toPx())
    )
    

    drawRoundRect(
        brush = Brush.radialGradient(
            colors = listOf(shadowColor, Color.Transparent),
            center = Offset(size.width * 0.9f, size.height / 2),
            radius = (size.width.coerceAtLeast(size.height)) * 0.7f
        ),
        size = Size(size.width + 2 * horizontalShadowSize, size.height + 2*verticalShadowSize),
        topLeft = Offset(-horizontalShadowSize, -verticalShadowSize),
        cornerRadius = CornerRadius(46.dp.toPx(), 46.dp.toPx())
    )
}