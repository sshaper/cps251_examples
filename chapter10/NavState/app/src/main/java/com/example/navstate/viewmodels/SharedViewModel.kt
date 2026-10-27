package com.example.navstate.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.navstate.data.User

/**
 * SharedViewModel manages state that needs to be accessed by multiple screens
 * AppNavigation obtains this instance from its Activity owner and passes it explicitly
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
    // Readers observe Compose state. private set keeps writes behind updateUser/clearUser.
    // In-memory only: a new app process starts with null, not a remembered login.
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
    // Clear only the shared sample user. ProfileData and existing route arguments
    // have separate owners and are not rewritten by this operation.
    fun clearUser() {
        currentUser = null
    }
}

