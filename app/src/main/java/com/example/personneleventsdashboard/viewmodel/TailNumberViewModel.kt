package com.example.personneleventsdashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.personneleventsdashboard.data.TailNumberRepository
import com.example.personneleventsdashboard.model.TailNumber
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TailNumberViewModel(
    private val repository: TailNumberRepository
) : ViewModel() {

    private val _tailNumbers = MutableStateFlow<List<TailNumber>>(emptyList())
    val tailNumbers: StateFlow<List<TailNumber>> = _tailNumbers

    init {
        viewModelScope.launch {
            repository.getActiveTailNumbers().collect { list ->
                _tailNumbers.value = list
            }
        }
    }

    fun insertTailNumber(tailNumber: TailNumber) {
        viewModelScope.launch {
            repository.insertTailNumber(tailNumber)
        }
    }

    fun updateTailNumber(tailNumber: TailNumber) {
        viewModelScope.launch {
            repository.updateTailNumber(tailNumber)
        }
    }

    fun deleteTailNumber(tailNumber: TailNumber) {
        viewModelScope.launch {
            repository.deleteTailNumber(tailNumber)
        }
    }
}

class TailNumberViewModelFactory(
    private val repository: TailNumberRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TailNumberViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TailNumberViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}