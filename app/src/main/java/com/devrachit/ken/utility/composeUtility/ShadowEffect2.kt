package com.devrachit.ken.utility.composeUtility

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Creates a softer, more subtle shadow effect - ideal for cards and elevated surfaces.
 * This version provides a gentler shadow compared to shadowEffect().
 * 
 * @param shadowRadius The spread radius of the shadow. Default is 24dp
 * @param shadowColor The base color of the shadow. Default is Black
 * @param alpha The alpha/opacity of the shadow. Default is 0.25f for softer shadows
 * @param cornerRadius The corner radius of the shadow. Default is 36dp
 */
fun Modifier.shadowEffect2(
    shadowRadius: Dp = 24.dp,
    shadowColor: Color = Color.Black,
    alpha: Float = 0.25f,
    cornerRadius: Dp = 36.dp
): Modifier = this.drawBehind {
    val shadowRadiusPx = shadowRadius.toPx()
    val cornerRadiusPx = cornerRadius.toPx()
    
    // Layer 1: Soft center shadow
    drawRoundRect(
        brush = Brush.radialGradient(
            colors = listOf(
                shadowColor.copy(alpha = alpha * 0.6f),
                shadowColor.copy(alpha = alpha * 0.3f),
                Color.Transparent
            ),
            center = Offset(size.width / 2, size.height / 2),
            radius = (size.width.coerceAtLeast(size.height)) * 0.75f
        ),
        topLeft = Offset(-shadowRadiusPx, -shadowRadiusPx),
        size = Size(size.width + shadowRadiusPx * 2, size.height + shadowRadiusPx * 2),
        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
    )
    
    // Layer 2: Gentle bottom shadow for depth
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                shadowColor.copy(alpha = alpha * 0.4f),
                shadowColor.copy(alpha = alpha * 0.1f),
                Color.Transparent
            ),
            startY = size.height * 0.6f,
            endY = size.height + shadowRadiusPx * 1.5f
        ),
        topLeft = Offset(-shadowRadiusPx * 0.3f, size.height * 0.8f),
        size = Size(size.width + shadowRadiusPx * 0.6f, shadowRadiusPx * 1.5f),
        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
    )
    
    // Layer 3: Outer ambient shadow
    drawRoundRect(
        brush = Brush.radialGradient(
            colors = listOf(
                shadowColor.copy(alpha = alpha * 0.15f),
                Color.Transparent
            ),
            center = Offset(size.width / 2, size.height / 2),
            radius = (size.width.coerceAtLeast(size.height)) * 1.1f
        ),
        topLeft = Offset(-shadowRadiusPx * 1.5f, -shadowRadiusPx * 1.5f),
        size = Size(size.width + shadowRadiusPx * 3, size.height + shadowRadiusPx * 3),
        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
    )
}