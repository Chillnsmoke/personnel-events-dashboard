package com.example.personneleventsdashboard.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.personneleventsdashboard.model.Person
import com.example.personneleventsdashboard.model.Shop
import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.model.EventType
import com.example.personneleventsdashboard.model.TailNumber  // ADD THIS IMPORT

@Database(
    entities = [Person::class, Shop::class, Event::class, EventType::class, TailNumber::class],
    version = 14, // INCREMENT
    exportSchema = false
)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun shopDao(): ShopDao
    abstract fun eventDao(): EventDao
    abstract fun eventTypeDao(): EventTypeDao
    abstract fun tailNumberDao(): TailNumberDao
}