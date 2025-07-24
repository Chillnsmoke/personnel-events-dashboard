package com.example.personneleventsdashboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.personneleventsdashboard.data.AppDatabaseProvider
import com.example.personneleventsdashboard.model.Person
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PersonViewModel(application: Application) : AndroidViewModel(application) {

    private val personDao = AppDatabaseProvider.getDatabase(application).personDao()

    private val _people = MutableStateFlow<List<Person>>(emptyList())
    val people: StateFlow<List<Person>> = _people

    init {
        viewModelScope.launch {
            personDao.getAllPersons().collect { list ->
                _people.value = list
            }
        }
    }
}
