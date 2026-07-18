package com.fridgetracker.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

    @Query("SELECT * FROM food_items ORDER BY expiryDate ASC")
    fun observeAll(): Flow<List<FoodItem>>

    @Query("SELECT * FROM food_items WHERE id = :id")
    suspend fun getById(id: Long): FoodItem?

    @Query("SELECT * FROM food_items")
    suspend fun getAllSnapshot(): List<FoodItem>

    @Insert
    suspend fun insert(item: FoodItem): Long

    @Update
    suspend fun update(item: FoodItem)

    @Delete
    suspend fun delete(item: FoodItem)
}
