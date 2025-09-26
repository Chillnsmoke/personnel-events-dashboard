package com.example.personneleventsdashboard.ui.components.management

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personneleventsdashboard.ui.theme.Charcoal
import com.example.personneleventsdashboard.ui.theme.DarkBlue
import com.example.personneleventsdashboard.ui.theme.Forest
import com.example.personneleventsdashboard.ui.theme.Orange

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    onAddPerson: () -> Unit,
    onEditPerson: () -> Unit,
    onManageAircraft: () -> Unit,
    // Future functions - commented out for now
    // onManageShops: () -> Unit,
    // onManageEvents: () -> Unit,
    // onBackupRestore: () -> Unit,
    // onThemeSettings: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Settings & Management",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Charcoal
            )
        },
        text = {
            Column(
                modifier = Modifier.width(350.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Personnel Management",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                // Add Person Button
                OutlinedButton(
                    onClick = onAddPerson,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    border = BorderStroke(2.dp, Charcoal),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Add Person",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                // Edit Person Button
                OutlinedButton(
                    onClick = onEditPerson,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    border = BorderStroke(2.dp, Charcoal),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Edit Person",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Divider()

                Text(
                    "Aircraft Management",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                // Aircraft Management Button
                OutlinedButton(
                    onClick = onManageAircraft,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    border = BorderStroke(2.dp, Charcoal),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Manage Aircraft",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Divider()

                // Future Features Section (commented out for now)
                /*
                Text(
                    "System Management",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Shop Management Button
                    OutlinedButton(
                        onClick = {
                            onManageShops()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Purple.copy(alpha = 0.1f),
                            contentColor = Color.Purple
                        ),
                        border = BorderStroke(2.dp, Color.Purple),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🏪", fontSize = 20.sp)
                            Text(
                                "Manage Shops",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Event Management Button
                    OutlinedButton(
                        onClick = {
                            onManageEvents()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Cyan.copy(alpha = 0.1f),
                            contentColor = Color.Cyan
                        ),
                        border = BorderStroke(2.dp, Color.Cyan),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📅", fontSize = 20.sp)
                            Text(
                                "Manage Events",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Backup/Restore Button
                    OutlinedButton(
                        onClick = {
                            onBackupRestore()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Red.copy(alpha = 0.1f),
                            contentColor = Color.Red
                        ),
                        border = BorderStroke(2.dp, Color.Red),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("💾", fontSize = 20.sp)
                            Text(
                                "Backup/Restore",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Theme Settings Button
                    OutlinedButton(
                        onClick = {
                            onThemeSettings()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Magenta.copy(alpha = 0.1f),
                            contentColor = Color.Magenta
                        ),
                        border = BorderStroke(2.dp, Color.Magenta),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🎨", fontSize = 20.sp)
                            Text(
                                "Theme Settings",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                */

                // Placeholder for future features
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray.copy(alpha = 0.1f)
                    ),
                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "🚧 Coming Soon 🚧",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(
                            "Shop Management • Event Management",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            "Backup/Restore • Theme Settings",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        },
        confirmButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Gray
                )
            ) {
                Text("Close")
            }
        },
        dismissButton = {}
    )
}