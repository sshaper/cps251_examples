package com.example.imagegallery

/**
 * SampleImageData is an object that provides sample image data for our gallery app.
 * 
 * An "object" in Kotlin is a singleton - there's only one instance of it in the entire app.
 * This is perfect for providing data that doesn't need to be created multiple times.
 * 
 * We use Picsum Photos (https://picsum.photos) which is a free service that provides
 * placeholder images. The format /id/X/width/height gives us a specific image by ID.
 * 
 * IMPORTANT: We use /id/ format instead of ?random= because:
 * - /id/1/ always returns the same image (consistent)
 * - ?random=1 returns a different random image each time (inconsistent)
 * - This ensures the thumbnail and full-size image match when you click on them
 */
object SampleImageData {

    /**
     * Returns a list of sample ImageItem objects for our gallery.
     * 
     * @return A List containing 6 ImageItem objects with sample data
     */
    fun getSampleImages(): List<ImageItem> {
        return listOf(
            // Image 1: Both thumbnail and full-size use the same ID (1) so they show the same image
            ImageItem(
                id = "1",                                                    // Unique ID for this image
                title = "Sample Image 1",                                    // Title shown to users
                imageUrl = "https://picsum.photos/id/1/800/600",           // Full-size: 800 pixels wide, 600 pixels tall
                thumbnailUrl = "https://picsum.photos/id/1/200/200"        // Thumbnail: 200x200 pixels (smaller, loads faster)
            ),
            // Image 2: Notice both URLs use /id/2/ - this ensures they're the same image
            ImageItem(
                id = "2",
                title = "Sample Image 2",
                imageUrl = "https://picsum.photos/id/2/800/600",           // Full-size image
                thumbnailUrl = "https://picsum.photos/id/2/200/200"        // Thumbnail version
            ),
            // Image 3
            ImageItem(
                id = "3",
                title = "Sample Image 3",
                imageUrl = "https://picsum.photos/id/3/800/600",
                thumbnailUrl = "https://picsum.photos/id/3/200/200"
            ),
            // Image 4
            ImageItem(
                id = "4",
                title = "Sample Image 4",
                imageUrl = "https://picsum.photos/id/4/800/600",
                thumbnailUrl = "https://picsum.photos/id/4/200/200"
            ),
            // Image 5
            ImageItem(
                id = "5",
                title = "Sample Image 5",
                imageUrl = "https://picsum.photos/id/5/800/600",
                thumbnailUrl = "https://picsum.photos/id/5/200/200"
            ),
            // Image 6
            ImageItem(
                id = "6",
                title = "Sample Image 6",
                imageUrl = "https://picsum.photos/id/6/800/600",
                thumbnailUrl = "https://picsum.photos/id/6/200/200"
            )
        )
    }
}