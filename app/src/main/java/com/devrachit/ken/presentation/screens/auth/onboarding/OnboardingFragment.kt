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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.findFirstRoot
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.findNavController
import com.devrachit.ken.presentation.screens.dashboard.ActivityContent.MainActivity
import com.devrachit.ken.R
import com.devrachit.ken.utility.NetworkUtility.NetworkStateObserver
import com.devrachit.ken.utility.composeUtility.NetworkErrorDialog
import com.devrachit.ken.utility.composeUtility.SnackBar
import com.devrachit.ken.utility.constants.Constants.Companion.NAVKEYUSERNAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {
    
    // Create NetworkStateObserver instance
    private lateinit var networkStateObserver: NetworkStateObserver
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the network observer
        networkStateObserver = NetworkStateObserver(requireContext())
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel: OnboardingViewmodel by viewModels()
        
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val userValues = viewModel.userValues.collectAsStateWithLifecycle()
                
                // State to track network connectivity
                var isNetworkConnected by remember { mutableStateOf(true) }
                
                // Observe network state
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(lifecycleOwner) {
                    networkStateObserver.getNetworkState().observe(lifecycleOwner) { connected ->
                        isNetworkConnected = connected
                    }
                    // Force check network state on composition
                    networkStateObserver.checkNetworkState()
                    
                    onDispose {}
                }
                
                if (userValues.value.isUserNameVerified) {
                    navigateToNewActivity(username = userValues.value.userName.toString())
                }
                
                // Show network error dialog when not connected
                if (!isNetworkConnected) {
                    NetworkErrorDialog(
                        onRetry = {
                            networkStateObserver.checkNetworkState()
                        }
                    )
                }
                
                OnboardingScreenPortrait(
                    userValues = userValues.value,
                    updateUserName = viewModel::updateUserName,
                    onContinueButtonClick = {
                        if (isNetworkConnected) {
                            viewModel.checkUserExists()
                        } else {
                            networkStateObserver.checkNetworkState()
                        }
                    },
                    onVerified = { }
                )
            }
        }
    }
    
    private fun navigateToNewActivity(username: String) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.putExtra(NAVKEYUSERNAME, username)
        startActivity(intent)
        requireActivity().finish()
        requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}