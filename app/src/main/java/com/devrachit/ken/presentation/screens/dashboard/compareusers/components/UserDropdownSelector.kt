package com.devrachit.ken.presentation.screens.dashboard.compareusers.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.devrachit.ken.R
import com.devrachit.ken.presentation.screens.dashboard.compareusers.UserComparisonData
import com.devrachit.ken.utility.composeUtility.sdp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDropdownSelector(
    modifier: Modifier = Modifier,
    label: String,
    selectedUser: String?,
    availableUsers: List<String>,
    excludeUser: String? = null,
    onUserSelected: (String) -> Unit,
    userData: UserComparisonData?
) {
    var expanded by remember { mutableStateOf(false) }
    
    // Filter out the excluded user from available options
    val filteredUsers = availableUsers.filter { it != excludeUser }

    Column(
        modifier = modifier
    ) {
        Text(
            text = label,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.8f)
            ),
            modifier = Modifier.padding(bottom = 8.sdp)
        )

        // Dropdown button
        Box {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(id = R.color.bg_neutral)
                ),
                border = CardDefaults.outlinedCardBorder(),
                shape = RoundedCornerShape(8.sdp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.sdp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (userData != null && selectedUser != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            AsyncImage(
                                model = userData.userInfo?.profile?.userAvatar,
                                placeholder = painterResource(R.drawable.profile_placeholder),
                                contentDescription = "Profile picture",
                                modifier = Modifier
                                    .size(24.sdp)
                                    .clip(RoundedCornerShape(6.sdp))
                            )
                            
                            Text(
                                text = userData.userInfo?.profile?.realName?.takeIf { it.isNotBlank() }
                                    ?: selectedUser,
                                style = androidx.compose.ui.text.TextStyle(
                                    fontSize = 14.sp,
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(start = 8.sdp),
                                maxLines = 1
                            )
                        }
                    } else {
                        Text(
                            text = selectedUser ?: "Select ${label.lowercase()}",
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize = 14.sp,
                                color = if (selectedUser != null) Color.White else Color.White.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier.weight(1f),
                            maxLines = 1
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.sdp)
                    )
                }
            }

            // Dropdown menu
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(colorResource(R.color.card_elevated))
                    .border(
                        width = 1.sdp,
                        color = Color.White.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.sdp)
                    )
            ) {
                filteredUsers.forEach { username ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = username,
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        },
                        onClick = {
                            onUserSelected(username)
                            expanded = false
                        },
                        modifier = Modifier.padding(horizontal = 4.sdp)
                    )
                }
            }
        }

        // Loading indicator
        if (userData?.isLoading == true) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.sdp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.sdp),
                    color = colorResource(R.color.white),
                    strokeWidth = 2.sdp
                )
            }
        }
    }
}
