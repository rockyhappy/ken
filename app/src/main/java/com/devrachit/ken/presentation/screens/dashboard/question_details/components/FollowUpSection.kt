package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun FollowUpSection(content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.sdp)
            .clip(RoundedCornerShape(16.sdp))
            .border(
                border = BorderStroke(
                    width = (1.5).dp,
                    color = colorResource(R.color.blue_normal_600).copy(alpha = 0.4f)
                ),
                shape = RoundedCornerShape(16.sdp)
            )
            .padding(14.sdp)
    ) {
        // Follow-up title with icon
        Row {
            Text(
                text = "🔍 ",
                style = TextStyleInter14Lh20Fw400()
            )
            Text(
                text = "Follow Up",
                color = colorResource(R.color.blue_normal_400),
                style = TextStyleInter12Lh16Fw400(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 2.sdp)
            )
        }
        
        // Follow-up content
        Text(
            text = content.removePrefix("Follow up:").trim(),
            color = colorResource(R.color.white).copy(alpha = 0.88f),
            style = TextStyleInter12Lh16Fw400(),
            modifier = Modifier.padding(top = 8.sdp)
        )
    }
}
