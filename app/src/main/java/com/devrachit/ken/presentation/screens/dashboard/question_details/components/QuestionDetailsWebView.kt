package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.viewinterop.AndroidView
import com.devrachit.ken.R
import com.devrachit.ken.utility.composeUtility.sdp

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun QuestionDetailsWebView(
    questionSlug: String,
    questionTitle: String,
    onBackClick: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bg_neutral))
    ) {
        // Header with back button
        QuestionDetailsHeader(
            questionTitle = questionTitle,
            questionSlug = questionSlug,
            onBackClick = onBackClick
        )
        
        Box(modifier = Modifier.fillMaxSize()) {
            // WebView
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            setSupportZoom(true)
                            builtInZoomControls = true
                            displayZoomControls = false
                            loadWithOverviewMode = true
                            useWideViewPort = true
                        }
                        
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                            }
                        }
                        
                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                                progress = newProgress
                                if (newProgress == 100) {
                                    isLoading = false
                                }
                            }
                        }
                        
                        loadUrl("https://leetcode.com/problems/$questionSlug/")
                    }
                },
                update = { webView ->
                    // Update webview if needed
                },
                modifier = Modifier.fillMaxSize()
            )
            
            // Loading indicator
            if (isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                ) {
                    LinearProgressIndicator(
                        progress = progress / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.sdp),
                        color = colorResource(R.color.blue_normal_500),
                        trackColor = colorResource(R.color.card_elevated).copy(alpha = 0.3f)
                    )
                }
                
                // Center loading spinner for initial load
                if (progress < 10) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorResource(R.color.bg_neutral)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = colorResource(R.color.blue_normal_500)
                        )
                    }
                }
            }
        }
    }
}
