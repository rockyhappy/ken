package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.utility.composeUtility.sdp

data class FabItem(
    val icon: ImageVector? = null,
    val iconResId: Int? = null,
    val label: String,
    val onClick: () -> Unit
)

@Composable
fun ExpandableFab(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    items: List<FabItem>
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        animationSpec = tween(300),
        label = "fab_rotation"
    )

    Box(modifier = modifier) {
        // Scrim/Overlay when expanded
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(animationSpec = tween(200)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onToggle
                    )
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.sdp, bottom = 16.sdp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.sdp)
        ) {
            // Expandable items
            items.forEachIndexed { index, item ->
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(200, delayMillis = index * 50)
                    ) + fadeIn(animationSpec = tween(200, delayMillis = index * 50)) +
                            scaleIn(animationSpec = tween(200, delayMillis = index * 50)),
                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(150)
                    ) + fadeOut(animationSpec = tween(150)) +
                            scaleOut(animationSpec = tween(150))
                ) {
                    FabMenuItem(
                        item = item,
                        onClick = {
                            item.onClick()
                            onToggle()
                        }
                    )
                }
            }

            // Main FAB
            FloatingActionButton(
                onClick = onToggle,
                containerColor = colorResource(R.color.blue_normal_500),
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = if (isExpanded) "Close menu" else "Open menu",
                    modifier = Modifier.rotate(rotation)
                )
            }
        }
    }
}

@Composable
private fun FabMenuItem(
    item: FabItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(24.sdp))
            .background(colorResource(R.color.card_elevated))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.sdp, vertical = 12.sdp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.sdp)
    ) {
        Text(
            text = item.label,
            style = TextStyleInter12Lh16Fw400(),
            color = Color.White,
            fontWeight = FontWeight.Medium
        )

        Box(
            modifier = Modifier
                .size(36.sdp)
                .clip(CircleShape)
                .background(colorResource(R.color.blue_normal_500)),
            contentAlignment = Alignment.Center
        ) {
            when {
                item.icon != null -> {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = Color.White,
                        modifier = Modifier.size(20.sdp)
                    )
                }
                item.iconResId != null -> {
                    Icon(
                        painter = painterResource(item.iconResId),
                        contentDescription = item.label,
                        tint = Color.White,
                        modifier = Modifier.size(20.sdp)
                    )
                }
            }
        }
    }
}
