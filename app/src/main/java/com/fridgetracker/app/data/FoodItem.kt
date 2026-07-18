package com.fridgetracker.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: FoodCategory?,
    val addedDate: LocalDate,
    val expiryDate: LocalDate,
    val quantity: Double? = null,
    val quantityUnit: QuantityUnit? = null
) {
    /** Days remaining until expiry. Negative means already expired. */
    fun remainingDays(today: LocalDate = LocalDate.now()): Long =
        java.time.temporal.ChronoUnit.DAYS.between(today, expiryDate)
}

enum class ExpiryStatus {
    NORMAL,
    SOON,
    EXPIRED;

    companion object {
        fun from(remainingDays: Long): ExpiryStatus = when {
            remainingDays < 0 -> EXPIRED
            remainingDays <= 3 -> SOON
            else -> NORMAL
        }
    }
}
