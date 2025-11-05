package com.example.personneleventsdashboard.ui.components.events.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.model.EventType
import com.example.personneleventsdashboard.model.TailNumber
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
            // Main choice dialog - Using Dialog instead of AlertDialog for full width control
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
                        .height(400.dp), // Adequate height for title and buttons
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
                            "Add Event - ${selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                            fontSize = 18.sp,  // Same as other dialog titles
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray  // Same gray color for headers
                        )

                        // Content area with same Card styling as other dialogs
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),  // Take most of the space
                            colors = CardDefaults.cardColors(
                                containerColor = Color.LightGray.copy(alpha = 0.1f)  // Same as other dialogs
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    "What type of event would you like to add?",
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Bold
                                )

                                // Quick Event Button
                                OutlinedButton(
                                    onClick = { showPresetEvents = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(70.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.LightGray.copy(alpha = 0.2f),
                                        contentColor = Color.White
                                    ),
                                    border = BorderStroke(2.dp, Color.Gray)
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Quick Event", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                        Text("Aircraft Wash, Inspection, etc.", fontSize = 14.sp, color = Color.LightGray)
                                    }
                                }

                                // Custom Event Button
                                OutlinedButton(
                                    onClick = { showCustomEvent = true },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(70.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.LightGray.copy(alpha = 0.2f),
                                        contentColor = Color.White
                                    ),
                                    border = BorderStroke(2.dp, Color.Gray)
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("Custom Event", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                        Text("Create your own event", fontSize = 14.sp, color = Color.LightGray)
                                    }
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
                            // Cancel button - same styling as other dialogs
                            OutlinedButton(
                                onClick = onDismiss,
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                                border = BorderStroke(2.dp, Color.Gray),
                                modifier = Modifier.width(120.dp)
                            ) {
                                Text("Cancel", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}