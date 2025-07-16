package com.example.book

// Android Framework imports
import android.os.Bundle

// Activity and Compose imports
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

// Compose UI imports
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Navigation imports
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * MainActivity is the entry point of the application.
 * It sets up the Compose UI and manages the shared ViewModel for state management.
 */
class MainActivity : ComponentActivity() {
    // Create a single instance of the ViewModel for the entire activity
    // This ensures state is preserved across configuration changes and shared between screens
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure the window to use light status bar icons for better visibility
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        // Set up the Compose UI with Material Design theme
        setContent {
            MaterialTheme {
                // Surface provides a background color and elevation
                Surface {
                    // Pass the ViewModel to MainScreen for state management
                    MainScreen(viewModel)
                }
            }
        }
    }
}

/**
 * MainScreen is the root composable that sets up navigation between screens.
 * It uses NavHost to manage navigation between different screens in the app.
 *
 * @param viewModel The shared ViewModel instance that manages the app's state
 */
@Composable
fun MainScreen(viewModel: MainViewModel) {
    // Create a NavController to handle navigation between screens
    val navController = rememberNavController()

    // Set up the navigation graph with two destinations
    NavHost(navController = navController, startDestination = "screen1") {
        // Define the first screen in the navigation graph
        composable("screen1") {
            CounterScreen1(navController, viewModel)
        }
        // Define the second screen in the navigation graph
        composable("screen2") {
            CounterScreen2(navController, viewModel)
        }
    }
}

/**
 * CounterScreen1 is the first screen of the app.
 * It displays a counter and provides navigation to Screen 2.
 *
 * @param navController Handles navigation between screens
 * @param viewModel Provides shared state management for the counter
 */
@Composable
fun CounterScreen1(
    navController: NavController,
    viewModel: MainViewModel
) {
    // Column arranges its children vertically
    Column(
        modifier = Modifier
            .padding(16.dp)  // Add padding around the entire column
            .padding(top = 50.dp)  // Add extra padding at the top
    ) {
        // Display screen title
        Text("Screen 1")
        // Display the current count from the ViewModel
        Text("Count: ${viewModel.count}")
        // Button to increment the counter
        Button(onClick = { viewModel.increment() }) {
            Text("Increment")
        }
        // Button to navigate to Screen 2
        Button(onClick = { navController.navigate("screen2") }) {
            Text("Go to Screen 2")
        }
    }
}

/**
 * CounterScreen2 is the second screen of the app.
 * It displays the same counter as Screen 1 and provides navigation back to Screen 1.
 * Both screens share the same ViewModel, so they display the same counter value.
 *
 * @param navController Handles navigation between screens
 * @param viewModel Provides shared state management for the counter
 */
@Composable
fun CounterScreen2(
    navController: NavController,
    viewModel: MainViewModel
) {
    // Column arranges its children vertically
    Column(
        modifier = Modifier
            .padding(16.dp)  // Add padding around the entire column
            .padding(top = 50.dp)  // Add extra padding at the top
    ) {
        // Display screen title
        Text("Screen 2")
        // Display the current count from the ViewModel
        Text("Count: ${viewModel.count}")
        // Button to increment the counter
        Button(onClick = { viewModel.increment() }) {
            Text("Increment")
        }
        // Button to navigate back to Screen 1
        Button(onClick = { navController.navigate("screen1") }) {
            Text("Go to Screen 1")
        }
    }
}