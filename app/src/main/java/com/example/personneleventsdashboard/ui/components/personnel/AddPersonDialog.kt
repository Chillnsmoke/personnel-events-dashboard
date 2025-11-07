package com.example.personneleventsdashboard.ui.components.personnel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
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
fun AddPersonDialog(
    shopList: List<Shop>,
    allQualifications: List<String>,
    allRanks: List<String>,
    allSections: List<String>,
    onDismiss: () -> Unit,
    onSave: (Person) -> Unit,
    onSaveAndContinue: (Person) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var selectedRank by remember { mutableStateOf<String?>(null) }
    var selectedShopId by remember { mutableStateOf<Int?>(null) }
    var phoneNumber by remember { mutableStateOf("") }
    var selectedQualifications by remember { mutableStateOf(setOf<String>()) }
    var selectedSection by remember { mutableStateOf<String?>(null) }

    var showRankDropdown by remember { mutableStateOf(false) }
    var showShopDropdown by remember { mutableStateOf(false) }
    var showQualDropdown by remember { mutableStateOf(false) }
    var showSectionDropdown by remember { mutableStateOf(false) }

    val isFormValid = firstName.isNotBlank() && lastName.isNotBlank() &&
            selectedRank != null && selectedShopId != null &&
            phoneNumber.filter { it.isDigit() }.length == 10 &&
            selectedSection != null

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
                .width(450.dp)  // Same width as SettingsDialog
                .height(720.dp),  // Tall enough for all fields
            shape = RoundedCornerShape(16.dp),  // Same as other dialogs
            colors = CardDefaults.cardColors(containerColor = Color.Black),  // Same black background
            border = BorderStroke(2.dp, Color.Gray)  // Same border style
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),  // Same padding as SettingsDialog
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title - matches other dialogs
                Text(
                    "Add New Person",
                    fontSize = 18.sp,  // Same as other dialog titles
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray  // Same gray color for headers
                )

                // Scrollable content area
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Basic Information Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.LightGray.copy(alpha = 0.1f)  // Same as other dialogs
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            // Name fields
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = firstName,
                                    onValueChange = { firstName = it },
                                    label = { Text("First Name") },
                                    modifier = Modifier.weight(1f),
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
                                    )
                                )
                                OutlinedTextField(
                                    value = lastName,
                                    onValueChange = { lastName = it },
                                    label = { Text("Last Name") },
                                    modifier = Modifier.weight(1f),
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
                                    )
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
                                placeholder = { Text("5551234567", color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth()
                                    .onFocusChanged { focusState ->
                                        if (!focusState.isFocused && phoneNumber.length == 10) {
                                            phoneNumber = "(${phoneNumber.take(3)}) ${phoneNumber.drop(3).take(3)}-${phoneNumber.drop(6)}"
                                        }
                                    },
                                singleLine = true,
                                supportingText = {
                                    Text("Enter 10 digits - will format when complete",
                                        fontSize = 12.sp, color = Color.Gray)
                                },
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
                                )
                            )
                        }
                    }

                    // Assignment Information Section
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

                            // Rank dropdown
                            ExposedDropdownMenuBox(
                                expanded = showRankDropdown,
                                onExpandedChange = { showRankDropdown = !showRankDropdown },
                            ) {
                                OutlinedTextField(
                                    value = selectedRank ?: "Select Rank",
                                    onValueChange = { },
                                    readOnly = true,
                                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showRankDropdown) },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = Color.Gray,
                                        unfocusedBorderColor = Color.Gray,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent
                                    )
                                )
                                val rankOrder = remember {
                                    mapOf(
                                        "AN" to 0,
                                        "AET3" to 1, "AMT3" to 1,  // Same order value groups them together
                                        "AET2" to 2, "AMT2" to 2,
                                        "AET1" to 3, "AMT1" to 3,
                                        "AETC" to 4, "AMTC" to 4,
                                        "AETCS" to 5, "AMTCS" to 5,
                                        "AETCM" to 6, "AMTCM" to 6,
                                        "CWO" to 7,
                                        "ENS" to 8,
                                        "LTJG" to 9,
                                        "LT" to 10,
                                        "LCDR" to 11,
                                        "CDR" to 12,
                                        "CAPT" to 13
                                    )
                                }
                                // Updated dropdown with proper rank sorting (lowest first):
                                ExposedDropdownMenu(
                                    expanded = showRankDropdown,
                                    onDismissRequest = { showRankDropdown = false },
                                    modifier = Modifier.heightIn(max = 300.dp)
                                ) {
                                    allRanks.sortedWith(compareBy(
                                        { rankOrder[it] ?: 999 },                    // Primary: rank order
                                        { if (it.startsWith("AMT")) 1 else 0 }       // Secondary: AET before AMT when same rank level
                                    )).forEach { rank ->
                                        DropdownMenuItem(
                                            text = { Text(rank) },
                                            onClick = {
                                                selectedRank = rank
                                                showRankDropdown = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Shop dropdown
                            ExposedDropdownMenuBox(
                                expanded = showShopDropdown,
                                onExpandedChange = { showShopDropdown = !showShopDropdown }
                            ) {
                                OutlinedTextField(
                                    value = shopList.find { it.shopId == selectedShopId }?.name ?: "Select Shop",
                                    onValueChange = { },
                                    readOnly = true,
                                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showShopDropdown) },
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
                                    expanded = showShopDropdown,
                                    onDismissRequest = { showShopDropdown = false },
                                    modifier = Modifier.heightIn(max = 300.dp) // Limit length of visible dropdown menu
                                ) {
                                    shopList.forEach { shop ->
                                        DropdownMenuItem(
                                            text = { Text(shop.name) },
                                            onClick = {
                                                selectedShopId = shop.shopId
                                                showShopDropdown = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Section dropdown
                            ExposedDropdownMenuBox(
                                expanded = showSectionDropdown,
                                onExpandedChange = { showSectionDropdown = !showSectionDropdown }
                            ) {
                                OutlinedTextField(
                                    value = selectedSection ?: "Select Duty Section",
                                    onValueChange = { },
                                    readOnly = true,
                                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showSectionDropdown) },
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
                                    expanded = showSectionDropdown,
                                    onDismissRequest = { showSectionDropdown = false },
                                    modifier = Modifier.heightIn(max = 300.dp) // Limit length of visible dropdown menu
                                ) {
                                    allSections.forEach { section ->
                                        DropdownMenuItem(
                                            text = { Text(section) },
                                            onClick = {
                                                selectedSection = section
                                                showSectionDropdown = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Qualifications Section
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
                            // Qualifications - Multi-select
                            ExposedDropdownMenuBox(
                                expanded = showQualDropdown,
                                onExpandedChange = { showQualDropdown = !showQualDropdown }
                            ) {
                                OutlinedTextField(
                                    value = if (selectedQualifications.isEmpty()) "Select Qualifications"
                                    else selectedQualifications.joinToString(", "),
                                    onValueChange = { },
                                    readOnly = true,
                                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showQualDropdown) },
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
                                    expanded = showQualDropdown,
                                    onDismissRequest = { showQualDropdown = false },
                                    modifier = Modifier.heightIn(max = 300.dp) // Limit length of visible dropdown menu
                                ) {
                                    allQualifications.forEach { qual ->
                                        val isSelected = selectedQualifications.contains(qual)
                                        DropdownMenuItem(
                                            text = {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(if (isSelected) "✓ $qual" else qual)
                                                }
                                            },
                                            onClick = {
                                                selectedQualifications = if (isSelected) {
                                                    selectedQualifications - qual
                                                } else {
                                                    selectedQualifications + qual
                                                }
                                            }
                                        )
                                    }
                                    DropdownMenuItem(
                                        text = { Text("Done", fontWeight = FontWeight.Bold) },
                                        onClick = { showQualDropdown = false }
                                    )
                                }
                            }

                            if (selectedQualifications.isNotEmpty()) {
                                Text(
                                    "Selected: ${selectedQualifications.joinToString(", ")}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                // Bottom button row - matches other dialogs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Cancel button
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    // Save & Add Another button
                    OutlinedButton(
                        onClick = {
                            val newPerson = Person(
                                firstName = firstName.trim(),
                                lastName = lastName.trim(),
                                rank = selectedRank!!,
                                shopId = selectedShopId!!,
                                phoneNumber = phoneNumber,
                                qualifications = selectedQualifications.joinToString(", "),
                                dutySection = selectedSection!!,
                                status = "Normal"
                            )
                            onSaveAndContinue(newPerson)
                            // Clear form for next person
                            firstName = ""
                            lastName = ""
                            selectedRank = null
                            selectedShopId = null
                            phoneNumber = ""
                            selectedQualifications = setOf()
                            selectedSection = null
                        },
                        enabled = isFormValid,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.LightGray.copy(alpha = 0.2f),
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.2f),
                            disabledContentColor = Color.Gray
                        ),
                        border = BorderStroke(2.dp, Color.Gray),
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.weight(1.5f)
                    ) {
                        Text("Save & Add Another", fontWeight = FontWeight.Bold)
                    }

                    // Save button
                    OutlinedButton(
                        onClick = {
                            val newPerson = Person(
                                firstName = firstName.trim(),
                                lastName = lastName.trim(),
                                rank = selectedRank!!,
                                shopId = selectedShopId!!,
                                phoneNumber = phoneNumber,
                                qualifications = selectedQualifications.joinToString(", "),
                                dutySection = selectedSection!!,
                                status = "Normal"
                            )
                            onSave(newPerson)
                        },
                        enabled = isFormValid,
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