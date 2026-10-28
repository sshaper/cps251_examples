package com.example.roomdatabasedemo

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// Note Data Access Object (DAO).
// This interface defines the methods for interacting with the 'notes' table in the database.
@Dao
interface NoteDao {
    // Query to retrieve all notes from the 'notes' table, ordered by date in descending order.
    // Collection observes query results; table changes trigger query updates.
    // Equal date strings have no guaranteed relative order in this query.
    @Query("SELECT * FROM notes ORDER BY date DESC")
    fun getAllNotes(): Flow<List<Note>>

    // Inserts a new note or replaces an existing one if there's a conflict (e.g., same primary key).
    // Call from a coroutine or suspending function; Room handles asynchronous database work.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    // Update the row matching this note's primary key, preserving its identity.
    // If that ID no longer exists, Room leaves the table unchanged.
    @Update
    suspend fun update(note: Note)

    // Deletes an existing note from the database.
    // Room uses the supplied entity primary key to identify the row to delete.
    @Delete
    suspend fun delete(note: Note)
} 
