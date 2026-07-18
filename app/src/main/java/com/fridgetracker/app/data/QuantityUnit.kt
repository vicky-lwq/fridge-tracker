package com.fridgetracker.app.data

/**
 * Weight/count unit for the optional quantity field. Not user-configurable,
 * mirrors the fixed-set approach already used for FoodCategory.
 */
enum class QuantityUnit(val displayName: String) {
    GRAM("克"),
    KILOGRAM("千克"),
    JIN("斤"),
    COUNT("个"),
    BOX("盒")
}
