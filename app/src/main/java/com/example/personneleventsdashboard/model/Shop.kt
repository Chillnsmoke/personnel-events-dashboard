package com.example.personneleventsdashboard.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shops")
data class Shop(
    @PrimaryKey(autoGenerate = true) val shopId: Int = 0,
    val name: String,
    val color: String? = null,
    val chief: String? = null,
    val displayPosition: Int? = null,     // Single position index (1-34, null = unassigned)
    val isActive: Boolean = true          // Allow shops to be disabled without deletion
)

/**
 * Position mapping:
 * 1 = Row 1, Position 1 (AVENG Officer)
 * 2-4 = Row 2, Positions 1-3 (LCPO, MX Officer, Division Managers)
 * 5-9 = Row 3, Positions 1-5 (Training, Flight Schedules, etc.)
 * 10-14 = Column 1, Positions 1-5 (AMO, Engine, Metal, etc.)
 * 15-19 = Column 2, Positions 1-5 (Prop, Load Cage, Line Crew, etc.)
 * 20-24 = Column 3, Positions 1-5 (Avionics, etc.)
 * 25-29 = Column 4, Positions 1-5 (QA, Sensor, Tool Room, etc.)
 * 30-34 = Column 5, Positions 1-5 (QA - Nights, Nights, etc.)
 */