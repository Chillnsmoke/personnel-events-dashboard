package com.example.personneleventsdashboard.ui.components.events.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.model.EventType
import com.example.personneleventsdashboard.model.TailNumber
import com.example.personneleventsdashboard.ui.components.events.getEventColor
import com.example.personneleventsdashboard.ui.components.events.getEventIcon
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Quick Event - ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.width(500.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    fontSize = 16.sp,
                    text = "Select an event type:",
                    fontWeight = FontWeight.Bold
                )


                // Event type selection grid - 3 columns
                LazyColumn(
                    modifier = Modifier.height(220.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Group event types into pairs for 3-column layout
                    val eventTypePairs = eventTypes.chunked(3)

                    items(eventTypePairs) { eventTypePair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            eventTypePair.forEach { eventType ->
                                val isSelected = selectedEventType == eventType
                                val eventColor = getEventColor(eventType.color)
                                val eventIcon = getEventIcon(eventType.iconName)

                                OutlinedButton(
                                    onClick = { selectedEventType = eventType },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (isSelected) eventColor.copy(alpha = 0.2f) else Color.White,
                                        contentColor = if (isSelected) eventColor else Color.Black
                                    ),
                                    border = BorderStroke(
                                        2.dp,
                                        if (isSelected) eventColor else Color.Gray
                                    )
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = eventIcon,
                                            fontSize = 20.sp,
                                            modifier = Modifier.padding(bottom = 4.dp)
                                        )
                                        Text(
                                            text = eventType.name,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 18.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            // If odd number of items, add spacer for the last row
                            if (eventTypePair.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Divider()

                // Quick tail number selection - 3 columns for better fit
                Text(
                    fontSize = 16.sp,
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
                        placeholder = { Text(
                            fontSize = 16.sp,
                            text = "Enter tail number",
                            fontWeight = FontWeight.Bold
                        )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = useCustomTailNumber || customTailNumber.isNotBlank()
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
                    Text(
                        fontSize = 18.sp,
                        text = "Back"
                    )
                }

                Button(
                    onClick = {
                        selectedEventType?.let { eventType ->
                            val finalTailNumber = when {
                                useCustomTailNumber && customTailNumber.isNotBlank() -> customTailNumber.trim()
                                selectedTailNumber != null -> selectedTailNumber
                                else -> null
                            }

                            val newEvent = Event(
                                title = eventType.name,
                                description = eventType.description,
                                startDate = selectedDate,
                                endDate = selectedDate,
                                eventTypeId = eventType.eventTypeId,
                                aircraftTailNumber = finalTailNumber,
                                status = "Scheduled"
                            )
                            onEventAdded(newEvent)
                        }
                    },
                    enabled = selectedEventType != null,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green.copy(alpha = 0.4f)
                    )
                ) {
                    Text(
                        fontSize = 18.sp,
                        text = "Add Event"
                    )
                }
            }
        },
        dismissButton = {}
    )
}