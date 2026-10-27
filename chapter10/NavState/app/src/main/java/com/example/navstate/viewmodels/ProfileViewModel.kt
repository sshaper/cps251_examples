package com.example.navstate.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.navstate.data.ProfileData

/**
 * ProfileViewModel manages state specific to the Profile screen
 * This ViewModel handles profile-related data and operations
 * 
 * Key responsibilities:
 * - Load and manage profile data
 * - Handle profile-specific UI state
 * - Process profile-related user actions
 */
class ProfileViewModel : ViewModel() {
    
    /**
     * The profile data for the current user
     * This contains information specific to the profile being displayed
     */
    var profileData by mutableStateOf<ProfileData?>(null)
        private set  // Only this ViewModel can modify the profile data
    
    /**
     * Indicates whether profile data is currently being loaded
     */
    var isLoading by mutableStateOf(false)
        private set
    
    /**
     * Loads profile data for a specific user
     * In a real app, this would make a network call to fetch profile information
     * @param userId The ID of the user whose profile to load
     */
    fun loadProfile(userId: String) {
        isLoading = true
        
        // Construct sample data synchronously: there is no network request or delay.
        // isLoading changes true -> false in one call, so a spinner may never render.
        profileData = ProfileData(
            userId = userId,
            description = "Profile for $userId - This is a sample profile description that demonstrates how profile data is managed in the ViewModel."
        )
        
        isLoading = false
    }
    
    /**
     * Clears the current profile data
     * Available for an explicit reset; current navigation/logout callbacks do not call it.
     */
    fun clearProfile() {
        profileData = null
    }
}

