package com.devrachit.ken.utility.composeUtility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.devrachit.ken.ui.theme.TextStyleInter18Lh24Fw700
import com.devrachit.ken.ui.theme.TextStyleInter24Lh36Fw700
import com.valentinilk.shimmer.shimmer

@Composable
fun ProfilePictureShimmer(
    modifier: Modifier = Modifier,
) {
    Column {
        Box(
            modifier = Modifier
                .padding(top = 30.sdp)
                .size(100.sdp)
                .shimmer()
                .clip(RoundedCornerShape(10.sdp))
                .background(Color.Gray)
        )
        Box(
            modifier = Modifier
                .padding(top = 16.sdp)
                .width(200.sdp)
                .height(20.sdp)
                .shimmer()
                .clip(RoundedCornerShape(5.sdp))
                .background(Color.Gray),
        )
        Box(
            modifier = Modifier
                .padding(top = 16.sdp)
                .width(200.sdp)
                .height(20.sdp)
                .shimmer()
                .clip(RoundedCornerShape(5.sdp))
                .background(Color.Gray)
                .alpha(0.5f)
        )
    }
}

@Preview
@Composable
fun ProfilePictureShimmerPreview() {
    ProfilePictureShimmer(
        modifier = Modifier
            .width(100.sdp)
            .height(100.dp)
            .padding(16.sdp)
            .background(Color.Gray)
            .clip(RoundedCornerShape(50))
    )
}