package com.devrachit.ken.presentation.screens.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter14Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter18Lh24Fw700
import com.devrachit.ken.utility.composeUtility.CompletePreviews
import com.devrachit.ken.utility.composeUtility.ProgressIndicator
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun LoginScreenPortrait() {
    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .imePadding()
            .fillMaxSize(),
        containerColor = colorResource(R.color.bg_neutral)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(colorResource(R.color.bg_neutral)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        )
        {
            Image(
                painterResource(id = R.drawable.logo2),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(top = 30.sdp, bottom = 20.sdp)
                    .sizeIn(
                        minWidth = 0.sdp,
                        maxWidth = 100.sdp,
                        minHeight = 0.sdp,
                        maxHeight = 100.sdp
                    )
                    .align(Alignment.CenterHorizontally)
                    .scale(1.4f)
                    .clip(RoundedCornerShape(12.sdp))
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
                    ){

                ProgressIndicator(
                    modifier = Modifier
                        .padding(end= 36.sdp)
                        .size(40.sdp)
                    ,
                    color = colorResource(R.color.white)
                )
                Text(
                    text= "Just a Moment",
                    color = colorResource(R.color.white),
                    style = TextStyleInter18Lh24Fw700()

                )
            }
        }
    }
}
@CompletePreviews
@Composable
fun LoginScreenPortraitPreview() {
    LoginScreenPortrait()
}