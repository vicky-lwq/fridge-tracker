package com.fridgetracker.app.data

/**
 * Fixed set of food categories. Not user-configurable by design (CLAUDE.md).
 */
enum class FoodCategory(val displayName: String) {
    PRODUCE("蔬菜水果"),
    MEAT("肉类"),
    SEAFOOD("海鲜"),
    DAIRY("蛋奶"),
    FROZEN("速冻食品"),
    BEVERAGE("饮品"),
    CONDIMENT("调味品"),
    OTHER("其他")
}
