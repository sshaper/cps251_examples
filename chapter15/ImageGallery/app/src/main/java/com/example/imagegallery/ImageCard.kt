package com.example.imagegallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

/**
 * ImageCard is a composable that displays a single image in a card format.
 * 
 * This composable creates a card (like a photo card) that shows:
 * - A thumbnail image at the top
 * - The image title below the image
 * 
 * The entire card is clickable, so when a user taps anywhere on it,
 * the onClick callback is triggered.
 * 
 * @param imageItem The ImageItem data object containing the image information
 * @param onClick A lambda function that gets called when the card is clicked
 * @param modifier Optional Modifier for customizing the card's appearance
 */
@Composable
fun ImageCard(
    imageItem: ImageItem,        // The image data to display
    onClick: () -> Unit,          // Function to call when card is clicked
    modifier: Modifier = Modifier // Optional modifier for styling
) {
    // Card creates a Material Design card with rounded corners
    Card(
        modifier = modifier
            .fillMaxWidth()           // Card takes up the full width of its container
            .padding(8.dp)             // Adds 8dp of space around the card
            .clickable(onClick = onClick), // Makes the entire card clickable
        shape = RoundedCornerShape(8.dp)  // Rounds the corners by 8dp for a modern look
    ) {
        // Column arranges the image and title vertically (one on top of the other)
        Column {
            // AsyncImage loads and displays the image from the internet
            // This is the simplest way to load images - Coil handles everything automatically:
            // - Downloads the image in the background
            // - Shows a loading indicator while downloading
            // - Caches the image for faster future loads
            // - Handles errors gracefully
            AsyncImage(
                model = imageItem.thumbnailUrl,  // URL of the thumbnail (smaller image for the list)
                contentDescription = imageItem.title, // Text for screen readers (accessibility)
                modifier = Modifier
                    .fillMaxWidth()    // Image takes up full width of the card
                    .height(200.dp),   // Image is exactly 200dp tall
                contentScale = ContentScale.Crop // Crops the image to fill the 200dp height
                                                 // This maintains aspect ratio while filling the space
            )

            // Text displays the image title below the image
            Text(
                text = imageItem.title,                              // The text to display
                style = MaterialTheme.typography.titleMedium,         // Uses Material Design's medium title style
                modifier = Modifier.padding(16.dp)                   // Adds 16dp of padding around the text
            )
        }
    }
}

