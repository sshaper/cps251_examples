package com.example.imagegallery

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * FullSizeImageScreen displays a full-size version of an image with a top app bar.
 * 
 * This screen is shown when a user clicks on an image card in the gallery.
 * It displays:
 * - A top app bar with the image title and a back button
 * - The full-size image centered on the screen
 * 
 * @OptIn(ExperimentalMaterial3Api::class) is required because TopAppBar
 * is an experimental Material3 API. This annotation tells the compiler
 * that we're aware it might change in the future.
 * 
 * @param imageItem The ImageItem containing the image to display in full size
 * @param onBackClick A function to call when the back button is clicked
 * @param modifier Optional Modifier for customizing the screen's appearance
 */
@OptIn(ExperimentalMaterial3Api::class) // Required for TopAppBar (experimental API)
@Composable
fun FullSizeImageScreen(
    imageItem: ImageItem,        // The image data to display
    onBackClick: () -> Unit,      // Function called when back button is clicked
    modifier: Modifier = Modifier  // Optional modifier for styling
) {
    // Scaffold provides the basic structure of a screen with a top app bar
    Scaffold(
        topBar = {
            // TopAppBar creates a bar at the top of the screen
            TopAppBar(
                title = { Text(imageItem.title) }, // Shows the image title in the center
                navigationIcon = {
                    // IconButton creates a clickable icon button
                    IconButton(onClick = onBackClick) {
                        // Icon displays the back arrow
                        // Icons.AutoMirrored.Filled.ArrowBack automatically flips for right-to-left languages
                        // This is the recommended version (the old Icons.Default.ArrowBack is deprecated)
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // The back arrow icon
                            contentDescription = "Back" // Text for screen readers
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Box is a container that can center its content
        Box(
            modifier = modifier
                .fillMaxSize()                    // Takes up the full screen
                .padding(paddingValues),          // Adds padding from the Scaffold (for the top bar)
            contentAlignment = Alignment.Center   // Centers the image in the box
        ) {
            // AsyncImage loads and displays the full-size image
            // This uses imageItem.imageUrl (the full-size 800x600 image)
            // instead of imageItem.thumbnailUrl (the smaller 200x200 thumbnail)
            AsyncImage(
                model = imageItem.imageUrl,       // URL of the full-size image (800x600)
                contentDescription = imageItem.title, // Text for screen readers
                modifier = Modifier.fillMaxSize(), // Image takes up the full available space
                contentScale = ContentScale.Fit    // Scales the image to fit while maintaining aspect ratio
                                                   // This ensures the entire image is visible (unlike Crop)
            )
        }
    }
}