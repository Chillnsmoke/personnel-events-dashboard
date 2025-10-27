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
import com.example.personneleventsdashboard.ui.theme.Forest
import com.example.personneleventsdashboard.ui.theme.Orange

@OptIn(ExperimentalMaterial3Api::class)
// Update ShopManagementDialog to use the simplified position system:

@Composable
fun ShopManagementDialog(
    allShops: List<Shop>,
    onDismiss: () -> Unit,
    onAddShop: (Shop) -> Unit,
    onUpdateShop: (Shop) -> Unit,
    onDeleteShop: (Shop) -> Unit
) {
    var showAddShopDialog by remember { mutableStateOf(false) }

    val activeShops = allShops.filter { it.isActive }
    val availableShops = activeShops.map { it.name }
    val noneOption = "None"
    val shopOptions = listOf(noneOption) + availableShops

    // Get shop at specific position
    fun getShopAtPosition(position: Int): String {
        return activeShops.find { it.displayPosition == position }?.name ?: noneOption
    }

    // Update shop position
    fun updateShopPosition(shopName: String, newPosition: Int) {
        if (shopName == noneOption) {
            // Remove any shop from this position
            val existingShop = activeShops.find { it.displayPosition == newPosition }
            existingShop?.let { shop ->
                val updatedShop = shop.copy(displayPosition = null)
                onUpdateShop(updatedShop)
            }
        } else {
            // Find the shop to move
            val shopToMove = activeShops.find { it.name == shopName }
            shopToMove?.let { shop ->
                val updatedShop = shop.copy(displayPosition = newPosition)
                onUpdateShop(updatedShop)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Shop Layout Management",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Charcoal
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .width(800.dp)
                    .height(600.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Add New Shop Button
                OutlinedButton(
                    onClick = { showAddShopDialog = true },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Forest.copy(alpha = 0.1f),
                        contentColor = Forest
                    ),
                    border = BorderStroke(2.dp, Forest),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Add New Shop", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Divider()

                Text(
                    "Shop Layout Configuration",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Row 1: Position 1
                        Text("Row 1:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        ShopPositionDropdown(
                            label = "Position 1",
                            currentShop = getShopAtPosition(1),
                            shopOptions = shopOptions,
                            onShopSelected = { shopName ->
                                updateShopPosition(shopName, 1)
                            }
                        )

                        // Row 2: Positions 2-4
                        Text("Row 2:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            (2..4).forEach { position ->
                                ShopPositionDropdown(
                                    label = "Position ${position - 1}",
                                    currentShop = getShopAtPosition(position),
                                    shopOptions = shopOptions,
                                    onShopSelected = { shopName ->
                                        updateShopPosition(shopName, position)
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        // Row 3: Positions 5-9
                        Text("Row 3:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            (5..9).forEach { position ->
                                ShopPositionDropdown(
                                    label = "Pos ${position - 4}",
                                    currentShop = getShopAtPosition(position),
                                    shopOptions = shopOptions,
                                    onShopSelected = { shopName ->
                                        updateShopPosition(shopName, position)
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Divider()

                        // Columns: Positions 10-34 (5 columns × 5 positions)
                        Text("Operational Columns:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            (0..4).forEach { columnIndex ->
                                val startPosition = 10 + (columnIndex * 5)

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        "Col ${columnIndex + 1}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    (startPosition until startPosition + 5).forEach { position ->
                                        ShopPositionDropdown(
                                            label = "${position - startPosition + 1}",
                                            currentShop = getShopAtPosition(position),
                                            shopOptions = shopOptions,
                                            onShopSelected = { shopName ->
                                                updateShopPosition(shopName, position)
                                            },
                                            isCompact = true
                                        )
                                    }
                                }
                            }
                        }

                        // Unassigned Shops
                        val unassignedShops = activeShops.filter { it.displayPosition == null }

                        if (unassignedShops.isNotEmpty()) {
                            Divider()
                            Text(
                                "Unassigned Shops:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Orange
                            )
                            unassignedShops.forEach { shop ->
                                Text("• ${shop.name}", fontSize = 14.sp, color = Orange)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
            ) {
                Text("Close")
            }
        },
        dismissButton = {}
    )

    // Add Shop Dialog
    if (showAddShopDialog) {
        AddShopDialog(
            onDismiss = { showAddShopDialog = false },
            onConfirm = { shop ->
                onAddShop(shop)
                showAddShopDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopPositionDropdown(
    label: String,
    currentShop: String,
    shopOptions: List<String>,
    onShopSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isCompact: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        if (!isCompact) {
            Text(
                label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = currentShop,
                onValueChange = { },
                readOnly = true,
                label = if (isCompact) { { Text(label, fontSize = 10.sp) } } else null,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .height(if (isCompact) 48.dp else 56.dp),
                textStyle = if (isCompact)
                    MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp)
                else
                    MaterialTheme.typography.bodyMedium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = if (currentShop == "None")
                        Color.LightGray.copy(alpha = 0.1f)
                    else
                        DarkBlue.copy(alpha = 0.1f)
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                shopOptions.forEach { shop ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                shop,
                                fontSize = if (isCompact) 11.sp else 14.sp
                            )
                        },
                        onClick = {
                            onShopSelected(shop)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}