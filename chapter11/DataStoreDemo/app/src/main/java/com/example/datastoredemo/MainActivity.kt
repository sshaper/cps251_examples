package com.example.datastoredemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel

// MainActivity is the entry point of the Android application. It sets up the Compose UI
// and integrates the DataStoreViewModel to display and manage user preferences.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up the Compose UI for the activity.
        setContent {
            // Observe the same saved preference as the settings screen. Changes update
            // the app's colors, and DataStore restores the choice after an app restart.
            val viewModel: DataStoreViewModel = viewModel()
            val darkMode by viewModel.darkMode.collectAsState()
            val colorScheme = if (darkMode) darkColorScheme() else lightColorScheme()

            SideEffect {
                // Light status-bar appearance uses dark icons on a light background.
                WindowCompat.getInsetsController(window, window.decorView)
                    .isAppearanceLightStatusBars = !darkMode
            }

            MaterialTheme(colorScheme = colorScheme) {
                // Surface is a composable that provides a background and elevation.
                Surface(
                    modifier = Modifier.fillMaxSize(), // Makes the surface fill the entire screen.
                    color = MaterialTheme.colorScheme.background // Sets the background color from the theme.
                ) {
                    // Displays the DataStoreScreen, passing the ViewModel to it.
                    // The UI elements in DataStoreScreen will observe data from this ViewModel.
                    DataStoreScreen(viewModel = viewModel)
                }
            }
        }
    }
}

/*Beause of passing the view model I did not do a preview*/
