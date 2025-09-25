package com.example.personneleventsdashboard.ui.components.management

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personneleventsdashboard.model.TailNumber
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import com.example.personneleventsdashboard.ui.theme.Orange

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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Tail Number Management",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .width(350.dp)
                    .height(480.dp)
            ) {
                // Add button
                OutlinedButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add New Tail Number", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // List of tail numbers
                LazyColumn(
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
        },
        confirmButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        dismissButton = {}
    )

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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Tail Number", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.width(300.dp),
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
                    placeholder = { Text("") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    supportingText = { Text("Enter 4-digit tail number") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(number) }, // Just pass the number
                enabled = isValid,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green.copy(alpha = 0.8f))
            ) { Text("Add") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun EditTailNumberDialog(
    tailNumber: TailNumber,
    onDismiss: () -> Unit,
    onSave: (TailNumber) -> Unit
) {
    var number by remember { mutableStateOf(tailNumber.number) }

    val isValid = number.trim().length == 4 && number.trim().all { it.isDigit() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Tail Number", fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = number,
                onValueChange = { input ->
                    if (input.all { it.isDigit() } && input.length <= 4) {
                        number = input
                    }
                },
                label = { Text("Tail Number") },
                modifier = Modifier.width(300.dp),
                singleLine = true,
                supportingText = { Text("Enter 4-digit tail number") }
            )
        },
        confirmButton = {
            Button(
                onClick = { onSave(tailNumber.copy(number = number.trim())) },
                enabled = isValid,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue.copy(alpha = 0.8f))
            ) { Text("Save") }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun TailNumberItem(
    tailNumber: TailNumber,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.Blue)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                tailNumber.number,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onEdit,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue)
                ) { Text("Edit") }

                OutlinedButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) { Text("Delete") }
            }
        }
    }
}