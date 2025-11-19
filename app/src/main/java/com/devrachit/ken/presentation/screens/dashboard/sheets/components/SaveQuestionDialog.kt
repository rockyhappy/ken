package com.devrachit.ken.presentation.screens.dashboard.sheets.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devrachit.ken.presentation.screens.dashboard.sheets.SheetsScreenViewModel

@Composable
fun SaveQuestionDialog(
    questionId: String,
    questionTitle: String,
    questionSlug: String,
    difficulty: String,
    onDismiss: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: SheetsScreenViewModel = hiltViewModel()
) {
    val folders by viewModel.folders.collectAsState()
    var selectedFolderIndices by remember { mutableStateOf(setOf<Int>()) }
    var newFolderName by remember { mutableStateOf("") }
    var showNewFolderInput by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Save Question to Folder")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Question Info
                Text(
                    text = questionTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )

                // Folders List
                if (folders.isNotEmpty()) {
                    Text(
                        text = "Select folders to save to:",
                        style = MaterialTheme.typography.labelMedium
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(folders.size) { index ->
                            val folder = folders[index]
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedFolderIndices = if (selectedFolderIndices.contains(index)) {
                                            selectedFolderIndices - index
                                        } else {
                                            selectedFolderIndices + index
                                        }
                                    }
                                    .background(
                                        if (selectedFolderIndices.contains(index))
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                        else
                                            MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = folder.folderName,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    if (folder.folderDescription.isNotEmpty()) {
                                        Text(
                                            text = folder.folderDescription,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                androidx.compose.material3.Checkbox(
                                    checked = selectedFolderIndices.contains(index),
                                    onCheckedChange = { isChecked ->
                                        selectedFolderIndices = if (isChecked) {
                                            selectedFolderIndices + index
                                        } else {
                                            selectedFolderIndices - index
                                        }
                                    }
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "No folders yet. Create one!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Create New Folder Option
                if (showNewFolderInput) {
                    OutlinedTextField(
                        value = newFolderName,
                        onValueChange = { newFolderName = it },
                        label = { Text("Folder name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                } else {
                    OutlinedButton(
                        onClick = { showNewFolderInput = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Create new folder",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("Create New Folder")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (showNewFolderInput && newFolderName.isNotBlank()) {
                        // Create new folder and save question to it
                        viewModel.createFolder(newFolderName, "")
                        viewModel.saveQuestion(
                            questionTitle = questionTitle,
                            questionSlug = questionSlug,
                            questionDifficulty = difficulty,
                            questionUrl = "https://leetcode.com/problems/$questionSlug/",
                            folderId = folders.size
                        )
                    } else if (selectedFolderIndices.isNotEmpty()) {
                        // Save to all selected folders
                        selectedFolderIndices.forEach { index ->
                            val folder = folders[index]
                            viewModel.saveQuestion(
                                questionTitle = questionTitle,
                                questionSlug = questionSlug,
                                questionDifficulty = difficulty,
                                questionUrl = "https://leetcode.com/problems/$questionSlug/",
                                folderId = folder.folderId
                            )
                        }
                    }
                    onDismiss()
                    onSaveSuccess()
                },
                enabled = (showNewFolderInput && newFolderName.isNotBlank()) || selectedFolderIndices.isNotEmpty(),
                colors = ButtonDefaults.buttonColors()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
