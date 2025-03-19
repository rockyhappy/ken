package com.devrachit.ken.presentation.screens.auth.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter10Lh12Fw500
import com.devrachit.ken.ui.theme.TextStyleInter14Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter16Lh24Fw600
import com.devrachit.ken.ui.theme.TextStyleInter18Lh24Fw700
import com.devrachit.ken.ui.theme.TextStyleInter24Lh36Fw700
import com.devrachit.ken.utility.composeUtility.CompletePreviews
import com.devrachit.ken.utility.composeUtility.OrientationPreviews
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun OnboardingScreenPortrait(
    userValues: User,
    updateUserName: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
        containerColor = colorResource(R.color.bg_neutral)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(colorResource(R.color.bg_neutral)),

            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f)
                    .background(colorResource(R.color.bg_neutral)),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Image(
                    painterResource(id = R.drawable.logo),
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
                Text(
                    text = "Welcome Leetcoder",
                    style = TextStyleInter24Lh36Fw700(),
                    color = colorResource(R.color.white),

                    )

            }
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)  
                    .clip(RoundedCornerShape(topStart = 36.sdp, topEnd = 36.sdp))
                    .background(colorResource(R.color.white))
                    .verticalScroll(state = scrollState)
                ,
                verticalArrangement = Arrangement.spacedBy(0.sdp),
                horizontalAlignment = Alignment.CenterHorizontally,
            )
            {
                Spacer(modifier = Modifier.height(24.sdp))  
                Text(
                    text = "Lets Begin",
                    style = TextStyleInter24Lh36Fw700(),
                    color = colorResource(R.color.content_neutral_primary_black),
//                    modifier = Modifier
//                        .padding(top=200.sdp)
                )
                val focusManager = LocalFocusManager.current
                Spacer(modifier = Modifier.height(12.sdp))  
                OutlinedTextField(
                    value = userValues.userName ?: "",
                    onValueChange = { updateUserName(it) },
                    shape = RoundedCornerShape(10.sdp),
                    modifier = Modifier
                        .padding(start = 24.sdp, end = 24.sdp, top = 10.sdp)
                        .widthIn(400.sdp),
                    label = {
                        Text(
                            text = " Username ",
                            style = TextStyleInter14Lh16Fw400(),
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.sdp))
                                .background(Color.Transparent)
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    maxLines = 1,
                    isError = userValues?.isUserNameValid != true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colorResource(id = R.color.bg_neutral_light_default),
                        unfocusedContainerColor = colorResource(id = R.color.bg_neutral_light_default),
                        focusedBorderColor = if (userValues.isUserNameValid != false) colorResource(
                            id = R.color.content_neutral_primary_black
                        ) else colorResource(id = R.color.stroke_danger_normal),
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = if (userValues.isUserNameValid != false) colorResource(id = R.color.content_neutral_primary_black) else colorResource(
                            id = R.color.stroke_danger_normal
                        ),
                        focusedLabelColor = if (userValues.isUserNameValid != false) colorResource(
                            id = R.color.content_neutral_primary_black
                        ) else colorResource(
                            id = R.color.stroke_danger_normal
                        ),
                        unfocusedLabelColor = colorResource(id = R.color.content_neutral_primary_black),
                        focusedTextColor = colorResource(id = R.color.content_neutral_primary_black),
                        unfocusedTextColor = colorResource(id = R.color.content_neutral_primary_black),
                        focusedPlaceholderColor = if (userValues.isUserNameValid != false) colorResource(
                            id = R.color.content_neutral_primary_black
                        ) else colorResource(
                            id = R.color.stroke_danger_normal
                        ),
                        unfocusedPlaceholderColor = colorResource(id = R.color.content_neutral_primary_black),
                        errorBorderColor = colorResource(id = R.color.stroke_danger_normal),
                        errorContainerColor = colorResource(id = R.color.bg_neutral_light_default),
                        errorLabelColor = colorResource(id = R.color.stroke_danger_normal),
                    ),

                    )
                Text(
                    text = " Enter your LEETCODE username to login",
                    style = TextStyleInter14Lh16Fw400(),
                    modifier = Modifier
                        .padding(start = 24.sdp, end = 24.sdp, top = 10.sdp)
                        .padding(bottom = 20.sdp)
                        .widthIn(400.sdp)
                )
                Spacer(modifier = Modifier.height(24.sdp))  
                Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(start = 24.sdp, end = 24.sdp, top = 10.sdp)
                        .height(50.sdp)
                        .widthIn(400.sdp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.content_neutral_primary_black),
                        disabledContainerColor = colorResource(id = R.color.surface_card_normal_default),
                    ),
                    shape = RoundedCornerShape(24.sdp)
                ) {
                    Text(
                        text = "Continue",
                        color = colorResource(id = R.color.extra_blue_0),
                        style = TextStyleInter16Lh24Fw600()
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(top = 36.sdp, bottom = 16.sdp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                )
                {
                    Box(
                        modifier = Modifier
                            .width(60.sdp)
                            .height(1.sdp)
                            .alpha(0.5f)
                            .background(
                                colorResource(id = R.color.content_neutral_secondary),
                                shape = RoundedCornerShape(0.sdp)
                            )

                    )
                    Text(
                        text = " OR ",
                        style = TextStyleInter14Lh16Fw400(),
                        modifier = Modifier.alpha(0.6f),
                        color = colorResource(id = R.color.content_neutral_secondary)
                    )
                    Box(
                        modifier = Modifier
                            .width(60.sdp)
                            .height(1.sdp)
                            .alpha(0.5f)
                            .background(
                                colorResource(id = R.color.content_neutral_secondary),
                                shape = RoundedCornerShape(0.sdp)
                            )

                    )
                }
                OutlinedButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .padding(start = 24.sdp, end = 24.sdp, top = 16.sdp)
                        .height(50.sdp)
                        .widthIn(400.sdp),
                    shape = RoundedCornerShape(24.sdp),
                    border = BorderStroke(1.sdp, colorResource(id = R.color.content_neutral_secondary))

                ) {
                    Text(
                        text = "Guest User",
                        color = colorResource(id = R.color.content_neutral_primary_black),
                        style = TextStyleInter16Lh24Fw600()
                    )
                }

                Text(
                    text = stringResource(R.string.make_an_account_text),
                    style = TextStyleInter14Lh16Fw400(),
                    modifier = Modifier.padding(top = 16.sdp),
                    color= colorResource(id = R.color.content_neutral_primary_black)
                )
            }

        }
    }
}


@CompletePreviews
@OrientationPreviews
@Composable
fun OnboardingScreenPotraitPreview() {
    OnboardingScreenPortrait(User(), updateUserName = {})
}