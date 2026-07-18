package com.fridgetracker.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fridgetracker.app.data.FoodCategory
import com.fridgetracker.app.data.FoodItem
import com.fridgetracker.app.repository.FoodRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class FoodViewModel(private val repository: FoodRepository) : ViewModel() {

    val foods: StateFlow<List<FoodItem>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _selectedCategory = MutableStateFlow<FoodCategory?>(null)
    val selectedCategory: StateFlow<FoodCategory?> = _selectedCategory.asStateFlow()

    val filteredFoods: StateFlow<List<FoodItem>> =
        combine(foods, selectedCategory) { items, category ->
            if (category == null) items else items.filter { it.category == category }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** One-off "item was deleted" events for the list screen to surface as an undo Snackbar. */
    private val _deletedEvents = Channel<FoodItem>(Channel.BUFFERED)
    val deletedEvents: Flow<FoodItem> = _deletedEvents.receiveAsFlow()

    fun selectCategory(category: FoodCategory?) {
        _selectedCategory.value = category
    }

    fun addFood(name: String, category: FoodCategory, expiryDate: LocalDate) {
        viewModelScope.launch {
            repository.add(
                FoodItem(
                    name = name,
                    category = category,
                    addedDate = LocalDate.now(),
                    expiryDate = expiryDate
                )
            )
        }
    }

    fun updateFood(item: FoodItem) {
        viewModelScope.launch { repository.update(item) }
    }

    fun deleteFood(item: FoodItem) {
        viewModelScope.launch {
            repository.delete(item)
            _deletedEvents.send(item)
        }
    }

    fun undoDelete(item: FoodItem) {
        viewModelScope.launch { repository.restore(item) }
    }

    suspend fun getById(id: Long): FoodItem? = repository.getById(id)
}
