package com.example.personneleventsdashboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.personneleventsdashboard.data.AppDatabaseProvider
import com.example.personneleventsdashboard.data.ShopDao
import com.example.personneleventsdashboard.model.Shop
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ShopViewModel(application: Application) : AndroidViewModel(application) {
    private val shopDao: ShopDao = AppDatabaseProvider.getDatabase(application).shopDao()

    // Expose shops as Flow for reactive UI
    val allShops: Flow<List<Shop>> = shopDao.getAllShops()
    val activeShops: Flow<List<Shop>> = shopDao.getActiveShops()
    val shopsOrderedByPosition: Flow<List<Shop>> = shopDao.getActiveShopsOrderedByPosition()
    val unassignedShops: Flow<List<Shop>> = shopDao.getUnassignedShops()

    /**
     * Add a new shop (will be unassigned until positioned via layout management)
     */
    fun addShop(shop: Shop, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                shopDao.insertShop(shop)
                onResult(Result.success(Unit))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    /**
     * Update an existing shop (properties only - position managed separately)
     */
    fun updateShop(shop: Shop, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                shopDao.updateShop(shop)
                onResult(Result.success(Unit))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    /**
     * Update shop position in layout
     */
    fun updateShopPosition(shopId: Int, newPosition: Int?, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                // Check for position conflicts if assigning to a position
                if (newPosition != null) {
                    val hasConflict = shopDao.checkPositionConflict(newPosition, shopId) > 0
                    if (hasConflict) {
                        // Clear any existing shop at this position first
                        clearPosition(newPosition)
                    }
                }

                // Update the shop's position
                shopDao.updateShopPosition(shopId, newPosition)
                onResult(Result.success(Unit))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    /**
     * Move shop to specific position by name, handling conflicts automatically
     */
    fun moveShopToPosition(shopName: String, position: Int?, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                if (shopName == "None") {
                    // Remove any existing shop from this position
                    if (position != null) {
                        val existingShop = shopDao.getShopAtPosition(position)
                        existingShop?.let { shop ->
                            shopDao.clearShopPosition(shop.shopId)
                        }
                    }
                    onResult(Result.success(Unit))
                } else {
                    // Check for conflicts and clear position if needed
                    if (position != null) {
                        val existingShop = shopDao.getShopAtPosition(position)
                        existingShop?.let { shop ->
                            shopDao.clearShopPosition(shop.shopId)
                        }
                    }

                    // Update the shop's position by name
                    shopDao.updateShopPositionByName(shopName, position)
                    onResult(Result.success(Unit))
                }
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    /**
     * Remove shop from layout (set position to null)
     */
    fun removeShopFromLayout(shopId: Int, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                shopDao.clearShopPosition(shopId)
                onResult(Result.success(Unit))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    /**
     * Delete a shop (hard delete)
     */
    fun deleteShop(shop: Shop, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                shopDao.deleteShop(shop) // Hard delete - completely removes the record
                onResult(Result.success(Unit))
            } catch (e: Exception) {
                onResult(Result.failure(e))
            }
        }
    }

    /**
     * Get shop at specific position
     */
    suspend fun getShopAtPosition(position: Int): Shop? {
        return try {
            shopDao.getShopAtPosition(position)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Get shops in specific layout sections
     */
    fun getRow1Shops(): Flow<List<Shop>> = shopDao.getRow1Shops()
    fun getRow2Shops(): Flow<List<Shop>> = shopDao.getRow2Shops()
    fun getRow3Shops(): Flow<List<Shop>> = shopDao.getRow3Shops()
    fun getColumnShops(): Flow<List<Shop>> = shopDao.getColumnShops()

    /**
     * Get shops in specific column (1-5)
     */
    fun getShopsInColumn(columnNumber: Int): Flow<List<Shop>> {
        val startPos = 10 + ((columnNumber - 1) * 5) // Column 1: 10-14, Column 2: 15-19, etc.
        val endPos = startPos + 4
        return shopDao.getShopsInColumn(startPos, endPos)
    }

    /**
     * Get shops in position range
     */
    fun getShopsInRange(startPosition: Int, endPosition: Int): Flow<List<Shop>> {
        return shopDao.getShopsInPositionRange(startPosition, endPosition)
    }

    /**
     * Helper function to clear a position (used internally for conflict resolution)
     */
    private suspend fun clearPosition(position: Int) {
        val existingShop = shopDao.getShopAtPosition(position)
        existingShop?.let { shop ->
            shopDao.clearShopPosition(shop.shopId)
        }
    }

    /**
     * Get next available position for new shop assignment
     */
    suspend fun getNextAvailablePosition(): Int {
        return try {
            // Find first empty position from 1-34
            for (position in 1..34) {
                val shop = shopDao.getShopAtPosition(position)
                if (shop == null) {
                    return position
                }
            }
            // If all positions are taken, return a position beyond the layout
            35
        } catch (e: Exception) {
            1 // Default to position 1 if error
        }
    }

    /**
     * Validate position is within valid range
     */
    fun isValidPosition(position: Int): Boolean {
        return position in 1..34
    }

    /**
     * Get position description for UI display
     */
    fun getPositionDescription(position: Int?): String {
        return when (position) {
            null -> "Unassigned"
            1 -> "Row 1, Position 1"
            in 2..4 -> "Row 2, Position ${position - 1}"
            in 5..9 -> "Row 3, Position ${position - 4}"
            in 10..14 -> "Column 1, Position ${position - 9}"
            in 15..19 -> "Column 2, Position ${position - 14}"
            in 20..24 -> "Column 3, Position ${position - 19}"
            in 25..29 -> "Column 4, Position ${position - 24}"
            in 30..34 -> "Column 5, Position ${position - 29}"
            else -> "Invalid Position"
        }
    }
}