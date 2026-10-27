package com.example.bookexamplesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Chapter 9: one profile destination with a required ID and an optional display flag.
// Run this standalone example in a project with Navigation Compose configured.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface {
                    AppNavWithOptionalArgs()
                }
            }
        }
    }
}

@Composable
fun AppNavWithOptionalArgs() {
    // Remember navigation across recompositions; both Home buttons use this controller.
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                // Omit the query to use the declared default, showDetails = false.
                onNavigateToProfileBasic = { userId ->
                    navController.navigate("profile/$userId") {
                        // Only avoid another entry when this destination is already on top.
                        launchSingleTop = true
                    }
                },
                // Same destination, but explicitly request the detailed view.
                onNavigateToProfileWithDetails = { userId ->
                    navController.navigate("profile/$userId?showDetails=true") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            // The path argument is required. The query argument can be omitted
            // because its declaration below supplies a default value.
            route = "profile/{userId}?showDetails={showDetails}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("showDetails") {
                    type = NavType.BoolType
                    // profile/alex uses false; profile/alex?showDetails=true overrides it.
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            // Read the parsed values for this navigation entry, not live shared state.
            val userId = backStackEntry.arguments?.getString("userId")
            // The navigation default handles an omitted query. The Elvis expression
            // separately handles a null result from the safe call to arguments.
            val showDetails = backStackEntry.arguments?.getBoolean("showDetails") ?: false
            ProfileScreen(
                userId = userId,
                showDetails = showDetails,
                onNavigateBack = {
                    // Return to the existing Home entry rather than adding another Home.
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun HomeScreen(
    onNavigateToProfileBasic: (String) -> Unit,
    onNavigateToProfileWithDetails: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Optional argument example",
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Choose how to view the profile:",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Both buttons use a fixed simple ID. Arbitrary IDs containing route
        // delimiters would need encoding before insertion into the route.
        Button(onClick = { onNavigateToProfileBasic("alex") }) {
            Text("View profile (basic)")
        }
        Button(onClick = { onNavigateToProfileWithDetails("alex") }) {
            Text("View profile (with details)")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "This is the home page.",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun ProfileScreen(
    userId: String?,
    showDetails: Boolean,
    onNavigateBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Profile for user: $userId",
            style = MaterialTheme.typography.titleMedium
        )
        // The argument selects content in one screen; it does not select a new route.
        // These details are demonstration text, not data fetched for an account.
        if (showDetails) {
            Text(
                text = "Extra details (showDetails = true):",
                style = MaterialTheme.typography.labelMedium
            )
            Text(text = "Email: ${userId}@example.com")
            Text(text = "Member since: Jan 2024")
            Text(text = "Preferences: Notifications enabled")
        } else {
            Text(
                text = "Basic view only. Use \"View profile (with details)\" from home to see more.",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateBack) {
            Text("Back to Home")
        }
    }
}
