package com.example.datastoredemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel

// MainActivity is the entry point of the Android application. It sets up the Compose UI
// and integrates the DataStoreViewModel to display and manage user preferences.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure the window to use light status bar icons for a better visual experience.
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        // Set up the Compose UI for the activity.
        setContent {
            // Applies the MaterialTheme to the entire UI, providing consistent styling.
            MaterialTheme {
                // Surface is a composable that provides a background and elevation.
                Surface(
                    modifier = Modifier.fillMaxSize(), // Makes the surface fill the entire screen.
                    color = MaterialTheme.colorScheme.background // Sets the background color from the theme.
                ) {
                    // Creates and remembers an instance of DataStoreViewModel.
                    // The `viewModel()` function automatically handles the ViewModel's lifecycle.
                    val viewModel: DataStoreViewModel = viewModel()
                    // Displays the DataStoreScreen, passing the ViewModel to it.
                    // The UI elements in DataStoreScreen will observe data from this ViewModel.
                    DataStoreScreen(viewModel = viewModel)
                }
            }
        }
    }
}

/*Beause of passing the view model I did not do a preview*/