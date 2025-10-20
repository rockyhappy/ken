package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.devrachit.ken.R
import com.devrachit.ken.presentation.navigation.MainNavHost
import com.devrachit.ken.presentation.screens.auth.AuthActivity
import com.devrachit.ken.utility.composeUtility.LoadingDialog
import com.devrachit.ken.utility.constants.Constants.Companion.NAVKEYUSERNAME
import com.devrachit.ken.presentation.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var deepLinkQuestionSlug by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindow()
        val username = intent.getStringExtra(NAVKEYUSERNAME) ?: "Guest_User"
        
        // Handle deep link for LeetCode problem URLs
        deepLinkQuestionSlug = handleDeepLink(intent)
        
        viewModel.loadUserDetails()
        
        setContent {
            val uiStates = viewModel.userValues.collectAsStateWithLifecycle().value
            val navController = rememberNavController()

            if(uiStates.showDialogLoading) LoadingDialog(true, {},{})
            if(uiStates.navigateToLogin)navigateToNewActivity(this)

            MainNavHost(
                navController = navController,
                viewModel = viewModel,
                username = username,
                uiStates = uiStates
            )
            
            // Navigate to question details if deep link was clicked
            val currentDeepLinkSlug = deepLinkQuestionSlug
            if (currentDeepLinkSlug != null) {
                LaunchedEffect(currentDeepLinkSlug) {
                    navController.navigate(Screen.QuestionDetails.createRoute(currentDeepLinkSlug))
                    // Clear the deep link after navigation
                    deepLinkQuestionSlug = null
                }
            }
        }
    }

    override fun onResume() {
        println("OnResume called")
        super.onResume()
        viewModel.reloadUserDetails()
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        // Handle deep link when app is already running
        handleDeepLink(intent)?.let { questionSlug ->
            // Update state to trigger navigation in Compose
            deepLinkQuestionSlug = questionSlug
        }
    }
    
    /**
     * Extracts question slug from LeetCode problem deep link
     * Example: https://leetcode.com/problems/two-sum -> "two-sum"
     */
    private fun handleDeepLink(intent: Intent): String? {
        val data = intent.data ?: return null
        
        // Check if it's a LeetCode problem URL
        if (data.host == "leetcode.com" && data.path?.startsWith("/problems/") == true) {
            // Extract slug from path: /problems/two-sum/ -> two-sum
            val slug = data.path?.removePrefix("/problems/")?.removeSuffix("/")
            return if (!slug.isNullOrEmpty()) slug else null
        }
        
        return null
    }


    private fun setupWindow() {
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val bgColor = getColor(R.color.bg_neutral)
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
        windowInsetsController.isAppearanceLightNavigationBars = false

        window.statusBarColor = bgColor
        window.navigationBarColor = bgColor
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun navigateToNewActivity(context: MainActivity) {
        val intent = Intent(context, AuthActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}
