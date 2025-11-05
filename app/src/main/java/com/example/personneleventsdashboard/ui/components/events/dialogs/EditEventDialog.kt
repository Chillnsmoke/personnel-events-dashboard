package com.example.personneleventsdashboard.ui.components.events.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.model.EventType
import com.example.personneleventsdashboard.model.TailNumber
import com.example.personneleventsdashboard.ui.components.events.getEventIcon
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun EditEventDialog(
    event: Event,
    eventTypes: List<EventType>,
    tailNumbers: List<TailNumber>,
    onDismiss: () -> Unit,
    onEventUpdated: (Event) -> Unit,
    onEventDeleted: (Event) -> Unit
) {
    // Pre-populate with existing event data
    var title by remember { mutableStateOf(event.title) }
    var description by remember { mutableStateOf(event.description ?: "") }
    var selectedTailNumber by remember {
        mutableStateOf(
            if (tailNumbers.any { it.number == event.aircraftTailNumber }) event.aircraftTailNumber else null
        )
    }
    var customTailNumber by remember {
        mutableStateOf(
            if (tailNumbers.none { it.number == event.aircraftTailNumber }) event.aircraftTailNumber ?: "" else ""
        )
    }
    var useCustomTailNumber by remember {
        mutableStateOf(tailNumbers.none { it.number == event.aircraftTailNumber } && !event.aircraftTailNumber.isNullOrBlank())
    }
    var selectedStatus by remember { mutableStateOf(event.status) }
    var selectedEventType by remember {
        mutableStateOf(eventTypes.find { it.eventTypeId == event.eventTypeId })
    }

    // Multi-day support
    var isMultiDay by remember { mutableStateOf(event.startDate != event.endDate) }
    var startDate by remember { mutableStateOf(event.startDate) }
    var endDate by remember { mutableStateOf(event.endDate) }

    // Date picker states
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // Available status options
    val statusOptions = listOf("Scheduled", "In Progress", "Complete", "Cancelled")
    var showStatusDropdown by remember { mutableStateOf(false) }
    var showEventTypeDropdown by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    val isFormValid = title.isNotBlank() && startDate <= endDate

    // Date picker dialogs
    if (showStartDatePicker) {
        DatePickerDialog(
            currentDate = startDate,
            onDateSelected = { newDate ->
                startDate = newDate
                if (newDate > endDate) {
                    endDate = newDate
                }
                showStartDatePicker = false
            },
            onDismiss = { showStartDatePicker = false }
        )
    }

    if (showEndDatePicker) {
        DatePickerDialog(
            currentDate = endDate,
            minDate = startDate,
            onDateSelected = { newDate ->
                endDate = newDate
                showEndDatePicker = false
            },
            onDismiss = { showEndDatePicker = false }
        )
    }

    // Delete confirmation dialog - Updated to match styling
    if (showDeleteConfirmation) {
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
                    .height(250.dp),
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
                    Text(
                        "Delete Event",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.LightGray.copy(alpha = 0.1f)
                        )
                    ) {
                        Text(
                            "Are you sure you want to delete \"${event.title}\"? This action cannot be undone.",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

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
                            Text("Cancel", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                onEventDeleted(event)
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

    // Main edit dialog - Using Dialog instead of AlertDialog for full width control
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
                .width(500.dp)  // Same width as other event dialogs
                .height(850.dp), // Taller for all content
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
                // Title row with Delete button - matches other dialogs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Edit Event",
                        fontSize = 18.sp,  // Same as other dialog titles
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray  // Same gray color for headers
                    )

                    OutlinedButton(
                        onClick = { showDeleteConfirmation = true },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Red.copy(alpha = 0.2f),
                            contentColor = Color.Red
                        ),
                        border = BorderStroke(2.dp, Color.Red),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier.width(100.dp)
                    ) {
                        Text("Delete", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

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
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                label = { Text("Event Title", color = Color.Gray) },
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
                                )
                            )

                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = { Text("Description", color = Color.Gray) },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 3,
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

                    // Status and Event Type Section
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
                            Text(
                                "Status & Type",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            // Status dropdown
                            Box {
                                OutlinedButton(
                                    onClick = { showStatusDropdown = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = Color.White
                                    ),
                                    border = BorderStroke(1.dp, Color.Gray)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Status: $selectedStatus")
                                        Text("▼", fontSize = 12.sp)
                                    }
                                }

                                DropdownMenu(
                                    expanded = showStatusDropdown,
                                    onDismissRequest = { showStatusDropdown = false }
                                ) {
                                    statusOptions.forEach { status ->
                                        DropdownMenuItem(
                                            text = { Text(status) },
                                            onClick = {
                                                selectedStatus = status
                                                showStatusDropdown = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Event Type dropdown
                            Box {
                                OutlinedButton(
                                    onClick = { showEventTypeDropdown = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = Color.White
                                    ),
                                    border = BorderStroke(1.dp, Color.Gray)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Type: ${selectedEventType?.name ?: "Custom Event"}")
                                        Text("▼", fontSize = 12.sp)
                                    }
                                }

                                DropdownMenu(
                                    expanded = showEventTypeDropdown,
                                    onDismissRequest = { showEventTypeDropdown = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Custom Event") },
                                        onClick = {
                                            selectedEventType = null
                                            showEventTypeDropdown = false
                                        }
                                    )
                                    eventTypes.forEach { eventType ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text("${getEventIcon(eventType.iconName)} ${eventType.name}")
                                                }
                                            },
                                            onClick = {
                                                selectedEventType = eventType
                                                showEventTypeDropdown = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Aircraft Selection Section
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
                            Text(
                                text = "Select aircraft:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                modifier = Modifier.height(100.dp),
                                contentPadding = PaddingValues(4.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                items(tailNumbers) { tailNumber ->
                                    val isSelected = selectedTailNumber == tailNumber.number && !useCustomTailNumber

                                    OutlinedButton(
                                        onClick = {
                                            if (isSelected) {
                                                selectedTailNumber = null
                                                useCustomTailNumber = false
                                                customTailNumber = ""
                                            } else {
                                                selectedTailNumber = tailNumber.number
                                                useCustomTailNumber = false
                                                customTailNumber = ""
                                            }
                                        },
                                        modifier = Modifier.height(40.dp),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = if (isSelected) Color.LightGray.copy(alpha = 0.3f) else Color.Transparent,
                                            contentColor = if (isSelected) Color.White else Color.LightGray
                                        ),
                                        border = BorderStroke(
                                            2.dp,
                                            if (isSelected) Color.White else Color.Gray
                                        )
                                    ) {
                                        Text(
                                            text = tailNumber.number,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 16.sp
                                        )
                                    }
                                }
                            }

                            // Custom tail number option
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = useCustomTailNumber,
                                    onCheckedChange = {
                                        useCustomTailNumber = it
                                        if (it) {
                                            selectedTailNumber = null
                                        } else {
                                            customTailNumber = ""
                                        }
                                    }
                                )
                                Text(
                                    text = "Other:",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                                OutlinedTextField(
                                    value = customTailNumber,
                                    onValueChange = {
                                        customTailNumber = it
                                        if (it.isNotBlank()) {
                                            useCustomTailNumber = true
                                            selectedTailNumber = null
                                        }
                                    },
                                    placeholder = { Text("Enter tail number", color = Color.Gray) },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    enabled = useCustomTailNumber || customTailNumber.isNotBlank(),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = Color.Gray,
                                        unfocusedBorderColor = Color.Gray,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledTextColor = Color.Gray,
                                        disabledBorderColor = Color.Gray.copy(alpha = 0.5f)
                                    )
                                )
                            }
                        }
                    }

                    // Multi-day Event Section
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
                            // Multi-day event checkbox
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isMultiDay,
                                    onCheckedChange = {
                                        isMultiDay = it
                                        if (!it) {
                                            endDate = startDate
                                        }
                                    }
                                )
                                Text(
                                    text = "Multi-day event",
                                    modifier = Modifier.padding(start = 8.dp),
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }

                            // Date editing
                            if (isMultiDay) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.LightGray.copy(alpha = 0.15f)
                                    ),
                                    border = BorderStroke(1.dp, Color.Gray)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            "Date Range",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )

                                        // Start Date Picker Button
                                        OutlinedButton(
                                            onClick = { showStartDatePicker = true },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                containerColor = Color.Transparent,
                                                contentColor = Color.White
                                            ),
                                            border = BorderStroke(1.dp, Color.Gray)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text("Start Date", fontSize = 12.sp, color = Color.Gray)
                                                    Text(
                                                        startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                }
                                                Text("📅", fontSize = 20.sp)
                                            }
                                        }

                                        // End Date Picker Button
                                        OutlinedButton(
                                            onClick = { showEndDatePicker = true },
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                containerColor = Color.Transparent,
                                                contentColor = Color.White
                                            ),
                                            border = BorderStroke(1.dp, Color.Gray)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text("End Date", fontSize = 12.sp, color = Color.Gray)
                                                    Text(
                                                        endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                }
                                                Text("📅", fontSize = 20.sp)
                                            }
                                        }

                                        // Duration display
                                        val duration = ChronoUnit.DAYS.between(startDate, endDate) + 1
                                        Text(
                                            "Duration: $duration day${if (duration != 1L) "s" else ""}",
                                            fontSize = 14.sp,
                                            color = Color.Gray,
                                            fontStyle = FontStyle.Italic,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            } else {
                                // Single day event - show date picker button
                                OutlinedButton(
                                    onClick = { showStartDatePicker = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.Transparent,
                                        contentColor = Color.White
                                    ),
                                    border = BorderStroke(1.dp, Color.Gray)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("Event Date", fontSize = 12.sp, color = Color.Gray)
                                            Text(
                                                startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                        Text("📅", fontSize = 20.sp)
                                    }
                                }
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
                        Text("Cancel", fontWeight = FontWeight.Bold)
                    }

                    // Save Changes button
                    OutlinedButton(
                        onClick = {
                            val finalTailNumber = when {
                                useCustomTailNumber && customTailNumber.isNotBlank() -> customTailNumber.trim()
                                selectedTailNumber != null -> selectedTailNumber
                                else -> null
                            }

                            val updatedEvent = event.copy(
                                title = title.trim(),
                                description = description.takeIf { it.isNotBlank() },
                                aircraftTailNumber = finalTailNumber,
                                status = selectedStatus,
                                eventTypeId = selectedEventType?.eventTypeId,
                                startDate = startDate,
                                endDate = if (isMultiDay) endDate else startDate
                            )
                            onEventUpdated(updatedEvent)
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
                        Text("Save Changes", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}