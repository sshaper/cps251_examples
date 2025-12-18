package com.example.imagegallery

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * ImageGalleryScreen displays a scrollable list of image cards.
 * 
 * This screen shows all the images in a vertical list. It uses LazyColumn
 * which is efficient because it only creates and displays the image cards
 * that are currently visible on screen. As you scroll, it creates new cards
 * and removes ones that are off-screen, making it very memory efficient.
 * 
 * @param images A list of ImageItem objects to display in the gallery
 * @param onImageClick A callback function that gets called when an image card is clicked.
 *                     It receives the ImageItem that was clicked as a parameter.
 */
@Composable
fun ImageGalleryScreen(
    images: List<ImageItem>,                    // List of all images to display
    onImageClick: (ImageItem) -> Unit           // Function called when an image is clicked
) {
    // LazyColumn creates a vertically scrollable list
    // It's "lazy" because it only creates items that are visible on screen
    // This makes it very efficient for long lists
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()          // Takes up the full screen
            .padding(top = 50.dp),  // Adds 50dp padding at the top (for status bar)
        verticalArrangement = Arrangement.spacedBy(8.dp), // Adds 8dp space between each card
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp) // Padding around the entire list
    ) {
        // items() is a LazyColumn function that creates one item for each element in the list
        // For each image in the images list, it creates an ImageCard
        items(images) { image ->
            // Create an ImageCard for this image
            ImageCard(
                imageItem = image,  // Pass the current image data
                onClick = { onImageClick(image) } // When clicked, call onImageClick with this image
            )
        }
    }
}

