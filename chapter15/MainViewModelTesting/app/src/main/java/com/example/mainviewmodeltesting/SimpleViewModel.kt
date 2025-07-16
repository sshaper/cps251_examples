package com.example.mainviewmodeltesting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Simple ViewModel that manages user data.
 * This demonstrates basic state management with Compose.
 */
class SimpleViewModel : ViewModel() {
    // Observable state variables - these will trigger UI updates when changed
    var user: User? by mutableStateOf(null)
        private set

    var isLoading: Boolean by mutableStateOf(false)
        private set

    var error: String? by mutableStateOf(null)
        private set

    /**
     * Loads user data. In a real app, this would come from a database or network.
     */
    fun loadUser(userId: String) {
        isLoading = true
        error = null

        // Synchronous version for simplicity
        when (userId) {
            "1" -> {
                user = User("1", "John Doe", "john@example.com")
            }
            "2" -> {
                user = User("2", "Jane Smith", "jane@example.com")
            }
            else -> {
                error = "User not found"
            }
        }
        isLoading = false
    }

    /**
     * Updates the user's name.
     */
    fun updateUserName(newName: String) {
        val currentUser = user ?: return
        user = currentUser.copy(name = newName)
    }

    /**
     * Clears any error messages.
     */
    fun clearError() {
        error = null
    }

    /**
     * Clears the current user and returns to the initial state.
     */
    fun clearUser() {
        user = null
        error = null
    }
}