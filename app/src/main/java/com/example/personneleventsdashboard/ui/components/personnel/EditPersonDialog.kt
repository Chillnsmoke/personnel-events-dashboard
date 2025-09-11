package com.example.personneleventsdashboard.ui.components.personnel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personneleventsdashboard.model.Person
import com.example.personneleventsdashboard.model.Shop

@Composable
fun EditPersonDialog(
    person: Person,
    shopList: List<Shop>,
    allQualifications: List<String>,
    allRanks: List<String>,
    allSections: List<String>,
    onDismiss: () -> Unit,
    onSave: (Person) -> Unit,
    onSaveAndEditAnother: (Person) -> Unit,
    onDelete: (Person) -> Unit,
    onCancel: () -> Unit = { onSaveAndEditAnother(person) } // Default to reopening selector
) {
    var firstName by remember { mutableStateOf(person.firstName) }
    var lastName by remember { mutableStateOf(person.lastName) }
    var selectedRank by remember { mutableStateOf(person.rank) }
    var selectedShopId by remember { mutableStateOf(person.shopId) }
    var phoneNumber by remember { mutableStateOf(person.phoneNumber) }
    var selectedQualifications by remember {
        mutableStateOf(
            person.qualifications.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
        )
    }
    var selectedSection by remember { mutableStateOf(person.dutySection) }

    var showRankDropdown by remember { mutableStateOf(false) }
    var showShopDropdown by remember { mutableStateOf(false) }
    var showQualDropdown by remember { mutableStateOf(false) }
    var showSectionDropdown by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    val isFormValid = firstName.isNotBlank() && lastName.isNotBlank() &&
            selectedRank.isNotBlank() && selectedShopId > 0 &&
            phoneNumber.filter { it.isDigit() }.length == 10 &&
            selectedSection.isNotBlank()

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = {
                Text(
                    "Confirm Delete",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete ${person.rank} ${person.firstName} ${person.lastName}? This action cannot be undone.",
                    fontSize = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete(person)
                        showDeleteConfirmation = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column (
                modifier = Modifier.width(400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Edit Person", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    // Delete button below title
                    OutlinedButton(
                        onClick = { showDeleteConfirmation = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Red.copy(alpha = 0.1f),
                            contentColor = Color.Red
                        ),
                        border = BorderStroke(2.dp, Color.Red)
                    ) {
                        Text("Delete Person", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.width(400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Name fields
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name") },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Phone number
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { input ->
                        val digitsOnly = input.filter { it.isDigit() }
                        if (digitsOnly.length <= 10) {
                            phoneNumber = digitsOnly
                        }
                    },
                    label = { Text("Phone Number") },
                    placeholder = { Text("5551234567") },
                    modifier = Modifier.fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused && phoneNumber.length == 10) {
                                phoneNumber = "(${phoneNumber.take(3)}) ${phoneNumber.drop(3).take(3)}-${phoneNumber.drop(6)}"
                            }
                        },
                    singleLine = true,
                    supportingText = {
                        Text("Enter 10 digits - will format when complete", fontSize = 12.sp)
                    }
                )

                // Rank dropdown
                Box {
                    OutlinedButton(
                        onClick = { showRankDropdown = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(selectedRank)
                    }
                    DropdownMenu(
                        expanded = showRankDropdown,
                        onDismissRequest = { showRankDropdown = false }
                    ) {
                        allRanks.forEach { rank ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedRank = rank
                                    showRankDropdown = false
                                },
                                text = { Text(rank) }
                            )
                        }
                    }
                }

                // Shop dropdown
                Box {
                    OutlinedButton(
                        onClick = { showShopDropdown = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(shopList.find { it.shopId == selectedShopId }?.name ?: "Select Shop")
                    }
                    DropdownMenu(
                        expanded = showShopDropdown,
                        onDismissRequest = { showShopDropdown = false }
                    ) {
                        shopList.forEach { shop ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedShopId = shop.shopId
                                    showShopDropdown = false
                                },
                                text = { Text(shop.name) }
                            )
                        }
                    }
                }

                // Section dropdown
                Box {
                    OutlinedButton(
                        onClick = { showSectionDropdown = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(selectedSection)
                    }
                    DropdownMenu(
                        expanded = showSectionDropdown,
                        onDismissRequest = { showSectionDropdown = false }
                    ) {
                        allSections.forEach { section ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedSection = section
                                    showSectionDropdown = false
                                },
                                text = { Text(section) }
                            )
                        }
                    }
                }

                // Qualifications
                Text("Qualifications:", fontWeight = FontWeight.Bold)
                Box {
                    OutlinedButton(
                        onClick = { showQualDropdown = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text(
                            if (selectedQualifications.isEmpty()) "Select Qualifications"
                            else selectedQualifications.joinToString(", ")
                        )
                    }
                    DropdownMenu(
                        expanded = showQualDropdown,
                        onDismissRequest = { showQualDropdown = false }
                    ) {
                        allQualifications.forEach { qual ->
                            val isSelected = selectedQualifications.contains(qual)
                            DropdownMenuItem(
                                onClick = {
                                    selectedQualifications = if (isSelected) {
                                        selectedQualifications - qual
                                    } else {
                                        selectedQualifications + qual
                                    }
                                },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(if (isSelected) "✓ $qual" else qual)
                                    }
                                }
                            )
                        }
                        DropdownMenuItem(
                            onClick = { showQualDropdown = false },
                            text = { Text("Done", fontWeight = FontWeight.Bold) }
                        )
                    }
                }

                if (selectedQualifications.isNotEmpty()) {
                    Text(
                        "Selected: ${selectedQualifications.joinToString(", ")}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        },
        confirmButton = {
            // Use a Row to control the button layout precisely
            Row(
                modifier = Modifier.width(400.dp), // Match the dialog width
                horizontalArrangement = Arrangement.SpaceEvenly // This will space buttons evenly
            ) {
                OutlinedButton(
                    onClick = onCancel, // Use the cancel callback to reopen person selector
                    modifier = Modifier.weight(0.75f).padding(horizontal = 4.dp)
                ) {
                    Text("Back")
                }

                Button(
                    onClick = {
                        val updatedPerson = person.copy(
                            firstName = firstName.trim(),
                            lastName = lastName.trim(),
                            rank = selectedRank,
                            shopId = selectedShopId,
                            phoneNumber = phoneNumber,
                            qualifications = selectedQualifications.joinToString(", "),
                            dutySection = selectedSection
                        )
                        onSaveAndEditAnother(updatedPerson)
                    },
                    enabled = isFormValid,
                    modifier = Modifier.weight(1.5f).padding(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue.copy(alpha = 0.8f)
                    )
                ) {
                    Text("Save & Edit Another")
                }

                Button(
                    onClick = {
                        val updatedPerson = person.copy(
                            firstName = firstName.trim(),
                            lastName = lastName.trim(),
                            rank = selectedRank,
                            shopId = selectedShopId,
                            phoneNumber = phoneNumber,
                            qualifications = selectedQualifications.joinToString(", "),
                            dutySection = selectedSection
                        )
                        onSave(updatedPerson)
                    },
                    enabled = isFormValid,
                    modifier = Modifier.weight(0.75f).padding(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green.copy(alpha = 0.8f)
                    )
                ) {
                    Text("Save")
                }
            }
        },
        dismissButton = {
            // Empty - all buttons handled in confirmButton
        }
    )
}