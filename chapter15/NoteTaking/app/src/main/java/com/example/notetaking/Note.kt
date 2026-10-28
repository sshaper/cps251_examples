package com.example.notetaking

import java.util.Date

// Data class representing a note
// Timestamps are persisted data; recomposition does not create a new creation time.
data class Note(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)