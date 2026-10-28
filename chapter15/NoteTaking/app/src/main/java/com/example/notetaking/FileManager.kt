package com.example.notetaking

import android.content.Context

/** Batch convenience over the same per-note store; not a transaction. The UI uses SimpleFileManager. */
class FileManager(context: Context) {
    private val store = SimpleFileManager(context)
    fun saveNotes(notes: List<Note>): Boolean = notes.map { store.saveNote(it) }.all { it }
    fun loadNotes(): List<Note> = store.getAllNoteIds().mapNotNull { store.loadNote(it) }
}
