package com.example.videoplayer

// Data class representing a video in our player
// The thumbnail can be an independent image rather than a frame extracted from video.
data class VideoItem(
    val id: String,
    val title: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String
)
