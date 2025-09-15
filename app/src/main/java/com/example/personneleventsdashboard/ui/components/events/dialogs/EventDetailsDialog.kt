package com.example.personneleventsdashboard.ui.components.events.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.ui.theme.Orange
import java.time.format.DateTimeFormatter

@Composable
fun EventDetailsDialog(
    event: Event,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onStatusUpdate: (Event) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                event.title,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        },
        text = {
            Column(
                modifier = Modifier.width(400.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Date info
                if (event.startDate == event.endDate) {
                    Text(
                        "Date: ${event.startDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        "Dates: ${event.startDate.format(DateTimeFormatter.ofPattern("MMM dd"))} - ${event.endDate.format(
                            DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Description
                event.description?.let { desc ->
                    Text(
                        "Description: $desc",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // Aircraft
                event.aircraftTailNumber?.let { tailNumber ->
                    Text(
                        "Aircraft: $tailNumber",
                        fontSize = 14.sp,
                        color = Color.Blue
                    )
                }

                // Current Status Display
                Text(
                    "Status: ${event.status}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = when (event.status) {
                        "Complete" -> Color.Green
                        "In Progress" -> Orange
                        "Cancelled" -> Color.Red
                        else -> Color.Gray
                    }
                )

                Divider()

                // Quick Status Update Section
                Text(
                    "Quick Status Update:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
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
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isInProgress) Orange.copy(alpha = 0.2f) else Color.White,
                            contentColor = if (isInProgress) Orange else Color.Black
                        ),
                        border = BorderStroke(
                            2.dp,
                            if (isInProgress) Orange else Color.Gray
                        )
                    ) {
                        Text(
                            "In Progress",
                            fontSize = 12.sp,
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
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isComplete) Color.Green.copy(alpha = 0.2f) else Color.White,
                            contentColor = if (isComplete) Color.Green else Color.Black
                        ),
                        border = BorderStroke(
                            2.dp,
                            if (isComplete) Color.Green else Color.Gray
                        )
                    ) {
                        Text(
                            "Complete",
                            fontSize = 12.sp,
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
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isCancelled) Color.Red.copy(alpha = 0.2f) else Color.White,
                            contentColor = if (isCancelled) Color.Red else Color.Black
                        ),
                        border = BorderStroke(
                            2.dp,
                            if (isCancelled) Color.Red else Color.Gray
                        )
                    ) {
                        Text(
                            "Cancelled",
                            fontSize = 12.sp,
                            fontWeight = if (isCancelled) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }

                // Helper text
                Text(
                    "Tap a status to set it, or tap again to return to Scheduled",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.width(400.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    Text("Close")
                }

                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Blue
                    )
                ) {
                    Text("Edit")
                }

                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text("Delete")
                }
            }
        },
        dismissButton = {}
    )
}