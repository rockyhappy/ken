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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devrachit.ken.R
import com.devrachit.ken.presentation.navigation.Screen
import com.devrachit.ken.presentation.screens.auth.AuthActivity
import com.devrachit.ken.presentation.screens.dashboard.userdetails.UserDetailsScreen
import com.devrachit.ken.presentation.screens.dashboard.userdetails.UserDetailsViewModel
import com.devrachit.ken.utility.composeUtility.LoadingDialog
import com.devrachit.ken.utility.constants.Constants.Companion.NAVKEYUSERNAME
import dagger.hilt.android.AndroidEntryPoint
import android.util.Log

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindow()
        val username = intent.getStringExtra(NAVKEYUSERNAME) ?: "Guest_User"
        
        viewModel.loadUserDetails()
        
        setContent {
            val uiStates = viewModel.userValues.collectAsStateWithLifecycle().value
            val navController = rememberNavController()

            if(uiStates.showDialogLoading) LoadingDialog(true, {},{})
            if(uiStates.navigateToLogin)navigateToNewActivity(this)

            NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
                composable(route = Screen.Dashboard.route) {
                    DashboardContent(
                        logout = viewModel::logout, 
                        username = username, 
                        uiState = uiStates,
                        appNavController = navController
                    )
                }
                composable(
                    route = Screen.UserDetails.routeWithArgs,
                    arguments = listOf(navArgument("username") { type = NavType.StringType })
                ) {
                    val userDetailsViewModel: UserDetailsViewModel = hiltViewModel()
                    val uiState = userDetailsViewModel.uiState.collectAsStateWithLifecycle()
                    var hasInitiallyLoaded = rememberSaveable { mutableStateOf(false) }

                    LaunchedEffect(true) {
                        if (!hasInitiallyLoaded.value) {
                            userDetailsViewModel.loadUserDetails()
                            hasInitiallyLoaded.value = true
                        }
                    }

                    UserDetailsScreen(
                        uiState = uiState.value,
                        onRefresh = { userDetailsViewModel.loadUserDetails() },
                        onBackPress = { navController.popBackStack() },
                        onDeleteUser = { username -> 
                            userDetailsViewModel.deleteUser(username)
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        println("OnResume called")
        super.onResume()
        viewModel.reloadUserDetails()
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
