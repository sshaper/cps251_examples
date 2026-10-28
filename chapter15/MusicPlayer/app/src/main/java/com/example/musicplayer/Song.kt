package com.example.musicplayer

// Data class representing a song in our player
// Display metadata and media URLs travel together when an item is selected.
data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val audioUrl: String,
    val coverImageUrl: String
)