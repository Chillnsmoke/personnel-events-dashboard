package com.example.personneleventsdashboard.data

import androidx.room.TypeConverter
import java.time.LocalDate

class DateConverters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) }
    }
}