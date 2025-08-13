package com.example.personneleventsdashboard.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "event_types")
data class EventType(
    @PrimaryKey(autoGenerate = true) val eventTypeId: Int = 0,
    val name: String,
    val description: String,
    val iconName: String? = null, // For preset events with icons
    val color: String? = null,    // Hex color code for event display
    val isPreset: Boolean = true  // true for predefined events, false for custom
)

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val eventId: Int = 0,
    val title: String,
    val description: String? = null,
    val startDate: LocalDate,
    val endDate: LocalDate,        // Same as startDate for single-day events
    val eventTypeId: Int? = null,  // References EventType for preset events, null for custom
    val aircraftTailNumber: String? = null,
    val status: String = "Scheduled", // Scheduled, In Progress, Complete, Cancelled
    val createdDate: LocalDate = LocalDate.now(),
    val isAllDay: Boolean = true,  // For future time-specific events
    val notes: String? = null
)