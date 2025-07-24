package com.example.personneleventsdashboard.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.personneleventsdashboard.model.Person

@Database(
    entities = [Person::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
}
