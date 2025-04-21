package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devrachit.ken.R
import com.devrachit.ken.utility.constants.Constants.Companion.NAVKEYUSERNAME
import dagger.hilt.android.AndroidEntryPoint

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
            DashboardContent(username = username, uiState = uiStates)
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
//        override fun finish() {
//        super.finish()
//        val options = ActivityOptionsCompat.makeCustomAnimation(
//            this,
//            R.anim.slide_in_right,
//            R.anim.slide_out_left
//        )
//        startActivity(intent, options.toBundle())
//    }

}