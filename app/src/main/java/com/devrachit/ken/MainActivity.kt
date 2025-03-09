package com.devrachit.ken

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devrachit.ken.api.LeetCodeApiService
import com.devrachit.ken.data.LeetCodeRepository
import com.devrachit.ken.ui.components.LeetCodeHeatmap
import com.devrachit.ken.ui.theme.KenTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KenTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val contributionsData = remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    // Replace "your_leetcode_username" with actual username
    val username = "rockyhappy"

    LaunchedEffect(key1 = username) {
        val repository = LeetCodeRepository(LeetCodeApiService.create())
        try {
            val data = withContext(Dispatchers.IO) {
                repository.getUserHeatmap(username)
            }
            contributionsData.value = data
        } catch (e: Exception) {
            // Handle error
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "LeetCode Heatmap for $username",
                style = MaterialTheme.typography.headlineMedium
            )

            LeetCodeHeatmap(
                contributions = contributionsData.value,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    KenTheme {
        MainScreen()
    }
}