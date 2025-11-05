package com.example.personneleventsdashboard.ui.components.events.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import java.time.format.DateTimeFormatter

@Composable
fun EventDetailsDialog(
    event: Event,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onStatusUpdate: (Event) -> Unit
) {
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
                .width(450.dp)  // Same width as other dialogs
                .height(420.dp), // Adequate height for all content
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
                    event.title,
                    fontSize = 18.sp,  // Same as other dialog titles
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray  // Same gray color for headers
                )

                // Event Details Section
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
                        // Date info
                        if (event.startDate == event.endDate) {
                            Text(
                                "Date: ${event.startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        } else {
                            Text(
                                "Dates: ${event.startDate.format(DateTimeFormatter.ofPattern("MMM dd"))} - ${event.endDate.format(
                                    DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }

                        // Description
                        event.description?.let { desc ->
                            Text(
                                "Description: $desc",
                                fontSize = 14.sp,
                                color = Color.LightGray
                            )
                        }

                        // Aircraft
                        event.aircraftTailNumber?.let { tailNumber ->
                            Text(
                                "Aircraft: $tailNumber",
                                fontSize = 14.sp,
                                color = Color.LightGray
                            )
                        }

                        // Current Status Display
                        Text(
                            "Status: ${event.status}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = when (event.status) {
                                "Complete" -> Color.Green
                                "In Progress" -> Color.Yellow
                                "Cancelled" -> Color.Red
                                else -> Color.LightGray
                            }
                        )
                    }
                }

                // Quick Status Update Section
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
                            "Quick Status Update:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        // Quick Status Buttons Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // In Progress Button
                            val isInProgress = event.status == "In Progress"
                            OutlinedButton(
                                onClick = {
                                    val newStatus = if (isInProgress) "Scheduled" else "In Progress"
                                    val updatedEvent = event.copy(status = newStatus)
                                    onStatusUpdate(updatedEvent)
                                },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isInProgress) Color.Yellow.copy(alpha = 0.2f) else Color.Transparent,
                                    contentColor = if (isInProgress) Color.Yellow else Color.LightGray
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    Color.Gray
                                )
                            ) {
                                Text(
                                    "In Progress",
                                    fontSize = 14.sp,
                                    fontWeight = if (isInProgress) FontWeight.Bold else FontWeight.Normal
                                )
                            }

                            // Complete Button
                            val isComplete = event.status == "Complete"
                            OutlinedButton(
                                onClick = {
                                    val newStatus = if (isComplete) "Scheduled" else "Complete"
                                    val updatedEvent = event.copy(status = newStatus)
                                    onStatusUpdate(updatedEvent)
                                },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isComplete) Color.Green.copy(alpha = 0.2f) else Color.Transparent,
                                    contentColor = if (isComplete) Color.Green else Color.LightGray
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    Color.Gray
                                )
                            ) {
                                Text(
                                    "Complete",
                                    fontSize = 14.sp,
                                    fontWeight = if (isComplete) FontWeight.Bold else FontWeight.Normal
                                )
                            }

                            // Cancelled Button
                            val isCancelled = event.status == "Cancelled"
                            OutlinedButton(
                                onClick = {
                                    val newStatus = if (isCancelled) "Scheduled" else "Cancelled"
                                    val updatedEvent = event.copy(status = newStatus)
                                    onStatusUpdate(updatedEvent)
                                },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (isCancelled) Color.Red.copy(alpha = 0.2f) else Color.Transparent,
                                    contentColor = if (isCancelled) Color.Red else Color.LightGray
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    Color.Gray
                                )
                            ) {
                                Text(
                                    "Cancelled",
                                    fontSize = 14.sp,
                                    fontWeight = if (isCancelled) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }

                        // Helper text
                        Text(
                            "Tap a status to set it, or tap again to return to Scheduled",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Bottom button row - matches other dialogs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Close button
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Close", fontWeight = FontWeight.Bold)
                    }

                    // Edit button
                    OutlinedButton(
                        onClick = onEdit,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.LightGray.copy(alpha = 0.2f),
                            contentColor = Color.White
                        ),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Edit", fontWeight = FontWeight.Bold)
                    }

                    // Delete button
                    OutlinedButton(
                        onClick = onDelete,
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