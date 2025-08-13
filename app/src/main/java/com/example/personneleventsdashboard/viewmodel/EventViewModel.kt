package com.example.personneleventsdashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.personneleventsdashboard.data.EventRepository
import com.example.personneleventsdashboard.model.Event
import com.example.personneleventsdashboard.model.EventType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class EventViewModel(
    private val repository: EventRepository
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    private val _eventTypes = MutableStateFlow<List<EventType>>(emptyList())
    val eventTypes: StateFlow<List<EventType>> = _eventTypes

    init {
        viewModelScope.launch {
            repository.getAllEvents().collect { list ->
                _events.value = list
            }
        }

        viewModelScope.launch {
            repository.getAllEventTypes().collect { list ->
                _eventTypes.value = list
            }
        }
    }

    fun getEventsForDate(date: LocalDate): List<Event> {
        return _events.value.filter { event ->
            date >= event.startDate && date <= event.endDate
        }
    }

    fun insertEvent(event: Event) {
        viewModelScope.launch {
            repository.insertEvent(event)
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            repository.updateEvent(event)
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            repository.deleteEvent(event)
        }
    }

    fun getPresetEventTypes(): List<EventType> {
        return _eventTypes.value.filter { it.isPreset }
    }
}

class EventViewModelFactory(
    private val repository: EventRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}