package com.example.personneleventsdashboard.data.management

import com.example.personneleventsdashboard.data.TailNumberRepository
import com.example.personneleventsdashboard.model.TailNumber
import kotlinx.coroutines.flow.first

class TailNumberManager(
    private val repository: TailNumberRepository
) {

    suspend fun addTailNumber(number: String): Result<Long> {
        return try {
            // Check for duplicates
            val existing = repository.getAllTailNumbers().first()
            if (existing.any { it.number.equals(number, ignoreCase = true) }) {
                Result.failure(Exception("Tail number $number already exists"))
            } else {
                val id = repository.insertTailNumber(
                    TailNumber(
                        number = number.trim(),
                        isActive = true, // Always active, no user control
                        notes = "" // No notes needed
                    )
                )
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateTailNumber(tailNumber: TailNumber): Result<Unit> {
        return try {
            repository.updateTailNumber(tailNumber)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteTailNumber(tailNumber: TailNumber): Result<Unit> {
        return try {
            repository.deleteTailNumber(tailNumber)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}