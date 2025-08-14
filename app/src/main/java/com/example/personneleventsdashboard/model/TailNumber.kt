package com.example.personneleventsdashboard.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tail_numbers")
data class TailNumber(
    @PrimaryKey(autoGenerate = true) val tailNumberId: Int = 0,
    val number: String,
    val isActive: Boolean = true,
    val notes: String? = null
)