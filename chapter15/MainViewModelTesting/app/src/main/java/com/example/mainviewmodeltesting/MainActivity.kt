package com.example.mainviewmodeltesting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.mainviewmodeltesting.ui.theme.MainViewModelTestingTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainViewModelTestingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SimpleUserApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}





/**
 * Main app composable that creates and uses the ViewModel.
 */
@Composable
fun SimpleUserApp(modifier: Modifier = Modifier) {
    // Create the ViewModel
    val viewModel = remember { SimpleViewModel() }
    
    SimpleUserScreen(
        viewModel = viewModel,
        modifier = modifier
    )
}

/**
 * UI screen that displays user information and provides buttons to interact with the ViewModel.
 */
@Composable
fun SimpleUserScreen(
    viewModel: SimpleViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Simple User Profile",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Loading indicator
        if (viewModel.isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Loading...")
        }
        
        // Error message
        viewModel.error?.let { errorMessage ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { viewModel.clearError() }) {
                        Text("×", style = MaterialTheme.typography.headlineSmall)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // User information
        viewModel.user?.let { currentUser ->
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("User ID: ${currentUser.id}")
                    Text("Name: ${currentUser.name}")
                    Text("Email: ${currentUser.email}")
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { viewModel.updateUserName("Updated Name") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Update Name")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = { viewModel.clearUser() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text("Clear User")
                    }
                }
            }
        } ?: run {
            // No user loaded - show buttons to load users
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No user loaded")
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { viewModel.loadUser("1") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Load User 1")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = { viewModel.loadUser("2") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Load User 2")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = { viewModel.loadUser("999") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Load Non-existent User")
                    }
                }
            }
        }
    }
}