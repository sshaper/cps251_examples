package com.example.musicplayer

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.MediaItem

class AudioPlayerManager(private val context: Context) {
    private var exoPlayer: ExoPlayer? = null
    
    fun initializePlayer(): ExoPlayer {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
        return exoPlayer!!
    }
    
    // prepare loads the media; play requests playback once ready.
    fun playSong(audioUrl: String) {
        val player = initializePlayer()
        val mediaItem = MediaItem.fromUri(audioUrl)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }
    
    fun pause() {
        exoPlayer?.pause()
    }
    
    fun resume() {
        exoPlayer?.play()
    }
    
    fun stop() {
        exoPlayer?.stop()
    }
    
    fun release() {
        exoPlayer?.release()
        exoPlayer = null
    }
    
    fun getPlayer(): ExoPlayer? = exoPlayer
}