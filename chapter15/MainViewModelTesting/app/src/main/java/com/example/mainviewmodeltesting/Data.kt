package com.example.mainviewmodeltesting

/**
 * Simple data class representing a user.
 * This is the model that our ViewModel will work with.
 */
data class User(
    val id: String,
    val name: String,
    val email: String
)