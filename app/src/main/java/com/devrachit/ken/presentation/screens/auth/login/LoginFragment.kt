package com.devrachit.ken.presentation.screens.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.devrachit.ken.presentation.screens.dashboard.ActivityContent.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.devrachit.ken.R
import com.devrachit.ken.utility.constants.Constants.Companion.NAVKEYUSERNAME
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewmodel by viewModels()
    private lateinit var binding: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LoginScreenPortrait()
            }
        }
        binding = composeView
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(1000)
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.navigationState.collect { navState ->
                    handleNavigation(navState)
                }
            }
        }
    }

    private fun handleNavigation(navState: LoginNavigationState) {
        when (navState) {
            is LoginNavigationState.NavigateToMainActivity -> {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.putExtra(NAVKEYUSERNAME, navState.username)
                startActivity(intent)
                requireActivity().finish()
                requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                viewModel.resetNavigationState()
            }
            is LoginNavigationState.NavigateToOnboarding -> {
                findNavController().navigate(R.id.action_loginFragment_to_onboardingFragment)
                viewModel.resetNavigationState()
            }
            is LoginNavigationState.Error -> {
                Toast.makeText(requireContext(), navState.message, Toast.LENGTH_LONG).show()
                viewModel.resetNavigationState()
            }
            LoginNavigationState.Idle -> {
                // Do nothing
            }
        }
    }
}