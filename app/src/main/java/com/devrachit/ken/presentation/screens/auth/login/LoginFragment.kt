package com.devrachit.ken.presentation.screens.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import com.devrachit.ken.presentation.screens.auth.login.LoginViewmodel

@AndroidEntryPoint
class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View
    {
        val viewModel: LoginViewmodel by viewModels()
      return ComposeView(requireContext()).apply {
          setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
          setContent {
              LoginScreenPortrait()
          }
          
      }
    }
}