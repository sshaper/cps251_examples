package com.example.bookexamplesapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * MainViewModel manages the data loading state and operations.
 * It uses coroutines to perform background work without blocking the UI.
 */
class MainViewModel : ViewModel() {
    // State for the loaded data
    var data by mutableStateOf<String?>(null)
        private set

    // State to track loading status
    var isLoading by mutableStateOf(false)
        private set

    // State for the counter
    var counter by mutableStateOf(0)
        private set

    /**
     * Increments the counter value.
     * This demonstrates that UI operations can continue while coroutines are running.
     */
    fun incrementCounter() {
        counter++
    }

    /**
     * Loads data in the background using a coroutine.
     * Updates the UI state when the operation is complete.
     */
    fun loadData() {
        viewModelScope.launch {
            try {
                isLoading = true
                // Simulate background work (e.g., network request or database operation)
                delay(5000) // Simulates a 2-second delay
                data = "Data loaded successfully!"
            } catch (e: Exception) {
                data = "Error loading data: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}