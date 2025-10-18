package com.devrachit.ken.presentation.screens.dashboard.settings.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.devrachit.ken.R
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun BadgeDisplayModeOption(
    displayMode: BadgeDisplayMode,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val backgroundColor by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(300),
        label = "background_alpha"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        animationSpec = tween(300),
        label = "scale"
    )
    
    val iconRotation by animateFloatAsState(
        targetValue = if (isSelected) 360f else 0f,
        animationSpec = tween(500),
        label = "icon_rotation"
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(12.sdp))
            .background(
                color = colorResource(R.color.card_elevated)
                    .copy(alpha = 0.3f + (backgroundColor * 0.4f))
            )
            .border(
                border = BorderStroke(
                    width = if (isSelected) 2.sdp else 1.sdp,
                    color = if (isSelected) {
                        colorResource(R.color.white)
                    } else {
                        colorResource(R.color.card_elevated)
                    }
                ),
                shape = RoundedCornerShape(12.sdp)
            )
            .clickable { onSelect() }
            .padding(16.sdp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(40.sdp)
                    .clip(RoundedCornerShape(8.sdp))
                    .background(
                        if (isSelected) {
                            colorResource(R.color.white).copy(alpha = 0.15f)
                        } else {
                            Color.Transparent
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(displayMode.icon),
                    contentDescription = displayMode.displayName,
                    modifier = Modifier
                        .size(24.sdp)
                        .rotate(iconRotation),
                    tint = if (isSelected) {
                        colorResource(R.color.white)
                    } else {
                        colorResource(R.color.white).copy(alpha = 0.6f)
                    }
                )
            }
            
            Column(
                modifier = Modifier.padding(start = 16.sdp)
            ) {
                Text(
                    text = displayMode.displayName,
                    color = if (isSelected) {
                        colorResource(R.color.white)
                    } else {
                        colorResource(R.color.white).copy(alpha = 0.7f)
                    },
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                
                Text(
                    text = displayMode.description,
                    color = colorResource(R.color.white).copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = 4.sdp)
                )
            }
        }
        
        AnimatedContent(
            targetState = isSelected,
            transitionSpec = {
                (scaleIn(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300))) togetherWith
                (scaleOut(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300)))
            },
            label = "selection_indicator"
        ) { selected ->
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(24.sdp)
                        .clip(RoundedCornerShape(12.sdp))
                        .background(colorResource(R.color.white)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(android.R.drawable.checkbox_on_background),
                        contentDescription = "Selected",
                        modifier = Modifier.size(16.sdp),
                        tint = colorResource(R.color.card_elevated)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(24.sdp)
                        .clip(RoundedCornerShape(12.sdp))
                        .border(
                            border = BorderStroke(
                                width = 2.sdp,
                                color = colorResource(R.color.white).copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(12.sdp)
                        )
                )
            }
        }
    }
}
