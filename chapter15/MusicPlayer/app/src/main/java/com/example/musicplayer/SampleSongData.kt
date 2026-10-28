package com.example.musicplayer

// Sample song data using public test audio files
object SampleSongData {
    
    // Sample streams and artwork are remote dependencies, not bundled audio.
    fun getSampleSongs(): List<Song> {
        return listOf(
            Song(
                id = "1",
                title = "Sample Song 1",
                artist = "Test Artist",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                coverImageUrl = "https://picsum.photos/400/400?random=1"
            ),
            Song(
                id = "2",
                title = "Sample Song 2",
                artist = "Test Artist",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
                coverImageUrl = "https://picsum.photos/400/400?random=2"
            ),
            Song(
                id = "3",
                title = "Sample Song 3",
                artist = "Test Artist",
                audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
                coverImageUrl = "https://picsum.photos/400/400?random=3"
            )
        )
    }
}