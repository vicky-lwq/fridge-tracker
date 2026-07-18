package com.fridgetracker.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fridgetracker.app.ui.addedit.AddEditFoodScreen
import com.fridgetracker.app.ui.list.FoodListScreen
import com.fridgetracker.app.viewmodel.FoodViewModel

private const val ROUTE_LIST = "list"
private const val ROUTE_ADD_EDIT = "addEdit?foodId={foodId}"
private const val ARG_FOOD_ID = "foodId"
private const val NO_FOOD_ID = -1L

@Composable
fun FridgeNavHost(viewModel: FoodViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ROUTE_LIST) {
        composable(ROUTE_LIST) {
            FoodListScreen(
                viewModel = viewModel,
                onAddFood = { navController.navigate("addEdit") },
                onEditFood = { id -> navController.navigate("addEdit?foodId=$id") }
            )
        }
        composable(
            route = ROUTE_ADD_EDIT,
            arguments = listOf(
                navArgument(ARG_FOOD_ID) {
                    type = NavType.LongType
                    defaultValue = NO_FOOD_ID
                }
            )
        ) { backStackEntry ->
            val foodId = backStackEntry.arguments?.getLong(ARG_FOOD_ID) ?: NO_FOOD_ID
            AddEditFoodScreen(
                viewModel = viewModel,
                foodId = if (foodId == NO_FOOD_ID) null else foodId,
                onDone = { navController.popBackStack() }
            )
        }
    }
}
