package com.example.musicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.musicplayer.ui.theme.MusicPlayerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicPlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MusicPlayerApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MusicPlayerApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val audioPlayerManager = remember { AudioPlayerManager(context) }
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    val songs = remember { SampleSongData.getSampleSongs() }
    
    // Initialize player
    // One player belongs to this app composition; leaving only NowPlayingScreen does not release it.
    val player = remember { audioPlayerManager.initializePlayer() }
    
    // Clean up when done
    DisposableEffect(Unit) {
        onDispose {
            audioPlayerManager.release()
        }
    }
    
    if (selectedSong == null) {
        SongListScreen(
            songs = songs,
            onSongClick = { song ->
                selectedSong = song
                audioPlayerManager.playSong(song.audioUrl)
            }
        )
    } else {
        NowPlayingScreen(
            song = selectedSong!!,
            player = player,
            onPlayPauseClick = {
                if (player.isPlaying) {
                    audioPlayerManager.pause()
                } else {
                    audioPlayerManager.resume()
                }
            },
            onBackClick = {
                audioPlayerManager.stop()
                selectedSong = null
            }
        )
    }
}