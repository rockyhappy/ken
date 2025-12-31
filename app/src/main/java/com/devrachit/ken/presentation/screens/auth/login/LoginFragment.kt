package com.devrachit.ken.presentation.screens.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.devrachit.ken.presentation.screens.dashboard.ActivityContent.MainActivity
import com.devrachit.ken.data.remote.firebase.FirebaseRemoteConfigManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.devrachit.ken.R
import com.devrachit.ken.utility.constants.Constants.Companion.NAVKEYUSERNAME
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var firebaseRemoteConfigManager: FirebaseRemoteConfigManager

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
                LoginScreenPortrait(
                    viewmodel=viewModel
                )
            }
        }
        binding = composeView
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.navigationState.collectLatest { navState ->
                    viewModel.uiState.collectLatest { uiState ->
                        if (uiState.navigateToScreen)
                            handleNavigation(navState)
                    }
                }
            }
        }

        lifecycleScope.launch {
            try {
                val fetched = firebaseRemoteConfigManager.fetchConfig()
                val updateType = firebaseRemoteConfigManager.getUpdateType()
                val message = firebaseRemoteConfigManager.getUpdateMessage()
                val url = firebaseRemoteConfigManager.getPlayStoreUrl()

                when (updateType) {
                    "FORCED" -> {
                        val config = UpdateConfig(
                            forcePlaystoreUpdate = true,
                            minimumRequiredVersion = firebaseRemoteConfigManager.getString(
                                FirebaseRemoteConfigManager.MINIMUM_REQUIRED_VERSION,
                                firebaseRemoteConfigManager.getCurrentAppVersion()
                            ),
                            playstoreUpdateMessage = message,
                            playstoreUpdateUrl = url
                        )
                        viewModel.setUpdateConfig(config, getPresentVersion = { firebaseRemoteConfigManager.getCurrentAppVersion() })
                    }
                    "OPTIONAL" -> {
                        val config = UpdateConfig(
                            forcePlaystoreUpdate = false,
                            minimumRequiredVersion = firebaseRemoteConfigManager.getString(
                                FirebaseRemoteConfigManager.MINIMUM_REQUIRED_VERSION,
                                firebaseRemoteConfigManager.getCurrentAppVersion()
                            ),
                            playstoreUpdateMessage = message,
                            playstoreUpdateUrl = url
                        )
                        viewModel.setUpdateConfig(config, getPresentVersion = { firebaseRemoteConfigManager.getCurrentAppVersion() })
                    }
                    else -> {
                        viewModel.navigateForward()
                    }
                }

            } catch (e: Exception) {
                viewModel.navigateForward()
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