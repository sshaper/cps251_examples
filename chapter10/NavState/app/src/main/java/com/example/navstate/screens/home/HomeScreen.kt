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
    // This default resolves under the Home navigation entry, not the Activity.
    // Another Home entry can have another HomeViewModel.
    homeViewModel: HomeViewModel = viewModel(),
    // AppNavigation explicitly supplies the Activity-owned shared instance.
    // This default alone would NOT guarantee sharing with other destinations.
    sharedViewModel: SharedViewModel = viewModel(),
    // Navigation callbacks - functions to navigate to other screens
    onProfileClick: (String) -> Unit,
    onSettingsClick: () -> Unit
) {
    // This scope belongs to this composition and is cancelled when it leaves.
    // The next lesson contrasts this caller-owned work with viewModelScope.
    // The ViewModel holds the loading flag, but this screen owns the coroutine.
    // Retaining the ViewModel does not retain a composition-owned loading job.
    val coroutineScope = rememberCoroutineScope()
    
    // Runs when this effect enters composition, not on every recomposition.
    // It can run again on re-entry; Unit is not a permanent once-per-app flag.
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
                // This fixed argument does not read or update the shared logged-in user.
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

