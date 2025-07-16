package com.example.navstate.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.navstate.data.User

/**
 * SharedViewModel manages state that needs to be accessed by multiple screens
 * This ViewModel is shared across the entire navigation graph
 * 
 * Key responsibilities:
 * - Store user session information
 * - Manage app-wide state
 * - Handle data that persists across screen navigation
 */
class SharedViewModel : ViewModel() {
    
    /**
     * Current user logged into the application
     * This state is shared between Home, Profile, and Settings screens
     * Using mutableStateOf ensures the UI updates automatically when this changes
     */
    var currentUser by mutableStateOf<User?>(null)
        private set  // Only this ViewModel can modify the user
    
    /**
     * Updates the current user
     * This function provides a controlled way to change the user state
     * @param user The new user to set as current
     */
    fun updateUser(user: User) {
        currentUser = user
    }
    
    /**
     * Clears the current user (for logout functionality)
     */
    fun clearUser() {
        currentUser = null
    }
}

