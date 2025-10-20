package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun ExampleSection(content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.sdp)
            .clip(RoundedCornerShape(16.sdp))
            .border(
                border = BorderStroke(
                    width = 1.sdp,
                    color = colorResource(R.color.blue_normal_500).copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(16.sdp)
            )
            .padding(12.sdp)
    ) {
        // Example title
        Text(
            text = content.substringBefore(":").ifEmpty { "Example" },
            color = colorResource(R.color.blue_normal_500),
            style = TextStyleInter12Lh16Fw400(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.sdp)
        )
        
        // Example content with enhanced styling
        val lines = content.split("\n")
        lines.forEach { line ->
            val trimmedLine = line.trim()
            if (trimmedLine.isNotEmpty() && !trimmedLine.startsWith("Example")) {
                when {
                    trimmedLine.startsWith("Input:") -> {
                        // Style Input: label differently from value
                        Text(
                            text = "Input:",
                            color = colorResource(R.color.white).copy(alpha = 0.7f),
                            style = TextStyleInter12Lh16Fw400(),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 6.sdp, bottom = 2.sdp)
                        )
                        Text(
                            text = trimmedLine.substringAfter("Input:").trim(),
                            color = colorResource(R.color.green_normal_500),
                            style = TextStyleInter12Lh16Fw400(),
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(bottom = 4.sdp)
                        )
                    }
                    trimmedLine.startsWith("Output:") -> {
                        Text(
                            text = "Output:",
                            color = colorResource(R.color.white).copy(alpha = 0.7f),
                            style = TextStyleInter12Lh16Fw400(),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 6.sdp, bottom = 2.sdp)
                        )
                        Text(
                            text = trimmedLine.substringAfter("Output:").trim(),
                            color = colorResource(R.color.blue_normal_500),
                            style = TextStyleInter12Lh16Fw400(),
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(bottom = 4.sdp)
                        )
                    }
                    trimmedLine.startsWith("Explanation:") -> {
                        Text(
                            text = "Explanation:",
                            color = colorResource(R.color.white).copy(alpha = 0.7f),
                            style = TextStyleInter12Lh16Fw400(),
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(top = 6.sdp, bottom = 2.sdp)
                        )
                        Text(
                            text = trimmedLine.substringAfter("Explanation:").trim(),
                            color = colorResource(R.color.white).copy(alpha = 0.85f),
                            style = TextStyleInter12Lh16Fw400(),
                            modifier = Modifier.padding(bottom = 4.sdp)
                        )
                    }
                    else -> {
                        Text(
                            text = trimmedLine,
                            color = colorResource(R.color.white).copy(alpha = 0.85f),
                            style = TextStyleInter12Lh16Fw400(),
                            modifier = Modifier.padding(vertical = 2.sdp)
                        )
                    }
                }
            }
        }
    }
}
