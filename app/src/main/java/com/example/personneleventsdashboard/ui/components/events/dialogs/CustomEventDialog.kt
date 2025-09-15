package com.example.personneleventsdashboard.ui.components.events.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import com.example.personneleventsdashboard.model.TailNumber
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun CustomEventDialog(
    selectedDate: LocalDate,
    tailNumbers: List<TailNumber>,
    onDismiss: () -> Unit,
    onBack: () -> Unit,
    onEventAdded: (Event) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedTailNumber by remember { mutableStateOf<String?>(null) }
    var customTailNumber by remember { mutableStateOf("") }
    var useCustomTailNumber by remember { mutableStateOf(false) }
    var isMultiDay by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf(selectedDate) }
    var endDate by remember { mutableStateOf(selectedDate) }

    // Date picker states
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val isFormValid = title.isNotBlank() && startDate <= endDate

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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Custom Event - ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.width(500.dp), // Increased width to match PresetEventDialog
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Event Title") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 20.sp),
                    maxLines = 3
                )

                Divider()

                // Aircraft tail number selection (same as PresetEventDialog)
                Text(
                    fontSize = 18.sp,
                    text = "Select aircraft:",
                    fontWeight = FontWeight.Bold
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
                                fontSize = 18.sp
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
                        placeholder = {
                            Text(
                                text = "Enter tail number",
                                fontSize = 16.sp
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = useCustomTailNumber || customTailNumber.isNotBlank()
                    )
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
                                startDate = selectedDate
                                endDate = selectedDate
                            }
                        }
                    )
                    Text(
                        text = "Multi-day event",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp),
                        fontWeight = FontWeight.Medium
                    )
                }

                // Date selection (only show when multi-day is checked)
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
                                text = "Date Range",
                                fontSize = 18.sp,
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
                                        Text("Start Date", fontSize = 14.sp, color = Color.Gray)
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
                                        Text("End Date", fontSize = 14.sp, color = Color.Gray)
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
                    // Single day event info
                    Text(
                        "Single day event on ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.width(500.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    Text("Back")
                }

                Button(
                    onClick = {
                        val finalStartDate = if (isMultiDay) startDate else selectedDate
                        val finalEndDate = if (isMultiDay) endDate else selectedDate
                        val finalTailNumber = when {
                            useCustomTailNumber && customTailNumber.isNotBlank() -> customTailNumber.trim()
                            selectedTailNumber != null -> selectedTailNumber
                            else -> null
                        }

                        val newEvent = Event(
                            title = title.trim(),
                            description = description.takeIf { it.isNotBlank() },
                            startDate = finalStartDate,
                            endDate = finalEndDate,
                            aircraftTailNumber = finalTailNumber,
                            status = "Scheduled"
                        )
                        onEventAdded(newEvent)
                    },
                    enabled = isFormValid,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green.copy(alpha = 0.8f)
                    )
                ) {
                    Text("Add Event")
                }
            }
        },
        dismissButton = {}
    )
}