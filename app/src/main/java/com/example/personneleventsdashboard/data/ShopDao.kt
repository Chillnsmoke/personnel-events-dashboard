package com.example.personneleventsdashboard.data

import androidx.room.*
import com.example.personneleventsdashboard.model.Shop
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Query("SELECT * FROM shops ORDER BY name")
    fun getAllShops(): Flow<List<Shop>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShop(shop: Shop): Long

    @Update
    suspend fun updateShop(shop: Shop)

    @Delete
    suspend fun deleteShop(shop: Shop)

    // NEW: Simplified position-based queries
    @Query("SELECT * FROM shops WHERE isActive = 1 ORDER BY displayPosition ASC")
    fun getActiveShopsOrderedByPosition(): Flow<List<Shop>>

    @Query("SELECT * FROM shops WHERE isActive = 1")
    fun getActiveShops(): Flow<List<Shop>>

    @Query("SELECT * FROM shops WHERE displayPosition = :position AND isActive = 1")
    suspend fun getShopAtPosition(position: Int): Shop?

    @Query("SELECT * FROM shops WHERE displayPosition IS NULL AND isActive = 1")
    fun getUnassignedShops(): Flow<List<Shop>>

    // NEW: Position management queries
    @Query("SELECT COUNT(*) FROM shops WHERE displayPosition = :position AND isActive = 1 AND shopId != :excludeShopId")
    suspend fun checkPositionConflict(position: Int, excludeShopId: Int): Int

    @Query("SELECT MAX(displayPosition) FROM shops WHERE isActive = 1")
    suspend fun getMaxPosition(): Int?

    @Query("SELECT MIN(displayPosition) FROM shops WHERE displayPosition IS NULL OR displayPosition = 0")
    suspend fun getNextAvailablePosition(): Int?

    // NEW: Bulk position updates (useful for reordering)
    @Query("UPDATE shops SET displayPosition = :newPosition WHERE shopId = :shopId")
    suspend fun updateShopPosition(shopId: Int, newPosition: Int?)

    // NEW: Update position by shop name (needed for dropdown management)
    @Query("UPDATE shops SET displayPosition = :newPosition WHERE name = :shopName AND isActive = 1")
    suspend fun updateShopPositionByName(shopName: String, newPosition: Int?)

    // NEW: Get shop by name
    @Query("SELECT * FROM shops WHERE name = :shopName AND isActive = 1")
    suspend fun getShopByName(shopName: String): Shop?

    // NEW: Clear position (set to null)
    @Query("UPDATE shops SET displayPosition = NULL WHERE shopId = :shopId")
    suspend fun clearShopPosition(shopId: Int)

    // Management queries
    @Query("UPDATE shops SET isActive = 0 WHERE shopId = :shopId")
    suspend fun deactivateShop(shopId: Int)

    @Query("UPDATE shops SET isActive = 1 WHERE shopId = :shopId")
    suspend fun reactivateShop(shopId: Int)

    // NEW: Position range queries (useful for specific layout sections)
    @Query("SELECT * FROM shops WHERE displayPosition BETWEEN :startPosition AND :endPosition AND isActive = 1 ORDER BY displayPosition")
    fun getShopsInPositionRange(startPosition: Int, endPosition: Int): Flow<List<Shop>>

    // NEW: Row-specific queries for layout management
    @Query("SELECT * FROM shops WHERE displayPosition = 1 AND isActive = 1") // Row 1
    fun getRow1Shops(): Flow<List<Shop>>

    @Query("SELECT * FROM shops WHERE displayPosition BETWEEN 2 AND 4 AND isActive = 1 ORDER BY displayPosition") // Row 2
    fun getRow2Shops(): Flow<List<Shop>>

    @Query("SELECT * FROM shops WHERE displayPosition BETWEEN 5 AND 9 AND isActive = 1 ORDER BY displayPosition") // Row 3
    fun getRow3Shops(): Flow<List<Shop>>

    @Query("SELECT * FROM shops WHERE displayPosition BETWEEN 10 AND 34 AND isActive = 1 ORDER BY displayPosition") // Columns
    fun getColumnShops(): Flow<List<Shop>>

    // NEW: Column-specific queries
    @Query("SELECT * FROM shops WHERE displayPosition BETWEEN :startPos AND :endPos AND isActive = 1 ORDER BY displayPosition")
    fun getShopsInColumn(startPos: Int, endPos: Int): Flow<List<Shop>>
}