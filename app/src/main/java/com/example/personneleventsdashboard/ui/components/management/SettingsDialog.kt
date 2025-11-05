package com.example.personneleventsdashboard.ui.components.management

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onAddPerson: () -> Unit,
    onEditPerson: () -> Unit,
    onManageAircraft: () -> Unit,
    onManageShops: () -> Unit,
    // Future functions - implemented as placeholders
    onManageEvents: (() -> Unit)? = null,
    onBackupRestore: (() -> Unit)? = null,
    onThemeSettings: (() -> Unit)? = null
) {
    // Using Dialog instead of AlertDialog for full width control - matches other dialogs
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false  // This removes ALL width constraints!
        )
    ) {
        Card(
            modifier = Modifier
                .width(400.dp)
                .height(800.dp),  // Taller for more content
            shape = RoundedCornerShape(16.dp),  // Same as other dialogs
            colors = CardDefaults.cardColors(containerColor = Color.Black),  // Same black background
            border = BorderStroke(2.dp, Color.Gray)  // Same border style
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),  // Same padding as other dialogs
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title - matches other dialogs
                Text(
                    "Settings & Management",
                    fontSize = 18.sp,  // Same as other dialog titles
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray  // Same gray color for headers
                )

                // Scrollable content area
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Personnel Management Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.LightGray.copy(alpha = 0.1f)  // Same as other dialogs
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "Personnel & Shops",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            // Add Person Button
                            OutlinedButton(
                                onClick = onAddPerson,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.LightGray.copy(alpha = 0.4f),
                                    contentColor = Color.Black
                                ),
                                border = BorderStroke(2.dp, Color.Gray)
                            ) {
                                Text("Add Person", fontWeight = FontWeight.Bold)
                            }

                            // Edit Person Button
                            OutlinedButton(
                                onClick = onEditPerson,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.LightGray.copy(alpha = 0.4f),
                                    contentColor = Color.Black
                                ),
                                border = BorderStroke(2.dp, Color.Gray)
                            ) {
                                Text("Edit Person", fontWeight = FontWeight.Bold)
                            }

                            // Manage Shops Button
                            OutlinedButton(
                                onClick = onManageShops,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.LightGray.copy(alpha = 0.4f),
                                    contentColor = Color.Black
                                ),
                                border = BorderStroke(2.dp, Color.Gray)
                            ) {
                                Text("Manage Shops", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Aircraft Management Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.LightGray.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "Calendar Management",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            OutlinedButton(
                                onClick = onManageAircraft,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.LightGray.copy(alpha = 0.4f),
                                    contentColor = Color.Black
                                ),
                                border = BorderStroke(2.dp, Color.Gray)
                            ) {
                                Text("Manage Aircraft", fontWeight = FontWeight.Bold)
                            }

                            // Event Management Button (placeholder)
                            OutlinedButton(
                                onClick = {
                                    onManageEvents?.invoke() ?: run {
                                        // Placeholder action - could show toast or do nothing
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = onManageEvents != null,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (onManageEvents != null) Color.Blue.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f),
                                    contentColor = if (onManageEvents != null) Color.Blue else Color.Gray,
                                    disabledContainerColor = Color.Gray.copy(alpha = 0.2f),
                                    disabledContentColor = Color.Gray
                                ),
                                border = BorderStroke(2.dp, if (onManageEvents != null) Color.Blue else Color.Gray)
                            ) {
                                Text("Event Management", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // System Management Section (New placeholder buttons)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.LightGray.copy(alpha = 0.1f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "System Management",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            // Theme Settings Button (placeholder)
                            OutlinedButton(
                                onClick = {
                                    onThemeSettings?.invoke() ?: run {
                                        // Placeholder action
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = onThemeSettings != null,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (onThemeSettings != null) Color.Magenta.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f),
                                    contentColor = if (onThemeSettings != null) Color.Magenta else Color.Gray,
                                    disabledContainerColor = Color.Gray.copy(alpha = 0.2f),
                                    disabledContentColor = Color.Gray
                                ),
                                border = BorderStroke(2.dp, if (onThemeSettings != null) Color.Magenta else Color.Gray)
                            ) {
                                Text("Theme Settings", fontWeight = FontWeight.Bold)
                            }

                            // Backup/Restore Button (placeholder)
                            OutlinedButton(
                                onClick = {
                                    onBackupRestore?.invoke() ?: run {
                                        // Placeholder action
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = onBackupRestore != null,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (onBackupRestore != null) Color.Red.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f),
                                    contentColor = if (onBackupRestore != null) Color.Red else Color.Gray,
                                    disabledContainerColor = Color.Gray.copy(alpha = 0.2f),
                                    disabledContentColor = Color.Gray
                                ),
                                border = BorderStroke(2.dp, if (onBackupRestore != null) Color.Red else Color.Gray)
                            ) {
                                Text("Backup/Restore", fontWeight = FontWeight.Bold)
                            }

                            // Coming Soon indicator for disabled buttons
                            if (onManageEvents == null || onThemeSettings == null || onBackupRestore == null) {
                                Text(
                                    "🚧 Some features coming soon",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                }

                // Bottom Close button - matches other dialogs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.width(150.dp)  // Same width as other dialog buttons
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}