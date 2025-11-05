package com.example.personneleventsdashboard.ui.components.events

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.personneleventsdashboard.model.CalendarDay
import com.example.personneleventsdashboard.model.CalendarMonth
import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.model.EventType
import com.example.personneleventsdashboard.ui.theme.Border
import com.example.personneleventsdashboard.ui.theme.Charcoal
import com.example.personneleventsdashboard.ui.theme.Orange
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@Composable
fun CalendarHeader(
    yearMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onGoToToday: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous month arrow
        IconButton(
            onClick = onPreviousMonth,
            modifier = Modifier.size(48.dp)
        ) {
            Text(
                text = "◀",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
            )
        }

        // Center section with absolute positioning for Today button
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            // Month and year display - always centered
            Text(
                text = yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                fontSize = 58.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            // Today button - positioned based on past/future relative to current month
            if (yearMonth != YearMonth.now()) {
                val currentMonth = YearMonth.now()
                val isInPast = yearMonth.isBefore(currentMonth)
                val buttonOffset = if (isInPast) (-280).dp else 280.dp

                OutlinedButton(
                    onClick = onGoToToday,
                    modifier = Modifier
                        .height(40.dp)
                        .offset(x = buttonOffset),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Gray
                    ),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text(
                        text = "Today",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Next month arrow
        IconButton(
            onClick = onNextMonth,
            modifier = Modifier.size(48.dp)
        ) {
            Text(
                text = "▶",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
            )
        }
    }
}

@Composable
fun CalendarGrid(
    calendarMonth: CalendarMonth,
    events: List<Event>,
    eventTypes: List<EventType>,
    onDoubleClick: (LocalDate) -> Unit,
    onEventClick: (Event) -> Unit,
    onShowAllEvents: (LocalDate, List<Event>) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(5.dp, Border)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Day labels header (S-M-T-W-T-F-S)
            DayLabelsRow()

            // Calendar weeks
            calendarMonth.weeks.forEach { week ->
                CalendarWeekRow(
                    week = week,
                    events = events,
                    eventTypes = eventTypes,
                    onDoubleClick = onDoubleClick,
                    onEventClick = onEventClick,
                    onShowAllEvents = onShowAllEvents,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun DayLabelsRow() {
    val dayLabels = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
    ) {
        dayLabels.forEach { label ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .border(1.dp, Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun CalendarWeekRow(
    week: List<CalendarDay>,
    events: List<Event>,
    eventTypes: List<EventType>,
    onDoubleClick: (LocalDate) -> Unit,
    onEventClick: (Event) -> Unit,
    onShowAllEvents: (LocalDate, List<Event>) -> Unit,
    modifier: Modifier = Modifier
) {
    // Calculate multi-day event slots for the entire week
    val weekMultiDayEvents = events.filter { event ->
        event.startDate != event.endDate && // Is multi-day
                week.any { day -> day.date >= event.startDate && day.date <= event.endDate } // Overlaps with this week
    }.distinctBy { it.eventId }

    // FIXED: Assign slots to multi-day events based on actual availability for their specific date ranges
    val multiDayEventSlots = mutableMapOf<Int, Int>() // eventId to slot number

    // For each event, find the lowest available slot (starting from bottom: slot 4)
    weekMultiDayEvents.forEach { event ->
        val eventDates = week.filter { day ->
            day.date >= event.startDate && day.date <= event.endDate
        }.map { it.date }

        // Check each slot from bottom to top (4 to 0) to find the first available
        var assignedSlot = -1
        for (slot in 4 downTo 0) {
            var slotAvailable = true

            // Check if this slot is occupied by any other event that overlaps our date range
            for (otherEvent in weekMultiDayEvents) {
                if (otherEvent.eventId != event.eventId && multiDayEventSlots[otherEvent.eventId] == slot) {
                    // Check if this other event overlaps with any of our event's dates
                    val otherEventDates = week.filter { day ->
                        day.date >= otherEvent.startDate && day.date <= otherEvent.endDate
                    }.map { it.date }

                    if (eventDates.any { date -> otherEventDates.contains(date) }) {
                        slotAvailable = false
                        break
                    }
                }
            }

            if (slotAvailable) {
                assignedSlot = slot
                break
            }
        }

        if (assignedSlot != -1) {
            multiDayEventSlots[event.eventId] = assignedSlot
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        // First layer: Day cells with single-day events
        Row(modifier = Modifier.fillMaxWidth()) {
            week.forEach { day ->
                // Filter events for this specific day
                val dayEvents = events.filter { event ->
                    day.date >= event.startDate && day.date <= event.endDate
                }

                CalendarDayCell(
                    day = day,
                    events = dayEvents,
                    eventTypes = eventTypes,
                    multiDayEventSlots = multiDayEventSlots,
                    onDoubleClick = onDoubleClick,
                    onEventClick = onEventClick,
                    onShowAllEvents = onShowAllEvents,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Second layer: Multi-day event spans (overlaid on top)
        weekMultiDayEvents.forEach { event ->
            val slot = multiDayEventSlots[event.eventId] ?: return@forEach

            // Calculate span within this week
            val weekStartDate = week.first().date
            val weekEndDate = week.last().date
            val spanStartDate = maxOf(event.startDate, weekStartDate)
            val spanEndDate = minOf(event.endDate, weekEndDate)

            val startDayIndex = week.indexOfFirst { it.date == spanStartDate }
            val endDayIndex = week.indexOfFirst { it.date == spanEndDate }

            if (startDayIndex >= 0 && endDayIndex >= 0) {
                val eventType = eventTypes.find { it.eventTypeId == event.eventTypeId }

                MultiDayEventSpan(
                    event = event,
                    eventType = eventType,
                    startDayIndex = startDayIndex,
                    endDayIndex = endDayIndex,
                    slot = slot,
                    weekSize = week.size,
                    isFirstDayOfEvent = event.startDate == spanStartDate,
                    isLastDayOfEvent = event.endDate == spanEndDate,
                    onClick = { onEventClick(event) }
                )
            }
        }
    }
}

@Composable
fun BoxScope.MultiDayEventSpan(
    event: Event,
    eventType: EventType?,
    startDayIndex: Int,
    endDayIndex: Int,
    slot: Int,
    weekSize: Int,
    isFirstDayOfEvent: Boolean,
    isLastDayOfEvent: Boolean,
    onClick: () -> Unit
) {
    // CRITICAL: Match the exact spacing used in CalendarDayCell
    val eventHeight = 52.dp
    val totalEventSpace = 53.dp // This includes the 2dp spacing that's in the Spacer
    val dateNumberHeight = 52.dp // Match the actual space used by date number
    val topPadding = dateNumberHeight + 4.dp // Match CalendarDayCell's Spacer after date

    // Calculate vertical offset using the SAME logic as single-day events
    val verticalOffset = topPadding + (slot * totalEventSpace)

    // Get color from event type or use default
    val eventColor = if (eventType != null) {
        getEventColor(eventType.color)
    } else {
        Color.Red
    }

    // Status-based styling
    val (titleColor, titleDecoration) = when (event.status) {
        "Complete" -> Pair(Color.Green, null)
        "Cancelled" -> Pair(Color.Red, TextDecoration.LineThrough)
        "In Progress" -> Pair(Orange, null)
        else -> Pair(Color.Black, null)
    }

    // Use Row to position the event span correctly
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(eventHeight) // Use the exact same height as single-day events
            .offset(y = verticalOffset)
            .padding(
                top = 0.5.dp,
                bottom = 1.dp
            )
    ) {
        // Empty space for days before the event starts
        repeat(startDayIndex) {
            Spacer(modifier = Modifier.weight(1f))
        }

        // The actual event span
        Card(
            modifier = Modifier
                .weight((endDayIndex - startDayIndex + 1).toFloat())
                .height(eventHeight) // Explicit height to match single-day events
                .padding(
                    start = if (startDayIndex == 0) 4.dp else 4.dp,
                    end = if (endDayIndex == weekSize - 1) 4.dp else 4.dp,
                    top = 0.dp, // Remove top padding to match single-day events
                    bottom = 0.dp // Remove bottom padding to match single-day events
                )
                .clickable { onClick() },
            shape = RoundedCornerShape(
                topStart = if (isFirstDayOfEvent) 4.dp else 0.dp,
                bottomStart = if (isFirstDayOfEvent) 4.dp else 0.dp,
                topEnd = if (isLastDayOfEvent) 4.dp else 0.dp,
                bottomEnd = if (isLastDayOfEvent) 4.dp else 0.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = eventColor.copy(alpha = 0.4f)
            ),
            border = BorderStroke(1.dp, eventColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center // Changed from CenterStart to Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center, // Changed from Start to Center
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Event title
                    Text(
                        text = event.title,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium,
                        color = titleColor,
                        textDecoration = titleDecoration,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                    // Complete status indicator
                    if (event.status == "Complete") {
                        Text(
                            text = " ✓",
                            fontSize = 32.sp,
                            color = Color.Green,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 2.dp)
                        )
                    }
                    // In Progress status indicator
                    if (event.status == "In Progress") {
                        Text(
                            text = "...",
                            fontSize = 32.sp,
                            color = Orange,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 2.dp)
                        )
                    }

                    // Aircraft tail number
                    event.aircraftTailNumber?.let { tailNumber ->
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = tailNumber,
                            fontSize = 32.sp,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Empty space for days after the event ends
        repeat(weekSize - 1 - endDayIndex) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun CalendarDayCell(
    day: CalendarDay,
    events: List<Event>,
    eventTypes: List<EventType>,
    multiDayEventSlots: Map<Int, Int>, // eventId to slot number
    onDoubleClick: (LocalDate) -> Unit,
    onEventClick: (Event) -> Unit,
    onShowAllEvents: (LocalDate, List<Event>) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        day.isToday -> Color.White.copy(alpha = 0.6f)
        day.isCurrentMonth -> Color.White.copy(alpha = 0.4f)
        else -> Color.Gray.copy(alpha = 0.2f)
    }

    val textColor = when {
        day.isToday -> Charcoal.copy(alpha = 0.5f)
        day.isCurrentMonth -> Color.Black
        else -> Color.Gray
    }

    // Separate single-day and multi-day events
    val singleDayEvents = events.filter { it.startDate == it.endDate }
    val multiDayEvents = events.filter { it.startDate != it.endDate }

    // Calculate which slots are occupied by multi-day events
    val occupiedSlots = mutableSetOf<Int>()
    multiDayEvents.forEach { event ->
        multiDayEventSlots[event.eventId]?.let { slot ->
            occupiedSlots.add(slot)
        }
    }

    // Arrange single-day events in available slots
    val eventSlots = Array<Event?>(5) { null }

    // Place single-day events in unoccupied slots from top to bottom
    var eventIndex = 0
    for (slot in 0..4) {
        if (!occupiedSlots.contains(slot) && eventIndex < singleDayEvents.size) {
            eventSlots[slot] = singleDayEvents[eventIndex]
            eventIndex++
        }
    }

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .border(1.dp, Color.LightGray)
            .background(backgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple()
            ) {
                // Single click
            }
            .pointerInput(day.date) {
                detectTapGestures(
                    onDoubleTap = {
                        onDoubleClick(day.date)
                    }
                )
            }
            .padding(4.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = day.date.dayOfMonth.toString(),
                    fontSize = 40.sp,
                    fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal,
                    color = textColor
                )

                if (events.size > 5) {
                    OutlinedButton(
                        onClick = { onShowAllEvents(day.date, events) },
                        modifier = Modifier
                            .height(32.dp)
                            .width(64.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Charcoal,
                            containerColor = Color.White.copy(alpha = 0.1f)
                        ),
                        border = BorderStroke(1.dp, Charcoal.copy(alpha = .4f)),
                        contentPadding = PaddingValues(2.dp)
                    ) {
                        Text(
                            text = "View All",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Render single-day events only (multi-day events are rendered as overlays)
            for (slot in 0..4) {
                eventSlots[slot]?.let { event ->
                    val eventType = eventTypes.find { it.eventTypeId == event.eventTypeId }
                    EventIndicator(
                        event = event,
                        eventType = eventType,
                        onClick = { onEventClick(event) }
                    )
                } ?: run {
                    // Empty space for multi-day event slots
                    if (occupiedSlots.contains(slot)) {
                        Spacer(modifier = Modifier.height(50.dp))
                    } else {
                        Spacer(modifier = Modifier.height(52.dp))
                    }
                }

                if (slot < 4) {
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }
    }
}