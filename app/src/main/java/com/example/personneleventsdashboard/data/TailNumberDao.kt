package com.example.personneleventsdashboard.data

import androidx.room.*
import com.example.personneleventsdashboard.model.TailNumber
import kotlinx.coroutines.flow.Flow

@Dao
interface TailNumberDao {

    @Query("SELECT * FROM tail_numbers WHERE isActive = 1 ORDER BY number ASC")
    fun getActiveTailNumbers(): Flow<List<TailNumber>>

    @Query("SELECT * FROM tail_numbers ORDER BY number ASC")
    fun getAllTailNumbers(): Flow<List<TailNumber>>

    @Query("SELECT * FROM tail_numbers WHERE tailNumberId = :id")
    suspend fun getTailNumberById(id: Int): TailNumber?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTailNumber(tailNumber: TailNumber): Long

    @Update
    suspend fun updateTailNumber(tailNumber: TailNumber)

    @Delete
    suspend fun deleteTailNumber(tailNumber: TailNumber)

    @Query("DELETE FROM tail_numbers WHERE tailNumberId = :tailNumberId")
    suspend fun deleteTailNumberById(tailNumberId: Int)
}