package com.example.imagegallery

/**
 * ImageItem is a data class that represents a single image in our gallery.
 * 
 * A data class in Kotlin automatically provides:
 * - equals() and hashCode() methods for comparing objects
 * - toString() method for printing
 * - copy() method for creating modified copies
 * 
 * This makes it perfect for representing simple data structures like our image items.
 * 
 * @param id A unique identifier for each image (like "1", "2", "3")
 * @param title The display name or description of the image (shown to users)
 * @param imageUrl The URL for the full-size version of the image (used when viewing the image in detail)
 * @param thumbnailUrl The URL for the smaller thumbnail version (used in the list view for faster loading)
 */
data class ImageItem(
    val id: String,              // Unique identifier: "1", "2", "3", etc.
    val title: String,           // Display name: "Sample Image 1", "Sample Image 2", etc.
    val imageUrl: String,        // Full-size image URL: "https://picsum.photos/id/1/800/600"
    val thumbnailUrl: String     // Thumbnail URL: "https://picsum.photos/id/1/200/200"
)
