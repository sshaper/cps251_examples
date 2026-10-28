package com.example.roomdatabasedemo

import kotlinx.coroutines.flow.Flow

// NoteRepository acts as an abstraction layer over the data source (NoteDao).
// It provides a clean API for the ViewModel to interact with data.
class NoteRepository(private val noteDao: NoteDao) {
    // Exposes a Flow of all notes, allowing for observing changes in the database.
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    // Suspended function to insert a new note into the database.
    // Remains suspend because the delegated DAO operation is suspending.
    suspend fun insert(note: Note) = noteDao.insert(note)

    // Forward an edited note with its original ID to Room's update operation.
    suspend fun update(note: Note) = noteDao.update(note)

    // Suspended function to delete an existing note from the database.
    // Remains suspend because the delegated DAO operation is suspending.
    suspend fun delete(note: Note) = noteDao.delete(note)
} 
