package com.example.personneleventsdashboard.data

import com.example.personneleventsdashboard.model.Person
import kotlinx.coroutines.flow.Flow

class PersonRepository(private val personDao: PersonDao) {

    // Get all people as a Flow (for observing in Compose)
    fun getAllPersons(): Flow<List<Person>> = personDao.getAllPersons()

    // Update a person (useful for updating shop/status/etc.)
    suspend fun updatePerson(person: Person) = personDao.updatePerson(person)

    // (Optional) Insert and delete methods
    suspend fun insertPerson(person: Person) = personDao.insertPerson(person)
    suspend fun deletePerson(person: Person) = personDao.deletePerson(person)

    // (Optional) Get by ID
    suspend fun getPersonById(id: Int): Person? = personDao.getPersonById(id)

    // (Optional) Search method if your DAO supports it
    fun searchPersons(query: String) = personDao.searchPersons(query)
}
