package com.fridgetracker.app.data

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromEpochDay(epochDay: Long?): LocalDate? = epochDay?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun toEpochDay(date: LocalDate?): Long? = date?.toEpochDay()

    @TypeConverter
    fun fromCategoryName(name: String?): FoodCategory? = name?.let { FoodCategory.valueOf(it) }

    @TypeConverter
    fun toCategoryName(category: FoodCategory?): String? = category?.name

    @TypeConverter
    fun fromQuantityUnitName(name: String?): QuantityUnit? = name?.let { QuantityUnit.valueOf(it) }

    @TypeConverter
    fun toQuantityUnitName(unit: QuantityUnit?): String? = unit?.name
}
