package com.fridgetracker.app.repository

import com.fridgetracker.app.data.ExpiryStatus
import com.fridgetracker.app.data.FoodDao
import com.fridgetracker.app.data.FoodItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class FoodRepository(private val dao: FoodDao) {

    fun observeAll(): Flow<List<FoodItem>> = dao.observeAll()

    suspend fun getById(id: Long): FoodItem? = dao.getById(id)

    suspend fun add(item: FoodItem): Long = dao.insert(item)

    suspend fun update(item: FoodItem) = dao.update(item)

    suspend fun delete(item: FoodItem) = dao.delete(item)

    /** Items with remaining days <= 3 (including already expired), for the daily notification check. */
    suspend fun getItemsNeedingAttention(today: LocalDate = LocalDate.now()): List<FoodItem> =
        dao.getAllSnapshot().filter { item ->
            ExpiryStatus.from(item.remainingDays(today)) != ExpiryStatus.NORMAL
        }
}
