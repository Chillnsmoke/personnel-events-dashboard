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
import com.example.personneleventsdashboard.model.Shop
import com.example.personneleventsdashboard.ui.theme.Charcoal
import com.example.personneleventsdashboard.ui.theme.DarkBlue
import com.example.personneleventsdashboard.ui.theme.Orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditShopDialog(
    shop: Shop,
    allShops: List<Shop>,
    onDismiss: () -> Unit,
    onUpdate: (Shop) -> Unit,
    onDelete: (Shop) -> Unit
) {
    var shopName by remember { mutableStateOf(shop.name) }
    var shopChief by remember { mutableStateOf(shop.chief ?: "") }
    var shopColor by remember { mutableStateOf(shop.color ?: "") }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    // Check if shop has personnel assigned (you can implement this check if needed)
    val hasPersonnel = remember { false } // Placeholder

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Edit Shop",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Charcoal
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .width(400.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Shop Name
                OutlinedTextField(
                    value = shopName,
                    onValueChange = { shopName = it },
                    label = { Text("Shop Name *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Shop Chief
                OutlinedTextField(
                    value = shopChief,
                    onValueChange = { shopChief = it },
                    label = { Text("Shop Chief (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Shop Color
                OutlinedTextField(
                    value = shopColor,
                    onValueChange = { shopColor = it },
                    label = { Text("Color (Optional, e.g., #FF5722)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("#FF5722") }
                )

                // Current Position Display
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Blue.copy(alpha = 0.1f)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            "Current Position:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            if (shop.displayPosition != null)
                                "Position ${shop.displayPosition}"
                            else
                                "Not assigned to layout",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            "Use the Layout Configuration section above to change position.",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                Divider()

                // Delete Shop Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Red.copy(alpha = 0.1f)
                    ),
                    border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Danger Zone",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )

                        OutlinedButton(
                            onClick = { showDeleteConfirmation = true },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red
                            ),
                            border = BorderStroke(1.dp, Color.Red),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Delete Shop", fontWeight = FontWeight.Bold)
                        }

                        if (hasPersonnel) {
                            Text(
                                "⚠️ This shop has personnel assigned. Deleting will unassign them.",
                                fontSize = 11.sp,
                                color = Color.Red
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            OutlinedButton(
                onClick = {
                    if (shopName.isNotBlank()) {
                        val updatedShop = shop.copy(
                            name = shopName.trim(),
                            chief = if (shopChief.isBlank()) null else shopChief.trim(),
                            color = if (shopColor.isBlank()) null else shopColor.trim()
                            // displayPosition remains unchanged - managed via layout section
                        )
                        onUpdate(updatedShop)
                    }
                },
                enabled = shopName.isNotBlank(),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = DarkBlue.copy(alpha = 0.1f),
                    contentColor = DarkBlue
                ),
                border = BorderStroke(2.dp, DarkBlue)
            ) {
                Text("Update Shop", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Gray
                )
            ) {
                Text("Cancel")
            }
        }
    )

    // Delete Confirmation Dialog
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Confirm Delete") },
            text = {
                Text("Are you sure you want to delete \"${shop.name}\"? This action cannot be undone.")
            },
            confirmButton = {
                OutlinedButton(
                    onClick = {
                        onDelete(shop)
                        showDeleteConfirmation = false
                    },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red
                    ),
                    border = BorderStroke(1.dp, Color.Red)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteConfirmation = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}