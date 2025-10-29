package com.example.personneleventsdashboard.ui.components.management

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.personneleventsdashboard.model.Shop
import com.example.personneleventsdashboard.ui.theme.Charcoal
import com.example.personneleventsdashboard.ui.theme.DarkBlue
import com.example.personneleventsdashboard.ui.theme.Forest
import com.example.personneleventsdashboard.ui.theme.Orange
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Popup
import com.example.personneleventsdashboard.model.Person


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopManagementDialog(
    allShops: List<Shop>,
    allPersonnel: List<Person>,
    onDismiss: () -> Unit,
    onAddShop: (Shop) -> Unit,
    onUpdateShop: (Shop) -> Unit,
    onDeleteShop: (Shop) -> Unit
) {
    var showAddShopDialog by remember { mutableStateOf(false) }
    var showEditShopDialog by remember { mutableStateOf(false) }

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

//    // Add this debugging function
//    LaunchedEffect(allShops) {
//        println("=== SHOP DEBUG ===")
//        allShops.forEach { shop ->
//            println("${shop.name}: active=${shop.isActive}, pos=${shop.displayPosition}")
//        }
//        println("==================")
//    }
//
//    LaunchedEffect(Unit) {
//        // One-time cleanup of the broken "Testes 2 Bitches" entries
//        val brokenShops = allShops.filter { !it.isActive && it.displayPosition != null }
//        brokenShops.forEach { brokenShop ->
//            println("Cleaning up: ${brokenShop.name}")
//            onDeleteShop(brokenShop) // Hard delete
//        }
//    }

    // Using Dialog instead of AlertDialog for full width control
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
                .width(900.dp)
                .height(740.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black),
            border = BorderStroke(2.dp, Color.Gray)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Scrollable content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Shop Layout Configuration",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.LightGray
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.LightGray.copy(alpha = 0.2f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)  // Reduced from 16.dp to 8.dp
                        ) {
                            // Row 1: Position 1
                            Text("Row 1:", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                ShopPositionDropdown(
                                    label = "",
                                    currentShop = getShopAtPosition(1),
                                    shopOptions = shopOptions,
                                    onShopSelected = { shopName ->
                                        updateShopPosition(shopName, 1)
                                    },
                                    modifier = Modifier
                                        .width(150.dp)
                                        .padding(0.dp),  // Uniform width
                                    isCompact = true  // Use integrated label style
                                )
                            }

                            // Row 2: Positions 2-4
                            Text("Row 2:", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Spacer(Modifier.width(145.dp))
                                (2..4).forEach { position ->
                                    ShopPositionDropdown(
                                        label = "",
                                        currentShop = getShopAtPosition(position),
                                        shopOptions = shopOptions,
                                        onShopSelected = { shopName ->
                                            updateShopPosition(shopName, position)
                                        },
                                        modifier = Modifier
                                            .width(150.dp)
                                            .padding(0.dp),  // Uniform width
                                        isCompact = true  // Use integrated label style
                                    )
                                }
                                Spacer(Modifier.width(145.dp))
                            }

                            // Row 3: Positions 5-9 - Restructured as 5 separate columns for perfect alignment
                            Text("Row 3:", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly  // Match operational columns spacing
                            ) {
                                (5..9).forEach { position ->
                                    Column(
                                        modifier = Modifier.weight(1f),  // Each column takes equal space
                                        horizontalAlignment = Alignment.CenterHorizontally  // Center dropdown in column
                                    ) {
                                        ShopPositionDropdown(
                                            label = "",
                                            currentShop = getShopAtPosition(position),
                                            shopOptions = shopOptions,
                                            onShopSelected = { shopName ->
                                                updateShopPosition(shopName, position)
                                            },
                                            modifier = Modifier
                                                .width(150.dp)
                                                .padding(0.dp),  // Uniform width
                                            isCompact = true  // Use integrated label style
                                        )
                                    }
                                }
                            }
                            // Columns: Positions 10-34 (5 columns × 5 positions)
                            Text("Main Shops:", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                (0..4).forEach { columnIndex ->
                                    val startPosition = 10 + (columnIndex * 5)

                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(4.dp),  // Reduced spacing
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        (startPosition until startPosition + 5).forEach { position ->
                                            ShopPositionDropdown(
                                                label = "",
                                                currentShop = getShopAtPosition(position),
                                                shopOptions = shopOptions,
                                                onShopSelected = { shopName ->
                                                    updateShopPosition(shopName, position)
                                                },
                                                modifier = Modifier
                                                    .width(150.dp)
                                                    .padding(0.dp),
                                                isCompact = true,
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
                                    color = Orange.copy(alpha = 0.4f)
                                )
                                unassignedShops.forEach { shop ->
                                    Text("• ${shop.name}", fontSize = 14.sp, color = Orange.copy(alpha = 0.4f))
                                }
                            }
                        }
                    }
                }

                // Bottom button row with three buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Add New Shop button (left)
                    OutlinedButton(
                        onClick = { showAddShopDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.LightGray.copy(alpha = 0.4f),
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text("Add New Shop", fontWeight = FontWeight.Bold)
                    }

                    // Edit Shop button (middle)
                    OutlinedButton(
                        onClick = { showEditShopDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.LightGray.copy(alpha = 0.4f),
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text("Edit Shop", fontWeight = FontWeight.Bold)
                    }

                    // Close button (right)
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.width(150.dp)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }

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

    // Edit Shop Dialog (placeholder for now)
    if (showEditShopDialog) {
        EditShopDialog(
            allShops = allShops,
            allPersonnel = allPersonnel, // List<Person> from your database
            onDismiss = { showEditShopDialog = false },
            onUpdate = onUpdateShop,
            onDelete = onDeleteShop
        )
    }
}

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

        Box {
            // Your exact visual styling - this stays in layout
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isCompact) 48.dp else 56.dp)
                    .border(1.5.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .clickable { expanded = !expanded }
                    .padding(0.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentShop,
                    fontSize = if (isCompact) 12.sp else 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (currentShop == "None") Color.Gray else Color.LightGray,
                    textAlign = TextAlign.Center
                )
            }

            // POPUP DROPDOWN - Renders outside layout flow!
            if (expanded) {
                Popup(
                    alignment = Alignment.TopStart,
                    offset = IntOffset(0, if (isCompact) 48 else 56), // Position below the box
                    onDismissRequest = { expanded = false }
                ) {
                    Card(
                        modifier = Modifier
                            .width(150.dp)  // Match your dropdown width
                            .heightIn(max = 200.dp),  // Limit height for scrolling
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        LazyColumn {
                            items(shopOptions) { shop ->
                                Text(
                                    text = shop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onShopSelected(shop)
                                            expanded = false
                                        }
                                        .padding(12.dp),
                                    fontSize = if (isCompact) 11.sp else 14.sp,
                                    color = Color.Black
                                )
                                if (shop != shopOptions.last()) {
                                    Divider(thickness = 0.5.dp, color = Color.LightGray)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}