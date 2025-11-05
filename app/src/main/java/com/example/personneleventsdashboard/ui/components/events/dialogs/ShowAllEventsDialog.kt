package com.example.personneleventsdashboard.ui.components.events.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.model.EventType
import com.example.personneleventsdashboard.ui.components.events.getEventColor
import com.example.personneleventsdashboard.ui.components.events.getEventIcon
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ShowAllEventsDialog(
    date: LocalDate,
    events: List<Event>,
    eventTypes: List<EventType>,
    onDismiss: () -> Unit,
    onEventClick: (Event) -> Unit
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
                .height(500.dp), // Adequate height for event list
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
                    "Events for ${date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))}",
                    fontSize = 18.sp,  // Same as other dialog titles
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray  // Same gray color for headers
                )

                // Events List Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),  // Take remaining space
                    colors = CardDefaults.cardColors(
                        containerColor = Color.LightGray.copy(alpha = 0.1f)  // Same as other dialogs
                    )
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(events) { event ->
                            val eventType = eventTypes.find { it.eventTypeId == event.eventTypeId }

                            // Status-based styling - MATCHING EventIndicator logic
                            val (titleColor, titleDecoration, statusIndicator) = when (event.status) {
                                "Complete" -> Triple(
                                    Color.Green,
                                    null,
                                    "✓ " // Green checkmark prefix
                                )
                                "Cancelled" -> Triple(
                                    Color.Red,
                                    TextDecoration.LineThrough,
                                    "" // No prefix, just red strikethrough
                                )
                                "In Progress" -> Triple(
                                    Color.Yellow,
                                    null,
                                    "" // No prefix, dots will be added as suffix
                                )
                                else -> Triple(Color.White, null, "") // "Scheduled" - white for dark theme
                            }

                            // Create a larger version of EventIndicator for the dialog
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(45.dp)
                                    .clickable {
                                        onEventClick(event)
                                        onDismiss()
                                    },
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Black.copy(alpha = 0.3f)  // Dark background for event cards
                                ),
                                border = BorderStroke(
                                    1.dp,
                                    Color.Gray
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Event type icon
                                    Text(
                                        text = if (eventType != null) getEventIcon(eventType.iconName) else "📅",
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )

                                    // Title section with status indicators - takes up available space
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Complete status - green checkmark prefix
                                        if (event.status == "Complete") {
                                            Text(
                                                text = "✓",
                                                fontSize = 16.sp,
                                                color = Color.Green,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(end = 4.dp)
                                            )
                                        }

                                        // Event title with status styling
                                        Text(
                                            text = event.title,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = titleColor,
                                            textDecoration = titleDecoration,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f, fill = false) // Don't force fill
                                        )

                                        // In Progress status - yellow dots (immediately after title)
                                        if (event.status == "In Progress") {
                                            Text(
                                                text = "...",
                                                fontSize = 16.sp,
                                                color = Color.Yellow,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(start = 4.dp)
                                            )
                                        }
                                    }

                                    // Right-aligned section - separate from title
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        event.aircraftTailNumber?.let { tailNumber ->
                                            Text(
                                                text = tailNumber,
                                                fontSize = 14.sp,
                                                color = Color.LightGray,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Text(
                                            text = event.status,
                                            fontSize = 12.sp,
                                            color = when (event.status) {
                                                "Complete" -> Color.Green
                                                "In Progress" -> Color.Yellow
                                                "Cancelled" -> Color.Red
                                                else -> Color.LightGray
                                            }
                                        )
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
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Close button - same styling as other dialogs
                    OutlinedButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray),
                        border = BorderStroke(2.dp, Color.Gray),
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text("Close", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}