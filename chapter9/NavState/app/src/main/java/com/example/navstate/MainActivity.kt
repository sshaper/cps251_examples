package com.example.navstate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.navstate.navigation.AppNavigation
import com.example.navstate.ui.theme.NavStateTheme

/**
 * MainActivity is the entry point of the NavState application
 * It sets up the Material Design theme and initializes the navigation system
 * 
 * Key responsibilities:
 * - Initialize the Compose UI
 * - Set up the Material Design theme
 * - Start the navigation system
 */
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set up the Compose UI for this activity
        setContent {
            // Apply the Material Design theme to the entire app
            NavStateTheme {
                // Surface provides a background color and elevation for the content
                // It's a basic building block that follows Material Design guidelines
                Surface(
                    modifier = Modifier.fillMaxSize(),  // Take up the full screen
                    color = MaterialTheme.colorScheme.background  // Use theme background color
                ) {
                    // AppNavigation is the root composable that manages all navigation
                    // This is where the navigation graph is set up and all screens are defined
                    AppNavigation()
                }
            }
        }
    }
}

