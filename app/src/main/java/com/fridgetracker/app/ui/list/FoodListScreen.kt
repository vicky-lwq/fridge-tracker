package com.fridgetracker.app.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fridgetracker.app.ui.components.CategoryDrawerContent
import com.fridgetracker.app.ui.components.EmptyState
import com.fridgetracker.app.ui.components.FoodItemCard
import com.fridgetracker.app.ui.components.StatsBar
import com.fridgetracker.app.viewmodel.FoodViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodListScreen(
    viewModel: FoodViewModel,
    onAddFood: () -> Unit,
    onEditFood: (Long) -> Unit
) {
    val allFoods by viewModel.foods.collectAsState()
    val foods by viewModel.filteredFoods.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            CategoryDrawerContent(
                selectedCategory = selectedCategory,
                onSelectCategory = viewModel::selectCategory,
                onItemClick = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("冰了个箱", style = MaterialTheme.typography.titleLarge) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "分类菜单")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onAddFood, shape = MaterialTheme.shapes.large) {
                    Icon(Icons.Filled.Add, contentDescription = "新增食物")
                }
            }
        ) { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {
                StatsBar(
                    items = allFoods,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
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
}
