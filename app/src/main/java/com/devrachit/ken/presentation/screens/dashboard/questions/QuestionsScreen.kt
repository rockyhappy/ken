package com.devrachit.ken.presentation.screens.dashboard.questions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter14Lh18Fw400
import com.devrachit.ken.ui.theme.TextStyleInter24Lh36Fw700
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.bg_neutral))
            .padding(24.sdp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_questions_outlined),
            contentDescription = "Coming Soon",
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier
                .size(80.sdp)
                .padding(bottom = 24.sdp)
        )
        
        Text(
            text = "Coming Soon",
            style = TextStyleInter24Lh36Fw700(),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.sdp)
        )
        
        Text(
            text = "Questions feature is under development.\nStay tuned for updates!",
            style = TextStyleInter14Lh18Fw400(),
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}
