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
import com.example.personneleventsdashboard.model.Shop
import com.example.personneleventsdashboard.ui.theme.Charcoal
import com.example.personneleventsdashboard.ui.theme.Forest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddShopDialog(
    onDismiss: () -> Unit,
    onConfirm: (Shop) -> Unit
) {
    var shopName by remember { mutableStateOf("") }
    var shopChief by remember { mutableStateOf("") }
    var shopColor by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Add New Shop",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Charcoal
            )
        },
        text = {
            Column(
                modifier = Modifier.width(400.dp),
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

                // Shop Chief (Optional)
                OutlinedTextField(
                    value = shopChief,
                    onValueChange = { shopChief = it },
                    label = { Text("Shop Chief (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Shop Color (Optional)
                OutlinedTextField(
                    value = shopColor,
                    onValueChange = { shopColor = it },
                    label = { Text("Color (Optional, e.g., #FF5722)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("#FF5722") }
                )

                // Info card about positioning
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Blue.copy(alpha = 0.1f)
                    )
                ) {
                    Text(
                        "After creating the shop, use the Layout Configuration section above to assign it to a position on the roster display.",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        },
        confirmButton = {
            OutlinedButton(
                onClick = {
                    if (shopName.isNotBlank()) {
                        val newShop = Shop(
                            name = shopName.trim(),
                            chief = if (shopChief.isBlank()) null else shopChief.trim(),
                            color = if (shopColor.isBlank()) null else shopColor.trim(),
                            displayPosition = null, // Will be assigned later via layout management
                            isActive = true
                        )
                        onConfirm(newShop)
                    }
                },
                enabled = shopName.isNotBlank(),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Forest.copy(alpha = 0.1f),
                    contentColor = Forest
                ),
                border = BorderStroke(2.dp, Forest)
            ) {
                Text("Add Shop", fontWeight = FontWeight.Bold)
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
}