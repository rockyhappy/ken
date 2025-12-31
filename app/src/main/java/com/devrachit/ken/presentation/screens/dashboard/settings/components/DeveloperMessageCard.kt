package com.devrachit.ken.presentation.screens.dashboard.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw600
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun DeveloperMessageCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.sdp))
            .border(
                border = BorderStroke(
                    width = 2.sdp,
                    color = Color(0xFF4A90E2).copy(alpha = 0.3f) // Subtle blue border
                ),
                shape = RoundedCornerShape(16.sdp)
            )
            .background(
                color = Color(0xFF2C3E50).copy(alpha = 0.4f) // Slightly different background
            )
            .padding(18.sdp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_friends_filled),
                contentDescription = "Developer Message",
                tint = Color(0xFFE74C3C).copy(alpha = 0.9f),
                modifier = Modifier.size(22.sdp)
            )

            Spacer(modifier = Modifier.width(10.sdp))

            Text(
                text = "A Message from the Developer",
                color = Color(0xFF3498DB),
                style = TextStyleInter14Lh20Fw600(),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(14.sdp))

        Text(
            text = "Hey there, amazing user! 👋",
            color = colorResource(R.color.white),
            style = TextStyleInter14Lh20Fw600(),
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(10.sdp))

        Text(
            text = "If you're enjoying Ken, please share it with your friends and help more developers discover this app! In return, I give you my word to keep Ken:",
            color = colorResource(R.color.white).copy(alpha = 0.92f),
            style = TextStyleInter12Lh16Fw400(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(14.sdp))

        val promises = listOf(
            "✨ Completely ad-free forever",
            "🚫 Zero distractions or interruptions",
            "💯 100% clean and free to use",
            "🙅‍♂️ No annoying rating pop-ups",
            "🎯 No premium tiers or paywalls"
        )

        promises.forEach { promise ->
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(vertical = 3.sdp)
            ) {
                Text(
                    text = promise,
                    color = colorResource(R.color.white).copy(alpha = 0.88f),
                    style = TextStyleInter12Lh16Fw400()
                )
            }
        }

        Spacer(modifier = Modifier.height(14.sdp))

        Text(
            text = "Your support and word-of-mouth means the world to me! Thank you for being part of the Ken community. 💙",
            color = Color(0xFF27AE60).copy(alpha = 0.9f), // Nice green color
            style = TextStyleInter12Lh16Fw400(),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Start
        )
    }
}
