package com.devrachit.ken.presentation.screens.auth.login

import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter14Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter18Lh24Fw700
import com.devrachit.ken.utility.composeUtility.CompletePreviews
import com.devrachit.ken.utility.composeUtility.ProgressIndicator
import com.devrachit.ken.utility.composeUtility.UpdateDialog
import com.devrachit.ken.utility.composeUtility.sdp
import androidx.core.net.toUri
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreenPortrait(
    viewmodel: LoginViewmodel
) {
    val context = LocalContext.current
    val uiStates = viewmodel.uiState.collectAsStateWithLifecycle().value
    val firebaseAnalytics = Firebase.analytics

    Log.d("LoginScreenPortrait", "LoginScreenPortrait: ${uiStates.updateConfig}")

    LaunchedEffect(Unit) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "login_screen")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "LoginScreenPortrait")
        }
    }

    LaunchedEffect(uiStates.updateStatus) {
        uiStates.updateStatus?.let { status ->
            firebaseAnalytics.logEvent("login_update_status_changed") {
                param("update_status", status.name)
                param("has_update_config", (uiStates.updateConfig != null).toString())
            }
        }
    }

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

            when (uiStates.updateStatus) {
                UpdateStatus.NoNeedToUpdate -> {
                    firebaseAnalytics.logEvent("login_no_update_needed") {}
                    viewmodel.navigateForward()
                }
                UpdateStatus.NoForceUpdate -> {
                    firebaseAnalytics.logEvent("login_optional_update_shown") {
                        param("update_message", uiStates.updateConfig?.playstoreUpdateMessage ?: "default")
                    }
                    UpdateDialog(
                        showDialog = true, // Set to true directly for non-force updates
                        onDismissRequest = {
                            firebaseAnalytics.logEvent("login_update_dialog_dismissed") {
                                param("update_type", "optional")
                            }
                            viewmodel.navigateForward()
                        },
                        onConfirmExit = {
                            firebaseAnalytics.logEvent("login_update_dialog_confirmed") {
                                param("update_type", "optional")
                                param("store_url", uiStates.updateConfig?.playstoreUpdateUrl ?: "default")
                            }
                            val intent = Intent(Intent.ACTION_VIEW,
                                (uiStates.updateConfig?.playstoreUpdateUrl
                                    ?: "market://details?id=com.devrachit.ken").toUri())
                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                            } else {
                                firebaseAnalytics.logEvent("login_update_store_not_found") {
                                    param("store_url", uiStates.updateConfig?.playstoreUpdateUrl ?: "default")
                                }
                                println("No app found to open this link: ${uiStates.updateConfig?.playstoreUpdateUrl}")
                            }
                        },
                        text = uiStates.updateConfig?.playstoreUpdateMessage
                            ?: "An update is available. Please update the app to continue.",
                        dismissVisible = true
                    )
                }
                UpdateStatus.ForceUpdate -> {
                    firebaseAnalytics.logEvent("login_force_update_shown") {
                        param("update_message", uiStates.updateConfig?.playstoreUpdateMessage ?: "default")
                    }
                    UpdateDialog(
                        showDialog = true,
                        onDismissRequest = {},
                        onConfirmExit = {
                        },
                        text = uiStates.updateConfig?.playstoreUpdateMessage
                            ?: "An update is available. Please update the app to continue.",
                        dismissVisible = false
                    )
                }
                UpdateStatus.Error -> {
                    firebaseAnalytics.logEvent("login_update_check_error") {}
                    viewmodel.navigateForward()
                }
                null -> {
                    firebaseAnalytics.logEvent("login_update_status_null") {}
//                    viewmodel.navigateForward()
                }
            }
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
            ) {

                ProgressIndicator(
                    modifier = Modifier
                        .padding(end = 36.sdp)
                        .size(40.sdp),
                    color = colorResource(R.color.white)
                )
                Text(
                    text = "Just a Moment",
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
//    LoginScreenPortrait()
}
