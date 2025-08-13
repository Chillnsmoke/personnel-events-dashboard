package com.example.personneleventsdashboard.data

import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.model.EventType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class EventRepository(
    private val eventDao: EventDao,
    private val eventTypeDao: EventTypeDao
) {

    // Event operations
    fun getAllEvents(): Flow<List<Event>> = eventDao.getAllEvents()

    fun getEventsForDate(date: LocalDate): Flow<List<Event>> = eventDao.getEventsForDate(date)

    fun getEventsInDateRange(startDate: LocalDate, endDate: LocalDate): Flow<List<Event>> =
        eventDao.getEventsInDateRange(startDate, endDate)

    suspend fun getEventById(id: Int): Event? = eventDao.getEventById(id)

    suspend fun insertEvent(event: Event): Long = eventDao.insertEvent(event)

    suspend fun updateEvent(event: Event) = eventDao.updateEvent(event)

    suspend fun deleteEvent(event: Event) = eventDao.deleteEvent(event)

    suspend fun deleteEventById(eventId: Int) = eventDao.deleteEventById(eventId)

    // EventType operations
    fun getAllEventTypes(): Flow<List<EventType>> = eventTypeDao.getAllEventTypes()

    fun getPresetEventTypes(): Flow<List<EventType>> = eventTypeDao.getPresetEventTypes()

    suspend fun getEventTypeById(id: Int): EventType? = eventTypeDao.getEventTypeById(id)

    suspend fun insertEventType(eventType: EventType): Long = eventTypeDao.insertEventType(eventType)

    suspend fun updateEventType(eventType: EventType) = eventTypeDao.updateEventType(eventType)

    suspend fun deleteEventType(eventType: EventType) = eventTypeDao.deleteEventType(eventType)
}