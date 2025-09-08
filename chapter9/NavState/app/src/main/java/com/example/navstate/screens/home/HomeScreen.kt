package com.example.navstate.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.navstate.viewmodels.HomeViewModel
import com.example.navstate.viewmodels.SharedViewModel
import kotlinx.coroutines.launch

/**
 * HomeScreen is the main screen of the application
 * It demonstrates how to use both screen-specific and shared ViewModels
 * 
 * Key features:
 * - Uses HomeViewModel for screen-specific state
 * - Uses SharedViewModel for app-wide state
 * - Provides navigation to other screens
 * - Shows loading states and user information
 */
@Composable
fun HomeScreen(
    // Screen-specific ViewModel - manages state only for this screen
    homeViewModel: HomeViewModel = viewModel(),
    // Shared ViewModel - manages state that can be accessed by other screens
    sharedViewModel: SharedViewModel = viewModel(),
    // Navigation callbacks - functions to navigate to other screens
    onProfileClick: (String) -> Unit,
    onSettingsClick: () -> Unit
) {
    // Get coroutine scope for handling suspend functions
    val coroutineScope = rememberCoroutineScope()
    
    // Load home data when the screen is first displayed
    LaunchedEffect(Unit) {
        homeViewModel.loadHomeData()
    }
    
    // Main layout container
    Column(
        modifier = Modifier
            .fillMaxSize()           // Take up the full screen
            .padding(16.dp),         // Add padding around the content
        verticalArrangement = Arrangement.Center,      // Center content vertically
        horizontalAlignment = Alignment.CenterHorizontally  // Center content horizontally
    ) {
        
        // Display the screen title from the HomeViewModel
        Text(
            text = homeViewModel.screenTitle,
            style = MaterialTheme.typography.headlineMedium
        )
        
        // Show loading indicator if data is being loaded
        if (homeViewModel.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
        
        // Display user information from the SharedViewModel
        sharedViewModel.currentUser?.let { user ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome ${user.name}!",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Email: ${user.email}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        // Navigation buttons
        Spacer(modifier = Modifier.height(32.dp))
        
        // Button to navigate to Profile screen
        Button(
            onClick = { 
                // Pass a sample user name when navigating to profile
                //onProfileClick("SampleUser")
                onProfileClick("Scott Shaper")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Profile")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Button to navigate to Settings screen
        Button(
            onClick = onSettingsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go to Settings")
        }
        
        // Demo section to show ViewModel functionality
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Home Screen Features:",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Button to demonstrate updating the screen title
        OutlinedButton(
            onClick = { 
                homeViewModel.updateTitle("Updated Home") 
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Title")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Button to demonstrate loading state
        OutlinedButton(
            onClick = { 
                coroutineScope.launch {
                    homeViewModel.loadHomeData() 
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simulate Loading")
        }
    }
}

