package com.example.personneleventsdashboard.data

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.personneleventsdashboard.data.management.DataSeeder

object AppDatabaseProvider {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "personnel_events_database"
            )
                .addMigrations(MIGRATION_18_TO_21)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed data when database is created fresh
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                val seeder = DataSeeder(database)
                                seeder.seedInitialData()
                            }
                        }
                    }
                })
                .fallbackToDestructiveMigration() // For dev; remove or revise for prod!
                .build()
            INSTANCE = instance
            instance
        }
    }
}