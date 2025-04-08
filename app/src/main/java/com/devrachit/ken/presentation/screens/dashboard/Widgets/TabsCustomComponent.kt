package com.devrachit.ken.presentation.screens.dashboard.Widgets

import android.graphics.Color
import androidx.compose.material.MaterialTheme
import com.devrachit.ken.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.TextUnit
import com.devrachit.ken.utility.composeUtility.ssp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp 
import androidx.compose.ui.unit.dp
import com.devrachit.ken.utility.composeUtility.ssp
/**
 * A custom Composable for displaying tabs.
 *
 * @param tabs List of tab titles.
 * @param modifier Modifier for the parent layout.
 * @param textColor Color for tab text.
 * @param selectedFontSize Font size for selected tab text.
 * @param unSelectedFontSize Font size for unselected tab text.
 * @param selectedColor Background color for selected tab.
 * @param unselectedColor Background color for unselected tab.
 * @param cornerRadius Corner radius for the tab background.
 * @param elevation Elevation for the tab background.
 * @param componentPadding Padding for the entire component.
 * @param textPaddingVertical Vertical padding for tab text.
 * @param textPaddingHorizontal Horizontal padding for tab text.
 * @param onSelected Callback function invoked when a tab is selected.
 */
@Composable
fun TabsCustomComponent(
    tabs: List<String>,
    modifier: Modifier,
    textColor: androidx.compose.ui.graphics.Color = colorResource(id = R.color.white),
    selectedFontSize: TextUnit = 18.ssp,
    unSelectedFontSize: TextUnit = 16.ssp,
    selectedColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Transparent,
    unselectedColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Transparent,
    cornerRadius: Dp = 24.dp,
    elevation: Dp = 4.dp,
    componentPadding: Dp = 2.dp,
    textPaddingVertical: Dp = 12.dp,
    textPaddingHorizontal: Dp = 16.dp,
    onSelected: (String) -> Unit
) {
    // State to keep track of the selected tab
    var selectedOption by remember { mutableStateOf(tabs[0]) }

    // Surface composable to create a background for tabs
    Surface(
        shape = RoundedCornerShape(cornerRadius),
        elevation = elevation,
        modifier = modifier
            .wrapContentSize()
            .padding(componentPadding),
        color = unselectedColor
    ) {
        // Row to display the tabs horizontally
        Row(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(cornerRadius))
                .background(unselectedColor)
        ) {
            // Iterate over each tab
            tabs.forEach { text ->
                // Determine font weight and size based on the selected state
                val fontWeight = if (text == selectedOption) FontWeight.Bold else FontWeight.Normal
                val fontSize = if (text == selectedOption) selectedFontSize else unSelectedFontSize

                // Text composable representing each tab
                Text(
                    text = text,
                    color = textColor,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(cornerRadius))
                        .clickable {
                            // Update the selected tab and invoke the callback
                            selectedOption = text
                            onSelected.invoke(text)
                        }
                        .background(
                            if (text == selectedOption) selectedColor else unselectedColor
                        )
                        .padding(
                            vertical = textPaddingVertical,
                            horizontal = textPaddingHorizontal,
                        ),
                )
            }
        }
    }
}