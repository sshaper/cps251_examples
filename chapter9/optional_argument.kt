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
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToProfileBasic = { userId ->
                    navController.navigate("profile/$userId") {
                        launchSingleTop = true
                    }
                },
                onNavigateToProfileWithDetails = { userId ->
                    navController.navigate("profile/$userId?showDetails=true") {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(
            route = "profile/{userId}?showDetails={showDetails}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("showDetails") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            val showDetails = backStackEntry.arguments?.getBoolean("showDetails") ?: false
            ProfileScreen(
                userId = userId,
                showDetails = showDetails,
                onNavigateBack = {
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
