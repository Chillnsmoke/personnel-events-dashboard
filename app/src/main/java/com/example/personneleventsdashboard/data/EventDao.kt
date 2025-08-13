package com.example.personneleventsdashboard.data

import androidx.room.*
import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.model.EventType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface EventDao {

    @Query("SELECT * FROM events ORDER BY startDate ASC")
    fun getAllEvents(): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE startDate <= :date AND endDate >= :date ORDER BY startDate ASC")
    fun getEventsForDate(date: LocalDate): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE startDate >= :startDate AND startDate <= :endDate ORDER BY startDate ASC")
    fun getEventsInDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE eventId = :id")
    suspend fun getEventById(id: Int): Event?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event): Long

    @Update
    suspend fun updateEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("DELETE FROM events WHERE eventId = :eventId")
    suspend fun deleteEventById(eventId: Int)
}

@Dao
interface EventTypeDao {

    @Query("SELECT * FROM event_types ORDER BY name ASC")
    fun getAllEventTypes(): Flow<List<EventType>>

    @Query("SELECT * FROM event_types WHERE isPreset = 1 ORDER BY name ASC")
    fun getPresetEventTypes(): Flow<List<EventType>>

    @Query("SELECT * FROM event_types WHERE eventTypeId = :id")
    suspend fun getEventTypeById(id: Int): EventType?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventType(eventType: EventType): Long

    @Update
    suspend fun updateEventType(eventType: EventType)

    @Delete
    suspend fun deleteEventType(eventType: EventType)
}