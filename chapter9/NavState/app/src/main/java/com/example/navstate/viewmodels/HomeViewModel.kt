package com.example.navstate.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

/**
 * HomeViewModel manages state specific to the Home screen
 * This ViewModel is only used by the Home screen and its related components
 * 
 * Key responsibilities:
 * - Manage Home screen-specific UI state
 * - Handle Home screen business logic
 * - Control loading states for Home screen operations
 */
class HomeViewModel : ViewModel() {
    
    /**
     * The title displayed on the Home screen
     * This can be updated dynamically based on user actions or app state
     */
    var screenTitle by mutableStateOf("Home")
        private set  // Only this ViewModel can modify the title
    
    /**
     * Indicates whether the Home screen is currently loading data
     * This is used to show loading indicators to the user
     */
    var isLoading by mutableStateOf(false)
        private set  // Only this ViewModel can modify the loading state
    
    /**
     * Updates the screen title
     * @param newTitle The new title to display
     */
    fun updateTitle(newTitle: String) {
        screenTitle = newTitle
    }
    
    /**
     * Simulates loading data for the Home screen
     * In a real app, this would make network calls or database queries
     */
    suspend fun loadHomeData() {
        isLoading = true
        // Simulate network delay
        delay(2000) // 2 second delay to simulate loading
        isLoading = false
    }
}

