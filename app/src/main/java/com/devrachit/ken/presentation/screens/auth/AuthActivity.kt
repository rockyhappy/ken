package com.devrachit.ken.presentation.screens.auth

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.devrachit.ken.presentation.screens.dashboard.MainActivity
import com.devrachit.ken.R
import com.devrachit.ken.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import kotlin.text.toFloat

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindow()
        setupSplashScreen()
        setupBinding()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                setupSplashExitAnimation(splashScreenView)
            }
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

    private fun setupSplashScreen() {
        installSplashScreen()
    }

    private fun setupSplashExitAnimation(splashScreenView: View) {
        ObjectAnimator.ofFloat(
            splashScreenView,
            View.TRANSLATION_X,
            0f,
            -splashScreenView.height.toFloat()
        ).apply {
            duration = 600
            doOnEnd {
                (splashScreenView.parent as? ViewGroup)?.removeView(splashScreenView)
            }
        }.also {
            it.start()
        }

    }

    private fun setupBinding() {
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    override fun finish() {
        val rootView = binding.root
        ObjectAnimator.ofFloat(
            rootView,
            View.TRANSLATION_X,
            0f,
            rootView.width.toFloat()
        ).apply {
            duration = 600
            doOnEnd {
                super.finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }.start()
    }

}