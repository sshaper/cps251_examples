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
    
    // Obtain one instance from the current owner. MainActivity calls this function
    // above NavHost, so that owner is the Activity in this app. It survives rotation
    // and destination changes, but is not persistent storage or a real login session.
    // Calling viewModel() separately inside each destination would use different owners.
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
                    // The callback carries a value; the screen does not own the controller.
                    // The raw route text matches profile/{userId}. Arbitrary input with
                    // route delimiters would need Uri.encode(userName) before insertion.
                    // This sample passes a display name, despite the userId argument key.
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
            // The route requires this argument. The fallback handles a null Bundle/read;
            // it does not make the path argument optional.
            val userId = backStackEntry.arguments?.getString("userId") ?: "Unknown"
            
            ProfileScreen(
                // The value may come from Home or Settings. It is a snapshot for this
                // navigation entry, independent of the live shared currentUser.
                userName = userId,
                // Pass the SAME Activity-owned instance, overriding the screen default.
                sharedViewModel = sharedViewModel,
                // Navigation callbacks for Profile screen
                onHomeClick = { 
                    // This is forward navigation, not popBackStack: it can add a new
                    // Home entry and a new destination-owned HomeViewModel.
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
                // Pass the SAME Activity-owned instance, overriding the screen default.
                sharedViewModel = sharedViewModel,
                // Navigation callbacks for Settings screen
                onHomeClick = { 
                    // This is forward navigation, not popBackStack: it can add a new
                    // Home entry and a new destination-owned HomeViewModel.
                    navController.navigate(NavRoutes.HOME) 
                },
                onProfileClick = { userName -> 
                    navController.navigate("profile/$userName") 
                }
            )
        }
    }
}

