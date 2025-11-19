package com.devrachit.ken.presentation.screens.dashboard.question_details.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devrachit.ken.R
import com.devrachit.ken.ui.theme.TextStyleInter14Lh20Fw400
import com.devrachit.ken.utility.composeUtility.sdp
import com.devrachit.ken.presentation.screens.dashboard.sheets.components.SaveQuestionDialog

@Composable
fun QuestionDetailsSettings(
    questionSlug: String,
    questionTitle: String,
    questionId: String = "",
    difficulty: String = ""
) {
    var showMenu by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.sdp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Quick Save Button
        IconButton(
            onClick = { showSaveDialog = true },
            modifier = Modifier.size(40.sdp)
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Save to sheet",
                tint = colorResource(R.color.white),
                modifier = Modifier.size(24.sdp)
            )
        }
        
        // More Options Menu
        Box {
            IconButton(
                onClick = { showMenu = true },
                modifier = Modifier.size(40.sdp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = colorResource(R.color.white),
                    modifier = Modifier.size(24.sdp)
                )
            }
        
        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            modifier = Modifier
                .background(colorResource(R.color.card_elevated))
                .widthIn(min = 200.dp)
        ) {
            // Save Question
            SettingsMenuItem(
                icon = R.drawable.ic_copy,
                text = "Save to Sheet",
                onClick = {
                    showSaveDialog = true
                    showMenu = false
                }
            )

            Divider(color = colorResource(R.color.white).copy(alpha = 0.1f))
            
            // Copy Link
            SettingsMenuItem(
                icon = R.drawable.ic_link,
                text = "Copy Link",
                onClick = {
                    copyToClipboard(
                        context = context,
                        text = "https://leetcode.com/problems/$questionSlug/",
                        label = "Question Link"
                    )
                    showMenu = false
                }
            )
            
            Divider(color = colorResource(R.color.white).copy(alpha = 0.1f))
            
            // Open in Browser
            SettingsMenuItem(
                icon = R.drawable.ic_open_in_browser,
                text = "Open in Browser",
                onClick = {
                    openInBrowser(context, questionSlug)
                    showMenu = false
                }
            )
            
            Divider(color = colorResource(R.color.white).copy(alpha = 0.1f))
            
            // Share Question
            SettingsMenuItem(
                icon = R.drawable.ic_share,
                text = "Share Question",
                onClick = {
                    shareQuestion(context, questionTitle, questionSlug)
                    showMenu = false
                }
            )
            
            Divider(color = colorResource(R.color.white).copy(alpha = 0.1f))
            
            // Copy Title
            SettingsMenuItem(
                icon = R.drawable.ic_copy,
                text = "Copy Title",
                onClick = {
                    copyToClipboard(
                        context = context,
                        text = questionTitle,
                        label = "Question Title"
                    )
                    showMenu = false
                }
            )
        }
        }
    }

    // Save Question Dialog
    if (showSaveDialog) {
        SaveQuestionDialog(
            questionId = questionId,
            questionTitle = questionTitle,
            questionSlug = questionSlug,
            difficulty = difficulty,
            onDismiss = { showSaveDialog = false },
            onSaveSuccess = {
                Toast.makeText(context, "Question saved successfully!", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun SettingsMenuItem(
    icon: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.sdp, vertical = 12.sdp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = colorResource(R.color.white).copy(alpha = 0.9f),
            modifier = Modifier.size(20.sdp)
        )
        Spacer(modifier = Modifier.width(12.sdp))
        Text(
            text = text,
            color = colorResource(R.color.white).copy(alpha = 0.9f),
            style = TextStyleInter14Lh20Fw400()
        )
    }
}

private fun copyToClipboard(context: Context, text: String, label: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
}

private fun openInBrowser(context: Context, questionSlug: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://leetcode.com/problems/$questionSlug/"))
    context.startActivity(intent)
}

private fun shareQuestion(context: Context, questionTitle: String, questionSlug: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "Check out this LeetCode problem: $questionTitle\nhttps://leetcode.com/problems/$questionSlug/")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
}
