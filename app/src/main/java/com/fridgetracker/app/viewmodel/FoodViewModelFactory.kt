package com.fridgetracker.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fridgetracker.app.repository.FoodRepository

class FoodViewModelFactory(private val repository: FoodRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(FoodViewModel::class.java)) {
            "Unknown ViewModel class: $modelClass"
        }
        return FoodViewModel(repository) as T
    }
}
