
package com.example.bookexamplesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// -----------------------------------------
// Main Activity
// -----------------------------------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

// -----------------------------------------
// Navigation Routes
// -----------------------------------------
object NavigationRoutes {
    const val HOME = "home"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
}

// -----------------------------------------
// Navigation Helper
// Both bars use the same navigation logic.
// -----------------------------------------
fun NavController.navigateToScreen(route: String) {
    navigate(route) {
        popUpTo(NavigationRoutes.HOME) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

// -----------------------------------------
// Top Navigation Bar
// -----------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(navController: NavController) {

    val backStackEntry by
        navController.currentBackStackEntryAsState()

    val currentRoute =
        backStackEntry?.destination?.route

    TopAppBar(
        title = { Text("Top Navigation Bar") },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor =
                MaterialTheme.colorScheme.primaryContainer,
            titleContentColor =
                MaterialTheme.colorScheme.onPrimaryContainer
        ),

        actions = {

            // Home
            IconButton(
                onClick = {
                    navController.navigateToScreen(
                        NavigationRoutes.HOME
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home",
                    tint = if (
                        currentRoute == NavigationRoutes.HOME
                    ) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    }
                )
            }

            // Profile
            IconButton(
                onClick = {
                    navController.navigateToScreen(
                        NavigationRoutes.PROFILE
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = if (
                        currentRoute == NavigationRoutes.PROFILE
                    ) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    }
                )
            }

            // Settings
            IconButton(
                onClick = {
                    navController.navigateToScreen(
                        NavigationRoutes.SETTINGS
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = if (
                        currentRoute == NavigationRoutes.SETTINGS
                    ) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    }
                )
            }
        }
    )
}

// -----------------------------------------
// Bottom Navigation Bar
// -----------------------------------------
@Composable
fun BottomNavigationBar(navController: NavController) {

    val backStackEntry by
        navController.currentBackStackEntryAsState()

    val currentRoute =
        backStackEntry?.destination?.route

    NavigationBar {

        // Home
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            selected =
                currentRoute == NavigationRoutes.HOME,
            onClick = {
                navController.navigateToScreen(
                    NavigationRoutes.HOME
                )
            }
        )

        // Profile
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile") },
            selected =
                currentRoute == NavigationRoutes.PROFILE,
            onClick = {
                navController.navigateToScreen(
                    NavigationRoutes.PROFILE
                )
            }
        )

        // Settings
        NavigationBarItem(
            icon = {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Settings"
                )
            },
            label = { Text("Settings") },
            selected =
                currentRoute == NavigationRoutes.SETTINGS,
            onClick = {
                navController.navigateToScreen(
                    NavigationRoutes.SETTINGS
                )
            }
        )
    }
}

// -----------------------------------------
// Main Screen
// Both navigation bars are displayed.
// -----------------------------------------
@Composable
fun MainScreen() {

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = {
            TopNavigationBar(navController)
        },

        bottomBar = {
            BottomNavigationBar(navController)
        }

    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.HOME,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(NavigationRoutes.HOME) {
                HomeScreen()
            }

            composable(NavigationRoutes.PROFILE) {
                ProfileScreen()
            }

            composable(NavigationRoutes.SETTINGS) {
                SettingsScreen()
            }
        }
    }
}

// -----------------------------------------
// Home Screen
// -----------------------------------------
@Composable
fun HomeScreen() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home Screen",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Welcome to the Home Screen")
    }
}

// -----------------------------------------
// Profile Screen
// -----------------------------------------
@Composable
fun ProfileScreen() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile Screen",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Welcome to the Profile Screen")
    }
}

// -----------------------------------------
// Settings Screen
// -----------------------------------------
@Composable
fun SettingsScreen() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Settings Screen",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Welcome to the Settings Screen")
    }
}
