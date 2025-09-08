package com.example.navstate.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.navstate.viewmodels.ProfileViewModel
import com.example.navstate.viewmodels.SharedViewModel

/**
 * ProfileScreen displays user profile information
 * It demonstrates how to use both screen-specific and shared ViewModels
 * 
 * Key features:
 * - Uses ProfileViewModel for profile-specific state
 * - Uses SharedViewModel for app-wide state
 * - Loads profile data automatically
 * - Shows loading states and profile information
 */
@Composable
fun ProfileScreen(
    // User name passed from HomeScreen
    userName: String,
    // Screen-specific ViewModel for profile data
    profileViewModel: ProfileViewModel = viewModel(),
    // Shared ViewModel for app-wide state
    sharedViewModel: SharedViewModel = viewModel(),
    // Navigation callbacks
    onHomeClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    // Load profile data when the screen is first displayed
    // In a real app, you would get the userId from navigation arguments
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile(userName)
    }
    
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
            text = "Profile Screen",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Show loading indicator if profile data is being loaded
        if (profileViewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            // Display profile data when available
            profileViewModel.profileData?.let { profile ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "User ID: ${profile.userId}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = profile.description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        
        // Display shared user information if available
        sharedViewModel.currentUser?.let { user ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Shared User Info:",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text("Name: ${user.name}")
                    Text("Email: ${user.email}")
                }
            }
        }
        
        // Navigation buttons
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onHomeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Home")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = onSettingsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Settings")
        }
        
        // Demo section
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Profile Screen Features:",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Button to reload profile data
        OutlinedButton(
            onClick = { 
                profileViewModel.loadProfile("ReloadedUser") 
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reload Profile Data")
        }
    }
}

