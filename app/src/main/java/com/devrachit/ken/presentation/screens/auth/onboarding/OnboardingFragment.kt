package com.devrachit.ken.presentation.screens.auth.onboarding

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.findFirstRoot
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.findNavController
import com.devrachit.ken.presentation.screens.dashboard.ActivityContent.MainActivity
import com.devrachit.ken.R
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
                if(userValues.value.isUserNameVerified)
                {
                    navigateToNewActivity()
                }
                OnboardingScreenPortrait(
                    userValues = userValues.value,
                    updateUserName = viewModel::updateUserName,
                    onContinueButtonClick = viewModel::checkUserExists,
                    onVerified =  { navigateToNewActivity() }
                )
            }
        }
    }
    private fun navigateToNewActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
        requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}