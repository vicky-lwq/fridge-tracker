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

    /** Used to restore an item after the user taps "撤销" on the delete Snackbar.
     * Reinserted as a new row (id reset to 0) since the original row was already deleted. */
    suspend fun restore(item: FoodItem) = dao.insert(item.copy(id = 0))

    /** Items with remaining days <= 3 (including already expired), for the daily notification check. */
    suspend fun getItemsNeedingAttention(today: LocalDate = LocalDate.now()): List<FoodItem> =
        dao.getAllSnapshot().filter { item ->
            ExpiryStatus.from(item.remainingDays(today)) != ExpiryStatus.NORMAL
        }
}
