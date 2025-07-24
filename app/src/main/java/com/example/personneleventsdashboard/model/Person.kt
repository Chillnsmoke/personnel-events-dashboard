package com.example.personneleventsdashboard.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class Person(
    @PrimaryKey(autoGenerate = true) val personId: Int = 0,
    val lastName: String,
    val firstName: String,
    val rank: String,
    val shopId: Int,
    val phoneNumber: String,
    val qualifications: String,
    val status: String = "Normal" // default value
)
