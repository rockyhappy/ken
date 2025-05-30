package com.devrachit.ken.presentation.screens.dashboard.questions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.devrachit.ken.R
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.bg_neutral)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_questions_outlined),
            contentDescription = "Questions",
            tint = Color.White.copy(alpha = 0.6f),
            modifier = Modifier.size(80.sdp)
        )
        
        Spacer(modifier = Modifier.height(24.sdp))
        
        Text(
            text = "Questions Feature",
            style = TextStyle(
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.sdp))
        
        Text(
            text = "Coming Soon",
            style = TextStyle(
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.sdp))
        
        Text(
            text = "We're working hard to bring you\nan amazing questions experience!",
            style = TextStyle(
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 14.sp
            ),
            textAlign = TextAlign.Center
        )
    }
}
