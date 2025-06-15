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
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var remoteConfig: FirebaseRemoteConfig
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
        initRemoteConfig()
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            try {
                val fetched = withTimeoutOrNull(5000L) { // Increased timeout for better chances of success
                    remoteConfig.fetchAndActivate().await()
                } ?: false

                Log.d("RemoteConfig", "Fetch success: $fetched")

                val forceUpdate = remoteConfig.getBoolean("force_playstore_update")
                val minVersion = remoteConfig.getString("minimum_required_version")
                val message = remoteConfig.getString("playstore_update_message")
                val url = remoteConfig.getString("playstore_update_url")

                Log.d("RemoteConfig", "force: $forceUpdate, minVer: $minVersion, msg: $message, url: $url")

                val config = UpdateConfig(
                    forcePlaystoreUpdate = forceUpdate,
                    minimumRequiredVersion = minVersion,
                    playstoreUpdateMessage = message,
                    playstoreUpdateUrl = url
                )

                viewModel.setUpdateConfig(config, getPresentVersion = {getCurrentAppVersion()})
                checkAndHandleUpdate(config)

            } catch (e: Exception) {
                Log.e("RemoteConfig", "Remote Config fetch failed: ${e.message}")
                viewModel.navigateForward()
            }

            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.navigationState.collectLatest { navState ->
//                    if(viewModel.uiState.value.navigateToScreen)
//                    handleNavigation(navState)
                    viewModel.uiState.collectLatest {uiState->
                        if(uiState.navigateToScreen)
                            handleNavigation(navState)
                    }
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
    private fun initRemoteConfig() {
        remoteConfig = FirebaseRemoteConfig.getInstance()

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0L)
            .setFetchTimeoutInSeconds(10L)
            .build()

        remoteConfig.setConfigSettingsAsync(configSettings)

//        val defaults = mapOf(
//            "force_playstore_update" to false,
//            "minimum_required_version" to getCurrentAppVersion(),
//            "playstore_update_message" to "A new version is available. Please update to continue.",
//            "playstore_update_url" to "https://play.google.com/store/apps/details?id=com.devrachit.ken"
//        )
//
//        remoteConfig.setDefaultsAsync(defaults)
    }
    private fun checkAndHandleUpdate(config: UpdateConfig) {
        val currentVersion = getCurrentAppVersion()
        val requiredVersion = config.minimumRequiredVersion

        Log.d("AppUpdate", "Current: $currentVersion, Required: $requiredVersion")

        // 1. If versions match, do nothing
        if (compareVersions(currentVersion, requiredVersion) == 0) {
            Log.d("AppUpdate", "Versions match. No update required.")
            viewModel.setUpdateConfig(
                UpdateConfig(
                    forcePlaystoreUpdate = config.forcePlaystoreUpdate,
                    minimumRequiredVersion = config.minimumRequiredVersion,
                    playstoreUpdateMessage = "No update required.",
                    playstoreUpdateUrl = config.playstoreUpdateUrl
                ),
                getPresentVersion = {getCurrentAppVersion()}
            )
            return
        }

        // 2. Show forced update sheet (non-dismissible)
        if (config.forcePlaystoreUpdate) {
            viewModel.setUpdateConfig(
                UpdateConfig(
                    forcePlaystoreUpdate = config.forcePlaystoreUpdate,
                    minimumRequiredVersion = config.minimumRequiredVersion,
                    playstoreUpdateMessage = config.playstoreUpdateMessage,
                    playstoreUpdateUrl = config.playstoreUpdateUrl
                ),
                getPresentVersion = {getCurrentAppVersion()}
            )
        }
        else {
            Log.d("AppUpdate", "Showing optional update sheet (can dismiss).")
            viewModel.setUpdateConfig(
                UpdateConfig(
                    forcePlaystoreUpdate = config.forcePlaystoreUpdate,
                    minimumRequiredVersion = config.minimumRequiredVersion,
                    playstoreUpdateMessage = config.playstoreUpdateMessage,
                    playstoreUpdateUrl = config.playstoreUpdateUrl
                )
                , getPresentVersion = {getCurrentAppVersion()}
            )
        }
    }

    private fun getCurrentAppVersion(): String {
        return try {
            requireContext().packageManager
                .getPackageInfo(requireContext().packageName, 0).versionName ?: "1.0.0"
        } catch (e: Exception) {
            Log.e("AppUpdate", "Error fetching app version: ${e.message}")
            "1.0.0"
        }
    }

    private fun compareVersions(v1: String, v2: String): Int {
        val parts1 = v1.split(".").mapNotNull { it.toIntOrNull() }
        val parts2 = v2.split(".").mapNotNull { it.toIntOrNull() }
        val maxLength = maxOf(parts1.size, parts2.size)

        for (i in 0 until maxLength) {
            val p1 = parts1.getOrElse(i) { 0 }
            val p2 = parts2.getOrElse(i) { 0 }

            when {
                p1 < p2 -> return -1
                p1 > p2 -> return 1
            }
        }
        return 0
    }
    private fun isUpdateRequired(config: UpdateConfig): Boolean {
        val currentVersion = getCurrentAppVersion()
        val requiredVersion = config.minimumRequiredVersion

        Log.d("AppUpdate", "Comparing Current: $currentVersion with Required: $requiredVersion")

        // 1. If versions match, no update required
        if (compareVersions(currentVersion, requiredVersion) == 0) {
            return false
        }

        // 2. If current version is less than required, update is required
        return compareVersions(currentVersion, requiredVersion) < 0
    }

}