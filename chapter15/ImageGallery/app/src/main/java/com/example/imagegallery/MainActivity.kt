package com.example.imagegallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.imagegallery.ui.theme.ImageGalleryTheme

/**
 * MainActivity is the entry point of our Android app.
 * 
 * Every Android app needs an Activity, and MainActivity is where the app starts.
 * This class extends ComponentActivity, which is the base class for activities
 * that use Jetpack Compose for their UI.
 */
class MainActivity : ComponentActivity() {
    /**
     * onCreate is called when the activity is first created.
     * This is where we set up the UI for our app.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // enableEdgeToEdge() makes the app content extend behind system bars
        // (status bar and navigation bar) for a modern, immersive look
        enableEdgeToEdge()
        
        // setContent sets the UI content of this activity using Compose
        setContent {
            // ImageGalleryTheme applies our app's theme (colors, typography, etc.)
            ImageGalleryTheme {
                // Scaffold provides the basic structure with padding for system bars
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // ImageGalleryApp is our main composable that manages the app's screens
                    ImageGalleryApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * ImageGalleryApp is the main composable that manages which screen to show.
 * 
 * This composable uses state to track which image (if any) is currently selected.
 * Based on this state, it shows either:
 * - ImageGalleryScreen (the list of images) when no image is selected
 * - FullSizeImageScreen (the full-size image view) when an image is selected
 * 
 * This is a simple form of navigation using state management.
 * 
 * @param modifier Optional Modifier for customizing the app's appearance
 */
@Composable
fun ImageGalleryApp(modifier: Modifier = Modifier) {
    // selectedImage tracks which image is currently being viewed in full size
    // - null means no image is selected (show the gallery list)
    // - ImageItem means an image is selected (show the full-size view)
    // 
    // remember { mutableStateOf(...) } creates state that persists across recompositions
    // The "by" keyword is a Kotlin delegate that allows us to use selectedImage
    // directly instead of having to write selectedImage.value
    var selectedImage by remember { mutableStateOf<ImageItem?>(null) }
    
    // images holds the list of all images in our gallery
    // remember { } ensures we only call getSampleImages() once, not on every recomposition
    val images = remember { SampleImageData.getSampleImages() }

    // Conditional rendering: show different screens based on whether an image is selected
    if (selectedImage == null) {
        // No image selected - show the gallery list
        ImageGalleryScreen(
            images = images,  // Pass the list of images to display
            onImageClick = { image -> 
                // When an image is clicked, update selectedImage to that image
                // This will cause a recomposition and show FullSizeImageScreen
                selectedImage = image 
            }
        )
    } else {
        // An image is selected - show the full-size image view
        // The !! operator is a null assertion - we know it's not null because of the if check
        FullSizeImageScreen(
            imageItem = selectedImage!!,  // The selected image to display
            onBackClick = { 
                // When back is clicked, set selectedImage to null
                // This will cause a recomposition and show ImageGalleryScreen again
                selectedImage = null 
            }
        )
    }
}