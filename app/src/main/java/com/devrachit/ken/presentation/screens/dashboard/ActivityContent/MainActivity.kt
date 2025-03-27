package com.devrachit.ken.presentation.screens.dashboard.ActivityContent

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.devrachit.ken.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindow()
        setContent {
            DashboardContent()
        }
    }
    private fun setupWindow() {
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val bgColor = getColor(R.color.bg_neutral)
        val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = false
        windowInsetsController.isAppearanceLightNavigationBars = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.statusBarColor = bgColor
            window.navigationBarColor = bgColor
        } else {
            window.statusBarColor = bgColor
            window.navigationBarColor = bgColor
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
//    override fun finish() {
//        super.finish()
//        val options = ActivityOptionsCompat.makeCustomAnimation(
//            this,
//            R.anim.slide_in_right,
//            R.anim.slide_out_left
//        )
//        startActivity(intent, options.toBundle())
//    }

}