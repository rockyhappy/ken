package com.devrachit.ken.presentation.screens.dashboard.settings.components

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw600
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun UpdateAppButton(
    onUpdateClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(300),
        label = "scale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(8.sdp))
            .background(
                color = Color.White.copy(alpha = 0.05f)
            )
            .border(
                border = BorderStroke(
                    width = 1.sdp,
                    color = Color.White.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(8.sdp)
            )
            .clickable { onUpdateClick() }
            .padding(12.sdp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(24.sdp)
                    .clip(RoundedCornerShape(6.sdp))
                    .background(Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_link),
                    contentDescription = "Update App",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.sdp)
                )
            }

            Spacer(modifier = Modifier.padding(horizontal = 8.sdp))

            Text(
                text = "Update Available",
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium,
                style = TextStyleInter14Lh20Fw600()
            )
        }

        Text(
            text = "→",
            color = Color.White.copy(alpha = 0.6f),
            style = TextStyleInter14Lh20Fw600(),
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun ShareAppButton() {
    val context = LocalContext.current
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(300),
        label = "scale"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(8.sdp))
            .background(
                color = Color.White.copy(alpha = 0.05f)
            )
            .border(
                border = BorderStroke(
                    width = 1.sdp,
                    color = Color.White.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(8.sdp)
            )
            .clickable {
                shareApp(context)
            }
            .padding(12.sdp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(24.sdp)
                    .clip(RoundedCornerShape(6.sdp))
                    .background(Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = "Share App",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(14.sdp)
                )
            }

            Spacer(modifier = Modifier.padding(horizontal = 8.sdp))

            Text(
                text = "Share Ken with Friends",
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.Medium,
                style = TextStyleInter14Lh20Fw600()
            )
        }

        Text(
            text = "→",
            color = Color.White.copy(alpha = 0.6f),
            style = TextStyleInter14Lh20Fw600(),
            fontWeight = FontWeight.Normal
        )
    }
}

private fun shareApp(context: Context) {
    try {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out Ken - LeetCode Tracker")
            putExtra(
                Intent.EXTRA_TEXT,
                """
                Hey! 👋
                
                I've been using Ken to track my LeetCode progress and it's amazing! 🚀
                
                ✨ Track coding progress
                📊 View detailed analytics  
                👥 Compare with friends
                🎯 Set and achieve goals
                
                And the best part? It's completely FREE with no ads! 
                
                Download it here: https://play.google.com/store/apps/details?id=com.devrachit.ken
                """.trimIndent()
            )
        }

        val chooserIntent = Intent.createChooser(shareIntent, "Share Ken with friends")
        context.startActivity(chooserIntent)
    } catch (_: Exception) {
        // Fallback - just share the Play Store link
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Check out Ken - LeetCode Tracker: https://play.google.com/store/apps/details?id=com.devrachit.ken")
        }
        context.startActivity(Intent.createChooser(intent, "Share Ken"))
    }
}

