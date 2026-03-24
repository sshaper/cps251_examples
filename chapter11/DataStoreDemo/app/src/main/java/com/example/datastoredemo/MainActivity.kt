package com.example.datastoredemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState

// MainActivity is the entry point of the Android application. It sets up the Compose UI
// and integrates the DataStoreViewModel to display and manage user preferences.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up the Compose UI for the activity.
        setContent {
            // Create the ViewModel once at the activity level so theme state can drive the whole app UI.
            val viewModel: DataStoreViewModel = viewModel()
            // Observe dark mode preference from DataStore-backed StateFlow.
            val darkMode by viewModel.darkMode.collectAsState()
            // Pick a Material color scheme based on the persisted preference.
            val colorScheme = if (darkMode) darkColorScheme() else lightColorScheme()

            // Keep system status bar icon style in sync with the selected theme.
            SideEffect {
                WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = !darkMode
            }

            // Applies the MaterialTheme to the entire UI, providing consistent styling.
            MaterialTheme(
                colorScheme = colorScheme
            ) {
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