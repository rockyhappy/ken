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
 * Creates a multi-layered dark shadow effect for elevated UI elements.
 * Creates depth by drawing multiple shadow layers with varying opacity and blur.
 * 
 * @param shadowRadius The spread radius of the shadow. Default is 36dp
 * @param shadowColor The base color of the shadow. Default is Black
 * @param alpha The alpha/opacity of the shadow. Default is 0.5f for darker shadows
 */
fun Modifier.shadowEffect(
    shadowRadius: Dp = 36.dp,
    shadowColor: Color = Color.Black,
    alpha: Float = 0.3f
): Modifier = this.drawBehind {
    val cornerRadiusPx = 46.dp.toPx()
    val shadowRadiusPx = shadowRadius.toPx()
    
    // Layer 1: Darkest shadow (closest to element) - Bottom shadow
    drawRoundRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                shadowColor.copy(alpha = alpha * 0.4f),
                shadowColor.copy(alpha = alpha * 0.15f),
                Color.Transparent
            ),
            startY = size.height * 0.5f,
            endY = size.height + shadowRadiusPx * 1.8f
        ),
        topLeft = Offset(-shadowRadiusPx * 0.5f, size.height * 0.7f),
        size = Size(size.width + shadowRadiusPx, shadowRadiusPx * 2f),
        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
    )
    
    // Layer 2: Medium shadow - All around glow
    drawRoundRect(
        brush = Brush.radialGradient(
            colors = listOf(
                shadowColor.copy(alpha = alpha * 0.3f),
                shadowColor.copy(alpha = alpha * 0.1f),
                Color.Transparent
            ),
            center = Offset(size.width / 2, size.height / 2),
            radius = (size.width.coerceAtLeast(size.height)) * 0.85f
        ),
        topLeft = Offset(-shadowRadiusPx, -shadowRadiusPx),
        size = Size(size.width + shadowRadiusPx * 2, size.height + shadowRadiusPx * 2),
        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
    )
    
    // Layer 3: Subtle outer glow for depth
    drawRoundRect(
        brush = Brush.radialGradient(
            colors = listOf(
                shadowColor.copy(alpha = alpha * 0.15f),
                shadowColor.copy(alpha = alpha * 0.05f),
                Color.Transparent
            ),
            center = Offset(size.width / 2, size.height / 2),
            radius = (size.width.coerceAtLeast(size.height)) * 1.2f
        ),
        topLeft = Offset(-shadowRadiusPx * 1.5f, -shadowRadiusPx * 1.5f),
        size = Size(size.width + shadowRadiusPx * 3, size.height + shadowRadiusPx * 3),
        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
    )
    
    // Layer 4: Left edge shadow for dimension
    drawRoundRect(
        brush = Brush.horizontalGradient(
            colors = listOf(
                shadowColor.copy(alpha = alpha * 0.2f),
                Color.Transparent
            ),
            startX = -shadowRadiusPx * 0.8f,
            endX = shadowRadiusPx * 0.4f
        ),
        topLeft = Offset(-shadowRadiusPx * 0.8f, -shadowRadiusPx * 0.3f),
        size = Size(shadowRadiusPx * 1.5f, size.height + shadowRadiusPx * 0.6f),
        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
    )
    
    // Layer 5: Right edge shadow for dimension
    drawRoundRect(
        brush = Brush.horizontalGradient(
            colors = listOf(
                Color.Transparent,
                shadowColor.copy(alpha = alpha * 0.2f)
            ),
            startX = size.width - shadowRadiusPx * 0.4f,
            endX = size.width + shadowRadiusPx * 0.8f
        ),
        topLeft = Offset(size.width - shadowRadiusPx * 0.7f, -shadowRadiusPx * 0.3f),
        size = Size(shadowRadiusPx * 1.5f, size.height + shadowRadiusPx * 0.6f),
        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
    )
}