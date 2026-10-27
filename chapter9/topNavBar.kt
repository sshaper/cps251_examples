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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Chapter 9: top app bar with navigation action icons.
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

// The top bar uses its actions slot for destination buttons in this teaching example.
// It does not automatically add an Up button or change its title with the destination.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(navController: NavController) {
    // Observe the controller's current entry as Compose State. Reading .value
    // makes this composable respond when navigation changes that entry, including Back.
    // destination.route identifies which item should look selected. Safe calls handle
    // an absent entry/route while navigation is being initialized; no route then matches.
    // A local selected-index variable could drift away from the actual destination.
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    TopAppBar(
        title = { Text("Top Navigation Bar") },
        // Color roles come from the theme; actual colors depend on that theme.
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        // This slot places our three action buttons on the trailing side of the bar.
        actions = {
            // Navigate with the same controller used by NavHost in MainScreen.
            // Single-top avoids another Home only if Home is already on top; it
            // does not remove older Home entries elsewhere in the history.
            IconButton(onClick = {
                navController.navigate(NavigationRoutes.HOME) { launchSingleTop = true }
            }) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Home",
                    // IconButton has no destination-selected flag here: derive tint
                    // from the observed route so it also follows system Back.
                    tint = if (currentRoute == NavigationRoutes.HOME) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            IconButton(onClick = {
                navController.navigate(NavigationRoutes.PROFILE) { launchSingleTop = true }
            }) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = if (currentRoute == NavigationRoutes.PROFILE) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            IconButton(onClick = {
                navController.navigate(NavigationRoutes.SETTINGS) { launchSingleTop = true }
            }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings",
                    tint = if (currentRoute == NavigationRoutes.SETTINGS) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    )
}

// Keep the bar outside NavHost so it remains visible as destinations change.
@Composable
fun MainScreen() {
    // One remembered controller connects the persistent bar to the destination host.
    val navController = rememberNavController()

    Scaffold(
        // Scaffold places the bar in its topBar slot; it does not manage navigation.
        topBar = { TopNavigationBar(navController) }
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