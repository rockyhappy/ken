package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import coil.compose.SubcomposeAsyncImage
import com.devrachit.ken.R
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionImageSection(imageUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.sdp)
            .clip(RoundedCornerShape(12.sdp))
            .background(colorResource(R.color.card_elevated).copy(alpha = 0.3f))
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "Question diagram",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.sdp)),
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.sdp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = colorResource(R.color.white).copy(alpha = 0.6f),
                        modifier = Modifier.padding(16.sdp)
                    )
                }
            }
        )
    }
}
