package com.example.navigationtest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Home Screen", modifier = Modifier.testTag("home_screen_title"))
        
        Button(
            onClick = onNavigateToProfile,
            modifier = Modifier.testTag("profile_button")
        ) {
            Text("Go to Profile")
        }
        
        Button(
            onClick = { onNavigateToDetail("123") },
            modifier = Modifier.testTag("detail_button")
        ) {
            Text("Go to Detail")
        }
    }
} 