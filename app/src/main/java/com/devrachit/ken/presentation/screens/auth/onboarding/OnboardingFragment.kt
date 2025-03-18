package com.devrachit.ken.presentation.screens.auth.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: OnboardingViewmodel by viewModels()
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val userValues=viewModel.userValues.collectAsStateWithLifecycle()
                OnboardingScreenPortrait(
                    userValues = userValues.value,
                    updateUserName = viewModel::updateUserName
                )
            }
        }
    }
}