package com.example.personneleventsdashboard.data

import com.example.personneleventsdashboard.model.TailNumber
import kotlinx.coroutines.flow.Flow

class TailNumberRepository(private val tailNumberDao: TailNumberDao) {

    fun getActiveTailNumbers(): Flow<List<TailNumber>> = tailNumberDao.getActiveTailNumbers()

    fun getAllTailNumbers(): Flow<List<TailNumber>> = tailNumberDao.getAllTailNumbers()

    suspend fun getTailNumberById(id: Int): TailNumber? = tailNumberDao.getTailNumberById(id)

    suspend fun insertTailNumber(tailNumber: TailNumber): Long = tailNumberDao.insertTailNumber(tailNumber)

    suspend fun updateTailNumber(tailNumber: TailNumber) = tailNumberDao.updateTailNumber(tailNumber)

    suspend fun deleteTailNumber(tailNumber: TailNumber) = tailNumberDao.deleteTailNumber(tailNumber)

    suspend fun deleteTailNumberById(tailNumberId: Int) = tailNumberDao.deleteTailNumberById(tailNumberId)
}