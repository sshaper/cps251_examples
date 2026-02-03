package com.example.videoplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.videoplayer.ui.theme.VideoPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoPlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VideoPlayerApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun VideoPlayerApp(modifier: Modifier = Modifier) {
    var selectedVideo by remember { mutableStateOf<VideoItem?>(null) }
    val videos = remember { SampleVideoData.getSampleVideos() }

    if (selectedVideo == null) {
        VideoListScreen(
            videos = videos,
            onVideoClick = { video -> selectedVideo = video }
        )
    } else {
        VideoPlayerScreen(
            videoItem = selectedVideo!!,
            onBackClick = { selectedVideo = null }
        )
    }
}