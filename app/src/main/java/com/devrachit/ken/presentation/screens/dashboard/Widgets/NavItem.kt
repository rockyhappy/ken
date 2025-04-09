package com.devrachit.ken.presentation.screens.dashboard.Widgets

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw600
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw700
import com.devrachit.ken.ui.theme.TextStyleInter14Lh16Fw400
import com.devrachit.ken.utility.composeUtility.sdp

/**
 * A navigation item composable that displays an icon and text
 * 
 * @param label The text to display below the icon
 * @param outlinedIconRes Resource ID for the outlined (unselected) version of the icon
 * @param filledIconRes Resource ID for the filled (selected) version of the icon
 * @param isSelected Whether this item is currently selected
 * @param onClick Callback when the item is clicked
 * @param modifier Optional modifier for the composable
 */
@Composable
fun NavItem(
    label: String,
    @DrawableRes outlinedIconRes: Int,
    @DrawableRes filledIconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animate scale when selected
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "nav_item_scale"
    )
    
    // Animate color when selected
    val textColor by animateColorAsState(
        targetValue = if (isSelected) 
            colorResource(id = R.color.white) 
        else 
            colorResource(id = R.color.white).copy(alpha = 0.7f),
        label = "nav_item_text_color"
    )

    Column(
        modifier = modifier
            .clickable(interactionSource = null, indication = null, onClick = onClick)
            .padding(horizontal = 12.sdp, vertical = 4.sdp)
            .scale(scale),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display either filled or outlined icon based on selection state
        Icon(
            painter = painterResource(
                id = if (isSelected) filledIconRes else outlinedIconRes
            ),
            contentDescription = label,
            tint = textColor,
            modifier = Modifier
                .padding(bottom = 2.sdp)
                .size(24.sdp)
        )
        
        Text(
            text = label,
            style = TextStyleInter12Lh16Fw700(),
            color = textColor
        )
    }
}