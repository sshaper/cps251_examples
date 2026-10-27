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
    // Route value supplied by AppNavigation from either Home or Settings.
    userName: String,
    // Default owner is this Profile destination entry.
    profileViewModel: ProfileViewModel = viewModel(),
    // AppNavigation supplies the same Activity-owned instance used by other screens.
    // The default viewModel() call would otherwise use this destination owner.
    sharedViewModel: SharedViewModel = viewModel(),
    // Navigation callbacks
    onHomeClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    // Load this entry's route value when the effect enters composition.
    // Unit does not restart solely because userName changes in an existing
    // composition. Re-entering the composition can run this initializer again.
    // In this app a new route normally creates a new destination entry.
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
                // Replace only this entry's ProfileData. The route argument and
                // SharedViewModel.currentUser stay unchanged.
                profileViewModel.loadProfile("ReloadedUser") 
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reload Profile Data")
        }
    }
}

