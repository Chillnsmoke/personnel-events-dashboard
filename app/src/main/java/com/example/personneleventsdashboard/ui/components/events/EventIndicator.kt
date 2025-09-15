package com.example.personneleventsdashboard.ui.components.events

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.model.EventType
import com.example.personneleventsdashboard.ui.theme.Orange

@Composable
fun EventIndicator(
    event: Event,
    eventType: EventType?,
    onClick: () -> Unit
) {
    // Get color from event type or use default
    val eventColor = if (eventType != null) {
        getEventColor(eventType.color)
    } else {
        Color.Black
    }

    // Get icon from event type
    val eventIcon = if (eventType != null) {
        getEventIcon(eventType.iconName)
    } else {
        "\uD83D\uDCCC"
    }

    // Status-based styling
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
            Orange,
            null,
            "" // No prefix, dots will be added as suffix
        )
        else -> Triple(Color.Black, null, "") // "Scheduled" - default
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = eventColor.copy(alpha = 0.3f)
        ),
        border = BorderStroke(1.dp, eventColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Event type icon
            Text(
                text = eventIcon,
                fontSize = 40.sp,
                modifier = Modifier.padding(end = 2.dp)
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
                        fontSize = 32.sp,
                        color = Color.Green,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                }

                // Event title with status styling
                Text(
                    text = event.title,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium,
                    color = titleColor,
                    textDecoration = titleDecoration,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false) // Don't force fill
                )

                // In Progress status - orange dots (immediately after title)
                if (event.status == "In Progress") {
                    Text(
                        text = "...",
                        fontSize = 32.sp,
                        color = Orange,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }

            // Aircraft tail number (right-aligned, separate from title)
            event.aircraftTailNumber?.let { tailNumber ->
                Text(
                    text = tailNumber,
                    fontSize = 32.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}