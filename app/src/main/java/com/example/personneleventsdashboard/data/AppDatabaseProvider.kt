package com.example.personneleventsdashboard.data

import android.content.Context
import androidx.room.Room

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
                .addMigrations(MIGRATION_17_TO_18)
                .fallbackToDestructiveMigration() // For dev; remove or revise for prod!
                .build()
            INSTANCE = instance
            instance
        }
    }
}
