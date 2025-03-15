package com.devrachit.ken.presentation.screens.auth.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.res.colorResource
import com.devrachit.ken.R

@Composable
fun OnboardingScreenPortrait() {
    Scaffold (
        modifier=Modifier
            .systemBarsPadding()
            .fillMaxSize()
        ,
        containerColor = colorResource(R.color.bg_neutral)
    ){paddingValues->
        Text("Screen 1", modifier=Modifier.padding(paddingValues))
    }
}
