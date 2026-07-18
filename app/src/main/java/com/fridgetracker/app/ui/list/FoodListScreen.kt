package com.fridgetracker.app.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fridgetracker.app.ui.components.CategoryFilterChipRow
import com.fridgetracker.app.ui.components.EmptyState
import com.fridgetracker.app.ui.components.FoodItemCard
import com.fridgetracker.app.viewmodel.FoodViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListScreen(
    viewModel: FoodViewModel,
    onAddFood: () -> Unit,
    onEditFood: (Long) -> Unit
) {
    val foods by viewModel.filteredFoods.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.deletedEvents.collect { deletedItem ->
            val result = snackbarHostState.showSnackbar(
                message = "已删除'${deletedItem.name}'",
                actionLabel = "撤销",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete(deletedItem)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("冰箱食物", style = MaterialTheme.typography.titleLarge) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddFood, shape = MaterialTheme.shapes.large) {
                Icon(Icons.Filled.Add, contentDescription = "新增食物")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            CategoryFilterChipRow(
                selected = selectedCategory,
                onSelect = viewModel::selectCategory,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (foods.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(foods, key = { it.id }) { item ->
                        FoodItemCard(
                            item = item,
                            onClick = { onEditFood(item.id) },
                            onDelete = { viewModel.deleteFood(item) }
                        )
                    }
                }
            }
        }
    }
}
