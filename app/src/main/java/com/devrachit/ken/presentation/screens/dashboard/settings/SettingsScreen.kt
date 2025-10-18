package com.devrachit.ken.presentation.screens.dashboard.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.devrachit.ken.R

@Composable
fun SettingsScreen() {
    val viewmodel = hiltViewModel<SettingsViewmodel>()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.content_neutral_primary_white))
    ) {

    }
}