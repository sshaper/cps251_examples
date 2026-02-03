package com.example.videoplayer

// Sample video data using public test videos
object SampleVideoData {

    fun getSampleVideos(): List<VideoItem> {
        return listOf(
            VideoItem(
                id = "1",
                title = "Sample Video 1",
                description = "A sample video for testing video playback",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                thumbnailUrl = "https://picsum.photos/400/300?random=1"
            ),
            VideoItem(
                id = "2",
                title = "Sample Video 2",
                description = "Another sample video for testing",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                thumbnailUrl = "https://picsum.photos/400/300?random=2"
            ),
            VideoItem(
                id = "3",
                title = "Sample Video 3",
                description = "A third sample video",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                thumbnailUrl = "https://picsum.photos/400/300?random=3"
            )
        )
    }
}