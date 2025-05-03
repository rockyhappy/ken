package com.devrachit.ken.utility.composeUtility

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import com.devrachit.ken.R
import com.valentinilk.shimmer.shimmer

@Composable
fun HomeScreenShimmer(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(top = 20.sdp, start = 20.sdp, end = 20.sdp)
            .fillMaxWidth()
            .height(270.sdp)
            .clip(RoundedCornerShape(10.sdp))
            .border(
                border = BorderStroke(
                    width = 2.sdp,
                    color = colorResource(R.color.card_elevated)
                ),
                shape = RoundedCornerShape(36.sdp),
            )
            .padding(top = 20.sdp, start = 20.sdp, end = 20.sdp),
    ) {
        Box(
            modifier = Modifier
                .padding(top = 16.sdp, start = 16.sdp, end = 16.sdp)
                .fillMaxWidth()
                .height(30.sdp)
                .shimmer()
                .clip(RoundedCornerShape(16.sdp))
                .background(Color.Gray),
        )
        Box(
            modifier = Modifier
                .padding(top = 16.sdp, start = 16.sdp, end = 16.sdp)
                .fillMaxWidth()
                .height(40.sdp)
                .shimmer()
                .clip(RoundedCornerShape(16.sdp))
                .background(Color.Gray),
        )
        Box(
            modifier = Modifier
                .padding(top = 16.sdp, start = 16.sdp, end = 16.sdp)
                .fillMaxWidth()
                .height(90.sdp)
                .shimmer()
                .clip(RoundedCornerShape(20.sdp))
                .background(Color.Gray),
        )

    }
}

@Preview
@Composable
fun HomeScreenShimmerPreview() {
    HomeScreenShimmer()
}
