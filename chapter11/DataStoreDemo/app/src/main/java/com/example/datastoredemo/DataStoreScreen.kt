package com.example.datastoredemo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataStoreScreen(
    viewModel: DataStoreViewModel,
    modifier: Modifier = Modifier.padding(top=50.dp)
) {
    val userName by viewModel.userName.collectAsState()
    val darkMode by viewModel.darkMode.collectAsState()
    val fontSize by viewModel.fontSize.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    
    var newUserName by remember { mutableStateOf("") }
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "DataStore Demo",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Simple preferences storage with DataStore",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
        
        item {
            // Current Settings Display
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Current Settings (from DataStore):",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "• User Name: $userName",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = fontSize.sp
                    )
                    
                    Text(
                        text = "• Dark Mode: ${if (darkMode) "Enabled" else "Disabled"}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = fontSize.sp
                    )
                    
                    Text(
                        text = "• Font Size: ${fontSize}sp",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = fontSize.sp
                    )
                    
                    Text(
                        text = "• Notifications: ${if (notifications) "On" else "Off"}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = fontSize.sp
                    )
                }
            }
        }
        
        item {
            // Settings Controls
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Change Settings:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // User Name with Text Field
                    Column {
                        Text(
                            text = "User Name:",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = newUserName,
                                onValueChange = { newUserName = it },
                                label = { Text("Enter name") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )
                            Button(
                                onClick = { 
                                    if (newUserName.isNotBlank()) {
                                        viewModel.updateUserName(newUserName)
                                        newUserName = ""
                                    }
                                },
                                enabled = newUserName.isNotBlank()
                            ) {
                                Text("Change")
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Dark Mode
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Dark Mode:",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = darkMode,
                            onCheckedChange = { viewModel.updateDarkMode(it) }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Font Size with Simple Vertical Buttons
                    Column {
                        Text(
                            text = "Font Size:",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf(12, 14, 16, 18, 20).forEach { size ->
                                Button(
                                    onClick = { viewModel.updateFontSize(size) },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (fontSize == size)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.secondary
                                    )
                                ) {
                                    Text("${size}sp")
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Notifications
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Notifications:",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = notifications,
                            onCheckedChange = { viewModel.updateNotifications(it) }
                        )
                    }
                }
            }
        }
        
        item {
            // Clear All Settings
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
                        text = "Reset Settings:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Button(
                        onClick = { viewModel.clearAllPreferences() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Clear All Settings (Reset to Defaults)")
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = "This will reset: User Name to 'Guest', Dark Mode to Off, Font Size to 16sp, Notifications to On",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        item {
            // Instructions
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "How to Test DataStore:",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "1. Change any setting above",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "2. Close the app completely",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "3. Reopen the app",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "4. Your settings will still be there!",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "This demonstrates DataStore persistence - your preferences are saved and restored automatically.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
} 