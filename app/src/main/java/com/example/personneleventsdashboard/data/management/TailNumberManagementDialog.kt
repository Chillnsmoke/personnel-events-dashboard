package com.example.personneleventsdashboard.ui.components.management

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.personneleventsdashboard.model.TailNumber

@Composable
fun TailNumberManagementDialog(
    tailNumbers: List<TailNumber>,
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit, // Removed notes parameter
    onEdit: (TailNumber) -> Unit,
    onDelete: (TailNumber) -> Unit
    // Removed onToggleActive parameter
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingTailNumber by remember { mutableStateOf<TailNumber?>(null) }

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
                .width(450.dp)  // Same width as other dialogs
                .height(700.dp), // Tall enough for list and controls
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
                    "Tail Number Management",
                    fontSize = 18.sp,  // Same as other dialog titles
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray  // Same gray color for headers
                )

                // Add button section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray.copy(alpha = 0.1f)  // Same as other dialogs
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showAddDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.LightGray.copy(alpha = 0.2f),
                                contentColor = Color.White
                            ),
                            border = BorderStroke(2.dp, Color.Gray)
                        ) {
                            Text("Add New Tail Number", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // List section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),  // Take remaining space
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray.copy(alpha = 0.1f)  // Same as other dialogs
                    )
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tailNumbers.sortedBy { it.number }) { tailNumber ->
                            TailNumberItem(
                                tailNumber = tailNumber,
                                onEdit = { editingTailNumber = tailNumber },
                                onDelete = { onDelete(tailNumber) }
                                // Removed onToggleActive
                            )
                        }
                    }
                }

                // Bottom button row - matches other dialogs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Close button - same styling as other dialogs
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text("Close", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Add tail number dialog
    if (showAddDialog) {
        AddTailNumberDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { number -> // Removed notes parameter
                onAdd(number)
                showAddDialog = false
            }
        )
    }

    // Edit tail number dialog
    editingTailNumber?.let { tailNumber ->
        EditTailNumberDialog(
            tailNumber = tailNumber,
            onDismiss = { editingTailNumber = null },
            onSave = { updated ->
                onEdit(updated)
                editingTailNumber = null
            }
        )
    }
}

@Composable
fun AddTailNumberDialog(
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit // Remove notes parameter
) {
    var number by remember { mutableStateOf("") }

    val isValid = number.trim().length == 4 && number.trim().all { it.isDigit() }

    // Using Dialog instead of AlertDialog - matches other dialogs
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .width(400.dp)
                .height(280.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black),
            border = BorderStroke(2.dp, Color.Gray)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    "Add New Tail Number",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                // Content area
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
                        OutlinedTextField(
                            value = number,
                            onValueChange = { input ->
                                if (input.all { it.isDigit() } && input.length <= 4) {
                                    number = input
                                }
                            },
                            label = { Text("Tail Number") },
                            placeholder = { Text("1234", color = Color.Gray) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.Gray,
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = Color.Gray,
                                unfocusedLabelColor = Color.Gray,
                                cursorColor = Color.White,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            supportingText = {
                                Text("Enter 4-digit tail number", color = Color.Gray)
                            }
                        )
                    }
                }

                // Bottom buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { onAdd(number) },
                        enabled = isValid,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.LightGray.copy(alpha = 0.2f),
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.2f),
                            disabledContentColor = Color.Gray
                        ),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun EditTailNumberDialog(
    tailNumber: TailNumber,
    onDismiss: () -> Unit,
    onSave: (TailNumber) -> Unit
) {
    var number by remember { mutableStateOf(tailNumber.number) }

    val isValid = number.trim().length == 4 && number.trim().all { it.isDigit() }

    // Using Dialog instead of AlertDialog - matches other dialogs
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .width(400.dp)
                .height(280.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black),
            border = BorderStroke(2.dp, Color.Gray)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    "Edit Tail Number",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                // Content area
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = number,
                            onValueChange = { input ->
                                if (input.all { it.isDigit() } && input.length <= 4) {
                                    number = input
                                }
                            },
                            label = { Text("Tail Number") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color.Gray,
                                unfocusedBorderColor = Color.Gray,
                                focusedLabelColor = Color.Gray,
                                unfocusedLabelColor = Color.Gray,
                                cursorColor = Color.White,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            ),
                            supportingText = {
                                Text("Enter 4-digit tail number", color = Color.Gray)
                            }
                        )
                    }
                }

                // Bottom buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { onSave(tailNumber.copy(number = number.trim())) },
                        enabled = isValid,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.LightGray.copy(alpha = 0.2f),
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.2f),
                            disabledContentColor = Color.Gray
                        ),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Save", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun TailNumberItem(
    tailNumber: TailNumber,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.3f)  // Dark background for items
        ),
        border = BorderStroke(1.dp, Color.Gray)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                tailNumber.number,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.White  // White text on dark background
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onEdit,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.LightGray,
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("Edit", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red.copy(alpha = 0.8f),
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.8f))
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}