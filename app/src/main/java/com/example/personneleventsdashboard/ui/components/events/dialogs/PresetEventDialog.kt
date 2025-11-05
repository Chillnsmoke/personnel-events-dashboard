package com.example.personneleventsdashboard.ui.components.events.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
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
import com.example.personneleventsdashboard.ui.components.events.getEventColor
import com.example.personneleventsdashboard.ui.components.events.getEventIcon
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun PresetEventDialog(
    selectedDate: LocalDate,
    eventTypes: List<EventType>,
    tailNumbers: List<TailNumber>,
    onDismiss: () -> Unit,
    onBack: () -> Unit,
    onEventAdded: (Event) -> Unit
) {
    var selectedEventType by remember { mutableStateOf<EventType?>(null) }
    var selectedTailNumber by remember { mutableStateOf<String?>(null) }
    var customTailNumber by remember { mutableStateOf("") }
    var useCustomTailNumber by remember { mutableStateOf(false) }
    var description by remember { mutableStateOf("") }
    var isMultiDay by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf(selectedDate) }
    var endDate by remember { mutableStateOf(selectedDate) }

    // Date picker states
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // Start Date Picker Dialog
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

    // End Date Picker Dialog
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
                .width(500.dp)  // Wider for more content
                .height(900.dp), // Taller for all sections
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
                    "Quick Event - ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
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
                    // Event Type Selection Section
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
                                text = "Select an event type:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            // Event type selection grid - 3 columns
                            LazyColumn(
                                modifier = Modifier.height(200.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Group event types into triplets for 3-column layout
                                val eventTypeTriplets = eventTypes.chunked(3)

                                items(eventTypeTriplets) { eventTypeTriplet ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        eventTypeTriplet.forEach { eventType ->
                                            val isSelected = selectedEventType == eventType
                                            val eventColor = getEventColor(eventType.color)
                                            val eventIcon = getEventIcon(eventType.iconName)

                                            OutlinedButton(
                                                onClick = { selectedEventType = eventType },
                                                modifier = Modifier.weight(1f),
                                                colors = ButtonDefaults.outlinedButtonColors(
                                                    containerColor = if (isSelected) Color.LightGray.copy(alpha = 0.3f) else Color.Transparent,
                                                    contentColor = if (isSelected) Color.White else Color.LightGray
                                                ),
                                                border = BorderStroke(
                                                    2.dp,
                                                    if (isSelected) Color.White else Color.Gray
                                                )
                                            ) {
                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Text(
                                                        text = eventIcon,
                                                        fontSize = 18.sp,
                                                        modifier = Modifier.padding(bottom = 4.dp)
                                                    )
                                                    Text(
                                                        text = eventType.name,
                                                        fontWeight = FontWeight.Medium,
                                                        fontSize = 14.sp,
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                            }
                                        }

                                        // Fill remaining space if odd number
                                        repeat(3 - eventTypeTriplet.size) {
                                            Spacer(modifier = Modifier.weight(1f))
                                        }
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

                    // Description Section
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.LightGray.copy(alpha = 0.1f)  // Same as other dialogs
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = { Text("Description (Optional)", color = Color.Gray) },
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
                                            startDate = selectedDate
                                            endDate = selectedDate
                                        }
                                    }
                                )
                                Text(
                                    text = "Multi-day event",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 8.dp),
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }

                            // Date selection (only show when multi-day is checked)
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
                                            text = "Date Range",
                                            fontSize = 16.sp,
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
                                                    Text("Start Date", fontSize = 14.sp, color = Color.Gray)
                                                    Text(
                                                        startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                }
                                                Text("📅", fontSize = 18.sp)
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
                                                    Text("End Date", fontSize = 14.sp, color = Color.Gray)
                                                    Text(
                                                        endDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                }
                                                Text("📅", fontSize = 18.sp)
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
                                // Single day event info
                                Text(
                                    "Single day event on ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    fontStyle = FontStyle.Italic
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
                    // Back button
                    OutlinedButton(
                        onClick = onBack,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Back", fontWeight = FontWeight.Bold)
                    }

                    // Add Event button
                    OutlinedButton(
                        onClick = {
                            selectedEventType?.let { eventType ->
                                val finalStartDate = if (isMultiDay) startDate else selectedDate
                                val finalEndDate = if (isMultiDay) endDate else selectedDate
                                val finalTailNumber = when {
                                    useCustomTailNumber && customTailNumber.isNotBlank() -> customTailNumber.trim()
                                    selectedTailNumber != null -> selectedTailNumber
                                    else -> null
                                }

                                val newEvent = Event(
                                    title = eventType.name,
                                    description = if (description.isNotBlank()) description.trim() else eventType.description,
                                    startDate = finalStartDate,
                                    endDate = finalEndDate,
                                    eventTypeId = eventType.eventTypeId,
                                    aircraftTailNumber = finalTailNumber,
                                    status = "Scheduled"
                                )
                                onEventAdded(newEvent)
                            }
                        },
                        enabled = selectedEventType != null,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.LightGray.copy(alpha = 0.2f),
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray.copy(alpha = 0.2f),
                            disabledContentColor = Color.Gray
                        ),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add Event", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}