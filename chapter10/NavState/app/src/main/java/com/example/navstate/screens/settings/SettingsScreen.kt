package com.example.navstate.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.navstate.data.User
import com.example.navstate.viewmodels.SharedViewModel

/**
 * SettingsScreen displays app settings and user management options
 * It demonstrates how to use shared ViewModels and user interactions
 * 
 * Key features:
 * - Uses SharedViewModel for app-wide state
 * - Provides user management functionality
 * - Shows how to update shared state
 * - Demonstrates navigation between screens
 */
@Composable
fun SettingsScreen(
    // Required parameter: use the instance AppNavigation passes; do not create
    // a destination-owned replacement here, which would stop the sharing.
    sharedViewModel: SharedViewModel,
    // Navigation callbacks
    onHomeClick: () -> Unit,
    onProfileClick: (String) -> Unit
) {
    // Main layout container
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        // Screen title
        Text(
            text = "Settings Screen",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Display current user information
        sharedViewModel.currentUser?.let { user ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Current User:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Name: ${user.name}")
                    Text("Email: ${user.email}")
                }
            }
        } ?: run {
            // Show message when no user is logged in
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "No User Logged In",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        
        // User management buttons
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "User Management:",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Button to simulate user login
        Button(
            onClick = {
                // Local demonstration only: no authentication request or disk save.
                // Other screens observing this same instance can display the change.
                val sampleUser = User(
                    id = "1",
                    name = "John Doe",
                    email = "john.doe@example.com"
                )
                sharedViewModel.updateUser(sampleUser)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login Sample User")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Button to clear user (logout)
        OutlinedButton(
            onClick = { 
                sharedViewModel.clearUser() 
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
        
        // Navigation buttons
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Navigation:",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onHomeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Home")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            //onClick = { onProfileClick("SampleUser") },
            // Copy the current name into a navigation argument at click time.
            // Later shared-user changes do not rewrite that existing route value.
            onClick = { onProfileClick(sharedViewModel.currentUser?.name ?: "Unknown") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Profile")
        }
    }
}

