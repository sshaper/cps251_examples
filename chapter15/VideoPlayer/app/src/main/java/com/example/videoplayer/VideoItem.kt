package com.example.videoplayer

// Data class representing a video in our player
data class VideoItem(
    val id: String,
    val title: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String
)
