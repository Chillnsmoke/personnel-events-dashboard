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
import com.example.personneleventsdashboard.model.Shop

@Composable
fun AddShopDialog(
    onDismiss: () -> Unit,
    onConfirm: (Shop) -> Unit
) {
    var shopName by remember { mutableStateOf("") }

    // Using Dialog instead of AlertDialog for full width control - matches ShopManagementDialog
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
                .height(320.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),  // Same as ShopManagementDialog
            colors = CardDefaults.cardColors(containerColor = Color.Black),  // Same black background
            border = BorderStroke(2.dp, Color.Gray)  // Same border style
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),  // Same padding as ShopManagementDialog
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title - matches ShopManagementDialog text styling
                Text(
                    "Add New Shop",
                    fontSize = 18.sp,  // Same as section headers in ShopManagementDialog
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray  // Same gray color for headers
                )

                // Content area with same Card styling as ShopManagementDialog
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray.copy(alpha = 0.1f)  // Same as layout config card
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),  // Same padding as inner cards
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Shop Name Input
                        Text(
                            "Shop Name:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White  // White text on black background
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
                                    "Enter shop name...",
                                    color = Color.Gray
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))  // Push buttons to bottom

                // Bottom button row - matches ShopManagementDialog exactly
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Cancel button (left) - matches Close button styling
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.width(150.dp)  // Same width as ShopManagementDialog buttons
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.weight(1f))  // Space between buttons

                    // Add Shop button (right) - matches Add New Shop button styling
                    OutlinedButton(
                        onClick = {
                            if (shopName.trim().isNotEmpty()) {
                                val newShop = Shop(
                                    shopId = 0, // Will be auto-generated by database
                                    name = shopName.trim(),
                                    chief = null,  // No shop chief field as requested
                                    color = null,      // No color field as requested
                                    displayPosition = null,  // Unassigned initially
                                    isActive = true
                                )
                                onConfirm(newShop)
                            }
                        },
                        enabled = shopName.trim().isNotEmpty(),  // Only enable if name is entered
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.LightGray.copy(alpha = 0.4f),
                            contentColor = Color.Black,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.2f),
                            disabledContentColor = Color.Gray
                        ),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text("Add Shop", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}