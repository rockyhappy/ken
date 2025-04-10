package com.devrachit.ken.presentation.screens.dashboard.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(

): ViewModel(){

    private val _uiState = MutableStateFlow(HomeUiStates())
    val uiState  : StateFlow<HomeUiStates> = _uiState.asStateFlow()




}