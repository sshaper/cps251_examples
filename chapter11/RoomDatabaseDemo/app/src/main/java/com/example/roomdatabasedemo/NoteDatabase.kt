package com.example.roomdatabasedemo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates the class as a Room database, specifying the entities it contains (Note) and the version.
// exportSchema is set to false to prevent exporting schema into a folder.
@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    // Abstract method to provide the Data Access Object (DAO) for notes.
    // Room will generate the implementation for this method.
    abstract fun noteDao(): NoteDao

    // Companion object allows us to define static-like members and methods for the database.
    companion object {
        // The @Volatile annotation ensures that changes to the INSTANCE variable are immediately
        // visible to all threads. This does not by itself make database creation atomic.
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        // Reuses the database reference for this app's Activity/factory creation path.
        // The block serializes creation but has no second INSTANCE check inside it.
        // This is not double-checked locking for concurrent first-time callers.
        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                // The outer check found no instance; build and cache the database object.
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Application context to prevent memory leaks.
                    NoteDatabase::class.java,
                    "notes_db" // The name of the database file.
                ).build()
                INSTANCE = instance
                instance

            }
        }
    }
}

