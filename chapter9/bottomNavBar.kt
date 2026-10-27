package com.example.bookexamplesapp

// Core Android imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat

// Compose UI imports
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Chapter 9: bottom bar with three destination items.
// Run this file separately from the other examples: each defines MainActivity
// and its own NavigationRoutes/screens in the same package.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request a light status bar appearance: dark icons on a light background
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        // Set up the Compose UI with Material Design theme
        setContent {
            MaterialTheme {
                Surface {
                    MainScreen()
                }
            }
        }
    }
}

// Shared route names used by the graph, click actions, and selection comparisons.
// object provides a single holder; const val defines compile-time string constants.
// These comments describe roles within this standalone file, not extra required files.
object NavigationRoutes {
    const val HOME = "home"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
}

// The bar displays selection; NavigationBarItem does not navigate by itself.
// Each onClick below supplies the navigation action.
@Composable
fun BottomNavigationBar(navController: NavController) {
    // Observe the controller's current entry as Compose State. Reading .value
    // makes this composable respond when navigation changes that entry, including Back.
    // destination.route identifies which item should look selected. Safe calls handle
    // an absent entry/route while navigation is being initialized; no route then matches.
    // A local selected-index variable could drift away from the actual destination.
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        // The label is visible; the icon also has a description for accessibility.
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") },
            // Material draws the selected appearance from this Boolean.
            selected = currentRoute == NavigationRoutes.HOME,
            onClick = {
                navController.navigate(NavigationRoutes.HOME) {
                    // Prevent another Home entry only when Home is already on top.
                    // Switching Home -> Profile -> Home can still add a second Home.
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == NavigationRoutes.PROFILE,
            onClick = {
                navController.navigate(NavigationRoutes.PROFILE) {
                    launchSingleTop = true
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentRoute == NavigationRoutes.SETTINGS,
            onClick = {
                navController.navigate(NavigationRoutes.SETTINGS) {
                    launchSingleTop = true
                }
            }
        )
    }
}



// Keep the bar outside NavHost so it remains visible as destinations change.
@Composable
fun MainScreen() {
    // One remembered controller connects the persistent bar to the destination host.
    val navController = rememberNavController()

    Scaffold(
        // Scaffold places the bar in its bottomBar slot; it does not manage navigation.
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        // Scaffold supplies content padding; applying it is the content's job.
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.HOME,
            // Keep destination content clear of the bar and supplied insets.
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavigationRoutes.HOME) { HomeScreen() }
            composable(NavigationRoutes.PROFILE) { ProfileScreen() }
            composable(NavigationRoutes.SETTINGS) { SettingsScreen() }
        }
    }
}

// The destinations only show centered text; the persistent bar handles navigation.
@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Home Screen")
    }
}

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Profile Screen")
    }
}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Settings Screen")
    }
}

// Preview helps inspect the initial layout; run the app to check navigation and Back.
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MaterialTheme {
        MainScreen()
    }
}