package com.devrachit.ken.utility.composeUtility

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


fun Modifier.shadowEffect2(): Modifier = this.drawBehind {
    val shadowColor = Color.Black.copy(alpha = 0.1f)
    val horizontalShadowSize = 30.dp.toPx()
    val verticalShadowSize = 30.dp.toPx()

    drawRoundRect(
        brush = Brush.radialGradient(
            colors = listOf(shadowColor, Color.Transparent),
            center = Offset(size.width / 2, size.height / 2),
            radius = (size.width.coerceAtLeast(size.height)) * 0.8f
        ),
        size = Size(size.width + 2 * horizontalShadowSize, size.height + 2 * verticalShadowSize),
        topLeft = Offset(-horizontalShadowSize, -verticalShadowSize),
        cornerRadius = CornerRadius(36.dp.toPx(), 36.dp.toPx())
    )


}