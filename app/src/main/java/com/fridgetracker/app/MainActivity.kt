package com.fridgetracker.app

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fridgetracker.app.ui.navigation.FridgeNavHost
import com.fridgetracker.app.ui.theme.FridgeTrackerTheme
import com.fridgetracker.app.viewmodel.FoodViewModel
import com.fridgetracker.app.viewmodel.FoodViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FridgeTrackerTheme {
                val app = application as FridgeTrackerApplication
                val viewModel = viewModel<FoodViewModel>(factory = FoodViewModelFactory(app.repository))

                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { /* no-op: worker checks permission again before each notification */ }

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                FridgeNavHost(viewModel = viewModel)
            }
        }
    }
}
