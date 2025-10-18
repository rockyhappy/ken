package com.devrachit.ken.presentation.screens.dashboard.settings

import androidx.lifecycle.ViewModel
import com.devrachit.ken.domain.usecases.getCurrentTime.GetCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewmodel@Inject constructor(
    private val getCurrentTime: GetCurrentTime,
): ViewModel() {
}