package com.example.navstate.data

/**
 * Data class representing a user in the application
 * This is used to store user information that can be shared across screens
 */
data class User(
    val id: String,        // Unique identifier for the user
    val name: String,      // Display name of the user
    val email: String      // Email address of the user
)

/**
 * Data class representing profile data for a specific user
 * This contains information specific to a user's profile screen
 */
data class ProfileData(
    val userId: String,        // ID of the user this profile belongs to
    val description: String    // Description or bio for the user
)

