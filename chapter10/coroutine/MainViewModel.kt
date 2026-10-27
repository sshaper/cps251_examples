package com.example.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * MainViewModel manages the data loading state and operations.
 * Its coroutine suspends during delay, leaving the main thread free for input.
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
     * Launches a ViewModel-owned coroutine on Main and simulates waiting.
     * Updates the UI state when the operation is complete.
     */
    fun loadData() {
        // launch starts a Job and returns without waiting for the two-second delay.
        // This default scope uses Main; it is cancelled when this ViewModel clears.
        // Rotation retains this Activity-owned ViewModel and its active job.
        // Each tap starts another job; overlapping requests are not coordinated here.
        viewModelScope.launch {
            try {
                isLoading = true
                // Suspend this coroutine without blocking Main; no network request runs.
                delay(2000) // Simulates a 2-second delay
                data = "Data loaded successfully!"
            } catch (e: CancellationException) {
                // Cancellation is a stop request, not a failed load. Preserve it.
                throw e
            } catch (e: Exception) {
                data = "Error loading data: ${e.message}"
            } finally {
                // Runs on completion, failure, or cancellation; no suspension here.
                isLoading = false
            }
        }
    }
}
