package com.devrachit.ken.presentation.screens.dashboard.Widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Job

@Composable
fun DashboardHeader(
    username: String,
    onClick: () -> Job,
    drawerProgress: Float = 0f
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        MenuButton(onClick = onClick, drawerProgress = drawerProgress)
        UsernameDisplay(username = username, drawerProgress = drawerProgress)
    }
}
@Composable
fun DashboardHeaderDetails(
    username: String,
    onClick: () -> Unit,
    drawerProgress: Float = 0f
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        BackButton(onClick = onClick, drawerProgress = drawerProgress)
        UsernameDisplay(username = username, drawerProgress = drawerProgress)
    }
}