package com.example.accessibilityperformance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.accessibilityperformance.ui.theme.AccessibilityPerformanceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AccessibilityPerformanceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AccessibilityPerformanceApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AccessibilityPerformanceApp(modifier: Modifier = Modifier) {
    var items by remember { mutableStateOf(SampleData.generateSampleItems()) }
    var searchQuery by remember { mutableStateOf("") }
    
    AccessibleAndPerformantListScreen(
        items = items,
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        onItemClick = { item ->
            // Handle item click - in a real app, you might navigate to a detail screen
            println("Clicked on: ${item.title}")
        },
        onItemSelectionChange = { item, isSelected ->
            // Handle selection change
            items = items.map { 
                if (it.id == item.id) it.copy(isSelected = isSelected) else it 
            }
        }
    )
}