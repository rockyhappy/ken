package com.devrachit.ken.presentation.screens.auth.login

import androidx.compose.runtime.Composable
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.core.content.ContextCompat
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
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

        }
    }
}