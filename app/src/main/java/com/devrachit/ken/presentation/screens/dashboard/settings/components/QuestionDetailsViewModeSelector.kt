package com.devrachit.ken.presentation.screens.dashboard.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter12Lh16Fw400
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.utility.composeUtility.sdp

@Composable
fun QuestionDetailsViewModeSelector(
    currentViewMode: String,
    onViewModeChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.sdp))
            .background(colorResource(R.color.card_elevated).copy(alpha = 0.3f))
            .padding(16.sdp)
    ) {
        Text(
            text = "Question Details View",
            color = colorResource(R.color.white),
            style = TextStyleInter14Lh20Fw400(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.sdp)
        )
        
        // Pager View Option
        ViewModeOption(
            title = "Pager View",
            description = "Organized tabs for Description, Examples, Constraints, etc.",
            isSelected = currentViewMode == "PAGER",
            onClick = { onViewModeChanged("PAGER") }
        )
        
        Spacer(modifier = Modifier.height(8.sdp))
        
        // Simple Scrollable View Option
        ViewModeOption(
            title = "Simple Scrollable",
            description = "Clean single-scroll view with all content",
            isSelected = currentViewMode == "SIMPLE",
            onClick = { onViewModeChanged("SIMPLE") }
        )
        
        Spacer(modifier = Modifier.height(8.sdp))
        
        // WebView Option
        ViewModeOption(
            title = "WebView",
            description = "Native LeetCode web interface",
            isSelected = currentViewMode == "WEBVIEW",
            onClick = { onViewModeChanged("WEBVIEW") }
        )
    }
}

@Composable
private fun ViewModeOption(
    title: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.sdp))
            .background(
                if (isSelected) colorResource(R.color.blue_normal_500).copy(alpha = 0.15f)
                else colorResource(R.color.card_elevated).copy(alpha = 0.2f)
            )
            .clickable(onClick = onClick)
            .padding(12.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = colorResource(R.color.blue_normal_500),
                unselectedColor = colorResource(R.color.white).copy(alpha = 0.5f)
            )
        )
        
        Spacer(modifier = Modifier.width(12.sdp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = if (isSelected) colorResource(R.color.blue_normal_500)
                        else colorResource(R.color.white),
                style = TextStyleInter14Lh20Fw400(),
                fontWeight = FontWeight.SemiBold
            )
            
            Text(
                text = description,
                color = colorResource(R.color.white).copy(alpha = 0.6f),
                style = TextStyleInter12Lh16Fw400(),
                modifier = Modifier.padding(top = 4.sdp)
            )
        }
    }
}
