package com.example.personneleventsdashboard.ui.components.management

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.personneleventsdashboard.model.Person
import com.example.personneleventsdashboard.model.Shop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditShopDialog(
    allShops: List<Shop>,
    allPersonnel: List<Person>, // Add this parameter to check personnel assignments
    onDismiss: () -> Unit,
    onUpdate: (Shop) -> Unit,
    onDelete: (Shop) -> Unit
) {
    var selectedShop by remember { mutableStateOf<Shop?>(null) }
    var shopName by remember { mutableStateOf("") }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var shopDropdownExpanded by remember { mutableStateOf(false) }

    val activeShops = allShops.filter { it.isActive }

    // Check if selected shop has personnel assigned
    val hasPersonnelAssigned = selectedShop?.let { shop ->
        allPersonnel.any { person -> person.shopId == shop.shopId }
    } ?: false

    // Update shop name when shop is selected
    LaunchedEffect(selectedShop) {
        shopName = selectedShop?.name ?: ""
    }

    // Using Dialog instead of AlertDialog for full width control - matches AddShopDialog
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
                .width(450.dp)
                .height(420.dp)  // Slightly taller than AddShopDialog for additional fields
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),  // Same as AddShopDialog
            colors = CardDefaults.cardColors(containerColor = Color.Black),  // Same black background
            border = BorderStroke(2.dp, Color.Gray)  // Same border style
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),  // Same padding as AddShopDialog
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title - matches AddShopDialog text styling
                Text(
                    "Edit Shop",
                    fontSize = 18.sp,  // Same as AddShopDialog
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray  // Same gray color for headers
                )

                // Content area with same Card styling as AddShopDialog
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray.copy(alpha = 0.1f)  // Same as AddShopDialog
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),  // Same padding as AddShopDialog
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Shop Selection Dropdown
                        Text(
                            "Select Shop to Edit:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White  // White text on black background
                        )

                        ExposedDropdownMenuBox(
                            expanded = shopDropdownExpanded,
                            onExpandedChange = { shopDropdownExpanded = !shopDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedShop?.name ?: "Select a shop...",
                                onValueChange = { },
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = shopDropdownExpanded) },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color.Gray,
                                    unfocusedBorderColor = Color.Gray,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = shopDropdownExpanded,
                                onDismissRequest = { shopDropdownExpanded = false }
                            ) {
                                activeShops.forEach { shop ->
                                    DropdownMenuItem(
                                        text = { Text(shop.name) },
                                        onClick = {
                                            selectedShop = shop
                                            shopDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Shop Name Input (only shown if shop is selected)
                        selectedShop?.let {
                            Text(
                                "Rename Shop:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )

                            OutlinedTextField(
                                value = shopName,
                                onValueChange = { shopName = it },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color.Gray,
                                    unfocusedBorderColor = Color.Gray,
                                    cursorColor = Color.White,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent
                                ),
                                placeholder = {
                                    Text(
                                        "Enter new shop name...",
                                        color = Color.Gray
                                    )
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))  // Push buttons to bottom

                // Bottom button row - matches AddShopDialog exactly
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Cancel button (left)
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    // Delete button (center) - only enabled if shop selected and no personnel
                    OutlinedButton(
                        onClick = { showDeleteConfirmation = true },
                        enabled = selectedShop != null && !hasPersonnelAssigned,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (hasPersonnelAssigned) Color.Gray.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.1f),
                            contentColor = if (hasPersonnelAssigned) Color.Gray else Color.Red,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.2f),
                            disabledContentColor = Color.Gray
                        ),
                        border = BorderStroke(2.dp, if (hasPersonnelAssigned) Color.Gray else Color.Red),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Delete", fontWeight = FontWeight.Bold)
                    }

                    // Update button (right)
                    OutlinedButton(
                        onClick = {
                            selectedShop?.let { shop ->
                                if (shopName.trim().isNotEmpty() && shopName.trim() != shop.name) {
                                    val updatedShop = shop.copy(name = shopName.trim())
                                    onUpdate(updatedShop)
                                    onDismiss()
                                }
                            }
                        },
                        enabled = selectedShop != null && shopName.trim().isNotEmpty() && shopName.trim() != selectedShop?.name,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.LightGray.copy(alpha = 0.4f),
                            contentColor = Color.Black,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.2f),
                            disabledContentColor = Color.Gray
                        ),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Update", fontWeight = FontWeight.Bold)
                    }
                }

                // Warning text for personnel assignment
                selectedShop?.let {
                    if (hasPersonnelAssigned) {
                        Text(
                            "⚠️ Cannot delete: This shop has personnel assigned",
                            fontSize = 12.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmation && selectedShop != null) {
        Dialog(
            onDismissRequest = { showDeleteConfirmation = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Card(
                modifier = Modifier
                    .width(400.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                border = BorderStroke(2.dp, Color.Red)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Confirm Delete",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )

                    Text(
                        "Are you sure you want to delete \"${selectedShop!!.name}\"?\n\nThis action cannot be undone.",
                        fontSize = 14.sp,
                        color = Color.White
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showDeleteConfirmation = false },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                            border = BorderStroke(2.dp, Color.Gray),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }

                        OutlinedButton(
                            onClick = {
                                selectedShop?.let { onDelete(it) }
                                showDeleteConfirmation = false
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Red.copy(alpha = 0.2f),
                                contentColor = Color.Red
                            ),
                            border = BorderStroke(2.dp, Color.Red),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Delete", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}