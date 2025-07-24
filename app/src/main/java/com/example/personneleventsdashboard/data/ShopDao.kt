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
}
