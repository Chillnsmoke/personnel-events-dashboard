package com.example.personneleventsdashboard.ui.components.events.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.model.EventType
import com.example.personneleventsdashboard.model.TailNumber
import com.example.personneleventsdashboard.ui.components.events.getEventIcon
import com.example.personneleventsdashboard.ui.theme.Orange
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

    // Delete confirmation dialog
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = {
                Text(
                    "Delete Event",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${event.title}\"? This action cannot be undone.",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onEventDeleted(event)
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

    // Main edit dialog
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.width(500.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Edit Event",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                OutlinedButton(
                    onClick = { showDeleteConfirmation = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Red.copy(alpha = 0.1f),
                        contentColor = Color.Red
                    ),
                    border = BorderStroke(2.dp, Color.Red)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.width(500.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Event Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Event Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Event Type (if this was a preset event)
                if (event.eventTypeId != null) {
                    Text("Event Type:", fontWeight = FontWeight.Bold)
                    Box {
                        OutlinedButton(
                            onClick = { showEventTypeDropdown = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    selectedEventType?.let { eventType ->
                                        Text(
                                            text = getEventIcon(eventType.iconName),
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(end = 8.dp)
                                        )
                                        Text(eventType.name)
                                    } ?: Text("Select Event Type")
                                }
                            }
                        }

                        DropdownMenu(
                            expanded = showEventTypeDropdown,
                            onDismissRequest = { showEventTypeDropdown = false }
                        ) {
                            eventTypes.filter { it.isPreset }.forEach { eventType ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedEventType = eventType
                                        title = eventType.name
                                        showEventTypeDropdown = false
                                    },
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = getEventIcon(eventType.iconName),
                                                fontSize = 16.sp,
                                                modifier = Modifier.padding(end = 8.dp)
                                            )
                                            Text(eventType.name)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Divider()

                // Aircraft tail number selection with toggle clear
                Text("Select aircraft:", fontWeight = FontWeight.Bold)

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
                                    // If already selected, clear it
                                    selectedTailNumber = null
                                    useCustomTailNumber = false
                                    customTailNumber = ""
                                } else {
                                    // If not selected, select it
                                    selectedTailNumber = tailNumber.number
                                    useCustomTailNumber = false
                                    customTailNumber = ""
                                }
                            },
                            modifier = Modifier.height(40.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) Color.Blue.copy(alpha = 0.1f) else Color.White,
                                contentColor = if (isSelected) Color.Blue else Color.Black
                            ),
                            border = BorderStroke(
                                2.dp,
                                if (isSelected) Color.Blue else Color.Gray
                            )
                        ) {
                            Text(
                                text = tailNumber.number,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp
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
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        fontWeight = FontWeight.Medium
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
                        placeholder = { Text("Enter tail number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = useCustomTailNumber || customTailNumber.isNotBlank()
                    )
                }

                Divider()

                // Status
                Text("Status:", fontWeight = FontWeight.Bold)
                Box {
                    OutlinedButton(
                        onClick = { showStatusDropdown = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = when (selectedStatus) {
                                "Complete" -> Color.Green.copy(alpha = 0.1f)
                                "In Progress" -> Orange.copy(alpha = 0.1f)
                                "Cancelled" -> Color.Red.copy(alpha = 0.1f)
                                else -> Color.White
                            },
                            contentColor = when (selectedStatus) {
                                "Complete" -> Color.Green
                                "In Progress" -> Orange
                                "Cancelled" -> Color.Red
                                else -> Color.Black
                            }
                        )
                    ) {
                        Text(selectedStatus)
                    }

                    DropdownMenu(
                        expanded = showStatusDropdown,
                        onDismissRequest = { showStatusDropdown = false }
                    ) {
                        statusOptions.forEach { status ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedStatus = status
                                    showStatusDropdown = false
                                },
                                text = {
                                    Text(
                                        status,
                                        color = when (status) {
                                            "Complete" -> Color.Green
                                            "In Progress" -> Orange
                                            "Cancelled" -> Color.Red
                                            else -> Color.Black
                                        }
                                    )
                                }
                            )
                        }
                    }
                }

                Divider()

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
                        fontWeight = FontWeight.Medium
                    )
                }

                // Date editing
                if (isMultiDay) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Blue.copy(alpha = 0.05f)
                        ),
                        border = BorderStroke(1.dp, Color.Blue.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "Date Range",
                                fontWeight = FontWeight.Bold,
                                color = Color.Blue
                            )

                            // Start Date Picker Button
                            OutlinedButton(
                                onClick = { showStartDatePicker = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                border = BorderStroke(2.dp, Color.Blue.copy(alpha = 0.3f))
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
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                border = BorderStroke(2.dp, Color.Blue.copy(alpha = 0.3f))
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
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        border = BorderStroke(2.dp, Color.Blue.copy(alpha = 0.3f))
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
        },
        confirmButton = {
            Row(
                modifier = Modifier.width(500.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    Text("Cancel")
                }

                Button(
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
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue.copy(alpha = 0.8f)
                    )
                ) {
                    Text("Save Changes")
                }
            }
        },
        dismissButton = {}
    )
}