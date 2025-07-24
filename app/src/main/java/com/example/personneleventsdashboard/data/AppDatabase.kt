package com.example.personneleventsdashboard.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.personneleventsdashboard.model.Person
import com.example.personneleventsdashboard.model.Shop

@Database(
    entities = [Person::class, Shop::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun shopDao(): ShopDao
}
