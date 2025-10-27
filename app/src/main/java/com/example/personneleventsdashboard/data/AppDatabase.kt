package com.example.personneleventsdashboard.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.personneleventsdashboard.model.Person
import com.example.personneleventsdashboard.model.Shop
import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.model.EventType
import com.example.personneleventsdashboard.model.TailNumber  // ADD THIS IMPORT
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Person::class, Shop::class, Event::class, EventType::class, TailNumber::class],
    version = 18, // INCREMENT
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

// Updated migration for the simplified position system:

val MIGRATION_17_TO_18 = object : Migration(17, 18) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add new simplified position column
        database.execSQL("ALTER TABLE shops ADD COLUMN displayPosition INTEGER")

        // Remove old complex position columns (optional - can keep for rollback)
        // database.execSQL("ALTER TABLE shops DROP COLUMN displayRow")
        // database.execSQL("ALTER TABLE shops DROP COLUMN displayColumn")
        // database.execSQL("ALTER TABLE shops DROP COLUMN positionInSection")

        // Set position mappings for existing shops
        database.execSQL("UPDATE shops SET displayPosition = 1 WHERE name = 'C130 AVENG Officer'")
        database.execSQL("UPDATE shops SET displayPosition = 2 WHERE name = 'LCPO'")
        database.execSQL("UPDATE shops SET displayPosition = 3 WHERE name = 'Maintenance Officer'")
        database.execSQL("UPDATE shops SET displayPosition = 4 WHERE name = 'Division Managers'")
        database.execSQL("UPDATE shops SET displayPosition = 5 WHERE name = 'Training'")
        database.execSQL("UPDATE shops SET displayPosition = 6 WHERE name = 'Flight Schedules'")
        database.execSQL("UPDATE shops SET displayPosition = 7 WHERE name = 'Maintenance Control'")
        database.execSQL("UPDATE shops SET displayPosition = 8 WHERE name = 'AVENG FlightPay'")
        database.execSQL("UPDATE shops SET displayPosition = 9 WHERE name = 'MPC Analyst'")

        // Column 1: Positions 10-14
        database.execSQL("UPDATE shops SET displayPosition = 10 WHERE name = 'AMO'")
        database.execSQL("UPDATE shops SET displayPosition = 11 WHERE name = 'Engine'")
        database.execSQL("UPDATE shops SET displayPosition = 12 WHERE name = 'Metal'")
        // Positions 13, 14 empty

        // Column 2: Positions 15-19
        database.execSQL("UPDATE shops SET displayPosition = 15 WHERE name = 'Prop'")
        database.execSQL("UPDATE shops SET displayPosition = 16 WHERE name = 'Load Cage'")
        database.execSQL("UPDATE shops SET displayPosition = 17 WHERE name = 'Line Crew'")
        // Positions 18, 19 empty

        // Column 3: Positions 20-24
        database.execSQL("UPDATE shops SET displayPosition = 20 WHERE name = 'Avionics'")
        // Positions 21, 22, 23, 24 empty

        // Column 4: Positions 25-29
        database.execSQL("UPDATE shops SET displayPosition = 25 WHERE name = 'QA'")
        database.execSQL("UPDATE shops SET displayPosition = 26 WHERE name = 'Sensor'")
        database.execSQL("UPDATE shops SET displayPosition = 27 WHERE name = 'Tool Room'")
        // Positions 28, 29 empty

        // Column 5: Positions 30-34
        database.execSQL("UPDATE shops SET displayPosition = 30 WHERE name = 'QA - Nights'")
        database.execSQL("UPDATE shops SET displayPosition = 31 WHERE name = 'Nights'")
        // Positions 32, 33, 34 empty
    }
}