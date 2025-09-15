package com.example.personneleventsdashboard.ui.components.events.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.model.EventType
import com.example.personneleventsdashboard.model.TailNumber
import com.example.personneleventsdashboard.ui.theme.Charcoal
import com.example.personneleventsdashboard.ui.theme.Forest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AddEventDialog(
    selectedDate: LocalDate,
    eventTypes: List<EventType>,
    tailNumbers: List<TailNumber>,
    onDismiss: () -> Unit,
    onEventAdded: (Event) -> Unit
) {
    var showPresetEvents by remember { mutableStateOf(false) }
    var showCustomEvent by remember { mutableStateOf(false) }

    when {
        showPresetEvents -> {
            PresetEventDialog(
                selectedDate = selectedDate,
                eventTypes = eventTypes.filter { it.isPreset },
                tailNumbers = tailNumbers,
                onDismiss = { showPresetEvents = false; onDismiss() },
                onBack = { showPresetEvents = false },
                onEventAdded = onEventAdded
            )
        }
        showCustomEvent -> {
            CustomEventDialog(
                selectedDate = selectedDate,
                tailNumbers = tailNumbers,
                onDismiss = { showCustomEvent = false; onDismiss() },
                onBack = { showCustomEvent = false },
                onEventAdded = onEventAdded
            )
        }
        else -> {
            // Main choice dialog
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Text(
                        "Add Event - ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                text = {
                    Column(
                        modifier = Modifier.width(400.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "What type of event would you like to add?",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )

                        // Quick Event Button
                        OutlinedButton(
                            onClick = { showPresetEvents = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.DarkGray.copy(alpha = 0.2f),
                                contentColor = Charcoal
                            ),
                            border = BorderStroke(2.dp, Charcoal.copy(alpha = 0.4f))
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Quick Event", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text("Aircraft Wash, Inspection, etc.", fontSize = 16.sp)
                            }
                        }

                        // Custom Event Button
                        OutlinedButton(
                            onClick = { showCustomEvent = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.Gray.copy(alpha = 0.3f),
                                contentColor = Forest
                            ),
                            border = BorderStroke(2.dp, Forest.copy(alpha = 0.4f))
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Custom Event", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text("Create your own event", fontSize = 16.sp)
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}