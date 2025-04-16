package com.devrachit.ken.presentation.screens.dashboard.Widgets

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw700
import com.devrachit.ken.ui.theme.TextStyleInter20Lh24Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun DrawerNavItem(
    label: String,
    @DrawableRes outlinedIconRes: Int,
    @DrawableRes filledIconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    drawerProgress : Float=0f
) {
    val alpha = lerp(0f, 1f, drawerProgress)
    val xOffset = lerp(-300f, 0f, drawerProgress)

    // Animate scale when selected
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "nav_item_scale"
    )

    val bgColor by animateColorAsState(
        targetValue = if(isSelected)
        colorResource(id = R.color.bg_neutral)
        else colorResource(id = R.color.bg_neutral).copy(alpha = 0f),
        animationSpec = tween(300),

    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected)
            colorResource(id = R.color.white)
        else
            colorResource(id = R.color.white).copy(alpha = 0.7f),
        label = "nav_item_text_color"
    )

    Row(
        modifier = modifier
            .clickable(interactionSource = null, indication = null, onClick = onClick)
            .padding(horizontal = 12.sdp, vertical = 4.sdp)
//            .scale(scale)
            .offset(x= (-30).sdp)
            .fillMaxWidth()
            .offset(x= xOffset.dp)
            .alpha(alpha)
            .clip(RoundedCornerShape(topEnd = 30.sdp, bottomEnd = 30.sdp))
            .background(bgColor)
            .padding(top = 12.sdp, bottom = 12.sdp, start=30.sdp)
        ,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
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
            style = TextStyleInter20Lh24Fw700(),
            color = textColor,
            modifier = Modifier.padding(start=10.sdp)
        )
    }
}