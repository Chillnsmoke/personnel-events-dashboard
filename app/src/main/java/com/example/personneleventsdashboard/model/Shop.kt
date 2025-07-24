package com.example.personneleventsdashboard.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shops")
data class Shop(
    @PrimaryKey(autoGenerate = true) val shopId: Int = 0,
    val name: String,
    val color: String? = null,
    val chief: String? = null
)
