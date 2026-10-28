package com.example.roomdatabasedemo

import androidx.room.Entity
import androidx.room.PrimaryKey

// Defines the 'Note' data class, which represents a table in the Room database.
// The tableName is explicitly set to "notes".
@Entity(tableName = "notes")
data class Note(
    // Zero requests an auto-generated row ID; duplicate titles can still be separate notes.
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // The title of the note.
    val title: String,
    // The main content/body of the note.
    val content: String,
    // The screen supplies yyyy-MM-dd HH:mm text, also used by the ordered query.
    val date: String
) 