package com.example.personneleventsdashboard.data

import androidx.room.*
import com.example.personneleventsdashboard.model.Person
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Query("SELECT * FROM persons ORDER BY lastName, firstName")
    fun getAllPersons(): Flow<List<Person>>

    @Query("SELECT * FROM persons WHERE personId = :id")
    suspend fun getPersonById(id: Int): Person?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPerson(person: Person): Long

    @Update
    suspend fun updatePerson(person: Person)

    @Delete
    suspend fun deletePerson(person: Person)

    // Example search: Find by name, rank, shop, or qualification (case-insensitive)
    @Query("""
        SELECT * FROM persons 
        WHERE lastName LIKE '%' || :query || '%' 
           OR firstName LIKE '%' || :query || '%'
           OR rank LIKE '%' || :query || '%'
           OR qualifications LIKE '%' || :query || '%'
        ORDER BY lastName, firstName
    """)
    fun searchPersons(query: String): Flow<List<Person>>
}
