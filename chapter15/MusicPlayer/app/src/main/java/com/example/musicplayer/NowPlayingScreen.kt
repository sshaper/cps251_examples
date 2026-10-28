package com.example.musicplayer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.media3.common.Player

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
    song: Song,
    player: Player?,
    onPlayPauseClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember(player) { mutableStateOf(player?.isPlaying ?: false) }
    var currentPosition by remember(player) { mutableLongStateOf(0L) }
    var duration by remember(player) { mutableLongStateOf(0L) }
    // Player properties are not Compose state; this listener makes playing status observable.
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(value: Boolean) { isPlaying = value }
        }
        player?.addListener(listener)
        onDispose { player?.removeListener(listener) }
    }
    LaunchedEffect(player) {
        while (player != null) {
            currentPosition = player.currentPosition.coerceAtLeast(0L)
            duration = player.duration.coerceAtLeast(0L)
            kotlinx.coroutines.delay(100)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Now Playing") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Album cover
            AsyncImage(
                model = song.coverImageUrl,
                contentDescription = "${song.title} by ${song.artist}",
                modifier = Modifier
                    .size(300.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Song info
            Text(
                text = song.title,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = song.artist,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Progress bar
            if (duration > 0) {
                LinearProgressIndicator(
                    progress = { (currentPosition.toFloat() / duration.toFloat()).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(currentPosition),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = formatTime(duration),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Playback controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(enabled = false, onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                FloatingActionButton(
                    onClick = onPlayPauseClick,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(32.dp)
                    )
                }
                
                IconButton(enabled = false, onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

fun formatTime(milliseconds: Long): String {
    val seconds = (milliseconds / 1000) % 60
    val minutes = milliseconds / (1000 * 60)
    return String.format("%02d:%02d", minutes, seconds)
}