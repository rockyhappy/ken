package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import com.devrachit.ken.R
import com.devrachit.ken.data.local.entity.SheetEntity
import com.devrachit.ken.data.local.entity.SheetWithQuestions
import com.devrachit.ken.presentation.screens.dashboard.sheets.FAVORITES_SHEET_NAME
import com.devrachit.ken.ui.theme.*
import com.devrachit.ken.utility.composeUtility.sdp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToSheetBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    sheets: List<SheetWithQuestions>,
    questionSheetsIds: Set<Long>,
    newSheetName: String,
    isCreatingSheet: Boolean,
    onNewSheetNameChange: (String) -> Unit,
    onCreateSheet: () -> Unit,
    onAddToSheet: (Long) -> Unit,
    onRemoveFromSheet: (Long) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = colorResource(R.color.card_elevated),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.sdp)
                        .width(40.sdp)
                        .height(4.sdp)
                        .clip(RoundedCornerShape(2.sdp))
                        .background(Color.White.copy(alpha = 0.3f))
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.sdp)
                    .padding(bottom = 32.sdp)
            ) {
                // Title
                Text(
                    text = "Add to Sheet",
                    style = TextStyleInter16Lh24Fw700(),
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.sdp)
                )

                // Create New Sheet Section
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.sdp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.sdp)
                ) {
                    OutlinedTextField(
                        value = newSheetName,
                        onValueChange = onNewSheetNameChange,
                        placeholder = {
                            Text(
                                "New Sheet Name",
                                color = Color.White.copy(alpha = 0.5f),
                                style = TextStyleInter14Lh20Fw400()
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = colorResource(R.color.blue_normal_500),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            cursorColor = colorResource(R.color.blue_normal_500)
                        ),
                        shape = RoundedCornerShape(12.sdp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = { if (newSheetName.isNotBlank()) onCreateSheet() }
                        )
                    )

                    Button(
                        onClick = onCreateSheet,
                        enabled = newSheetName.isNotBlank() && !isCreatingSheet,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.blue_normal_500),
                            disabledContainerColor = colorResource(R.color.blue_normal_500).copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.sdp),
                        modifier = Modifier.height(56.sdp)
                    ) {
                        if (isCreatingSheet) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.sdp),
                                color = Color.White,
                                strokeWidth = 2.sdp
                            )
                        } else {
                            Text(
                                "Add",
                                style = TextStyleInter14Lh20Fw400(),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Divider
                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.1f),
                    modifier = Modifier.padding(vertical = 8.sdp)
                )

                // Sheets List - Sort with Favorites first, then alphabetically
                val sortedSheets = remember(sheets) {
                    sheets.sortedWith(compareBy(
                        { it.sheet.name != FAVORITES_SHEET_NAME },
                        { it.sheet.name.lowercase() }
                    ))
                }
                
                if (sortedSheets.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.sdp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No sheets yet. Create one above!",
                            style = TextStyleInter14Lh18Fw400(),
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.sdp),
                        verticalArrangement = Arrangement.spacedBy(8.sdp)
                    ) {
                        items(
                            items = sortedSheets,
                            key = { it.sheet.id }
                        ) { sheetWithQuestions ->
                            val isInSheet = questionSheetsIds.contains(sheetWithQuestions.sheet.id)
                            
                            SheetSelectionItem(
                                sheetWithQuestions = sheetWithQuestions,
                                isSelected = isInSheet,
                                isFavorites = sheetWithQuestions.sheet.name == FAVORITES_SHEET_NAME,
                                onClick = {
                                    if (isInSheet) {
                                        onRemoveFromSheet(sheetWithQuestions.sheet.id)
                                    } else {
                                        onAddToSheet(sheetWithQuestions.sheet.id)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SheetSelectionItem(
    sheetWithQuestions: SheetWithQuestions,
    isSelected: Boolean,
    isFavorites: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.sdp))
            .background(
                if (isSelected) colorResource(R.color.blue_normal_500).copy(alpha = 0.2f)
                else colorResource(R.color.bg_neutral)
            )
            .clickable(onClick = onClick)
            .padding(16.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Checkbox
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onClick() },
            colors = CheckboxDefaults.colors(
                checkedColor = if (isFavorites) colorResource(R.color.hard_filled_red) else colorResource(R.color.blue_normal_500),
                uncheckedColor = Color.White.copy(alpha = 0.5f),
                checkmarkColor = Color.White
            )
        )

        Spacer(modifier = Modifier.width(8.sdp))
        
        // Favorites icon
        if (isFavorites) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorites",
                tint = colorResource(R.color.hard_filled_red),
                modifier = Modifier.size(20.sdp)
            )
            Spacer(modifier = Modifier.width(8.sdp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = sheetWithQuestions.sheet.name,
                style = TextStyleInter14Lh20Fw400(),
                color = Color.White,
                fontWeight = if (isSelected || isFavorites) FontWeight.SemiBold else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${sheetWithQuestions.questions.size} questions",
                style = TextStyleInter12Lh16Fw400(),
                color = Color.White.copy(alpha = 0.6f)
            )
        }

        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Added to sheet",
                tint = if (isFavorites) colorResource(R.color.hard_filled_red) else colorResource(R.color.blue_normal_500),
                modifier = Modifier.size(20.sdp)
            )
        }
    }
}
