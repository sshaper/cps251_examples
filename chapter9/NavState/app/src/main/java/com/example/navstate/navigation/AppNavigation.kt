package com.example.navstate.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.navstate.screens.home.HomeScreen
import com.example.navstate.screens.profile.ProfileScreen
import com.example.navstate.screens.settings.SettingsScreen
import com.example.navstate.viewmodels.SharedViewModel

/**
 * AppNavigation sets up the navigation graph for the entire application
 * This composable manages navigation between different screens
 * 
 * Key features:
 * - Defines all possible navigation paths
 * - Manages shared ViewModel across screens
 * - Handles navigation arguments and parameters
 * - Provides navigation callbacks to screens
 */
@Composable
fun AppNavigation() {
    // Create the navigation controller that manages navigation state
    val navController = rememberNavController()
    
    // Create a shared ViewModel that will be available to all screens
    // This ViewModel persists across navigation and survives configuration changes
    val sharedViewModel: SharedViewModel = viewModel()
    
    // Define the navigation graph with all possible destinations
    NavHost(
        navController = navController,           // The navigation controller
        startDestination = NavRoutes.HOME       // The screen to show when the app starts
    ) {
        
        // Define the Home screen destination
        composable(NavRoutes.HOME) {
            HomeScreen(
                // Pass the shared ViewModel so Home screen can access shared state
                sharedViewModel = sharedViewModel,
                // Navigation callbacks that Home screen can use to navigate to other screens
                onProfileClick = { userName ->
                    // Navigate to profile screen with the provided user name
                    navController.navigate("profile/$userName")
                },
                onSettingsClick = {
                    // Navigate to settings screen
                    navController.navigate(NavRoutes.SETTINGS)
                }
            )
        }
        
        // Define the Profile screen destination with a dynamic argument
        composable(
            route = NavRoutes.PROFILE,  // "profile/{userId}"
            arguments = listOf(
                // Define the userId argument that will be extracted from the route
                navArgument("userId") { 
                    type = NavType.StringType  // Specify that userId is a string
                }
            )
        ) { backStackEntry ->
            // Extract the userId from the navigation arguments
            // If userId is not provided, default to "Unknown"
            val userId = backStackEntry.arguments?.getString("userId") ?: "Unknown"
            
            ProfileScreen(
                // Pass the user name from the HomeScreen
                userName = userId,
                // Pass the shared ViewModel
                sharedViewModel = sharedViewModel,
                // Navigation callbacks for Profile screen
                onHomeClick = { 
                    navController.navigate(NavRoutes.HOME) 
                },
                onSettingsClick = { 
                    navController.navigate(NavRoutes.SETTINGS) 
                }
            )
        }
        
        // Define the Settings screen destination
        composable(NavRoutes.SETTINGS) {
            SettingsScreen(
                // Pass the shared ViewModel
                sharedViewModel = sharedViewModel,
                // Navigation callbacks for Settings screen
                onHomeClick = { 
                    navController.navigate(NavRoutes.HOME) 
                },
                onProfileClick = { userName -> 
                    navController.navigate("profile/$userName") 
                }
            )
        }
    }
}

