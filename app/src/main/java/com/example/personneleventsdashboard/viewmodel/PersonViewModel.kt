package com.example.personneleventsdashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.personneleventsdashboard.data.PersonRepository
import com.example.personneleventsdashboard.model.Person
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PersonViewModel(
    private val repository: PersonRepository
) : ViewModel() {

    private val _people = MutableStateFlow<List<Person>>(emptyList())
    val people: StateFlow<List<Person>> = _people

    init {
        viewModelScope.launch {
            repository.getAllPersons().collect { list ->
                _people.value = list
            }
        }
    }

    fun updatePerson(person: Person) {
        viewModelScope.launch {
            repository.updatePerson(person)
        }
    }

    fun insertPerson(person: Person) {
        viewModelScope.launch {
            repository.insertPerson(person)
        }
    }
    // Optional: add insert, delete, getPersonById, search methods as needed
}
