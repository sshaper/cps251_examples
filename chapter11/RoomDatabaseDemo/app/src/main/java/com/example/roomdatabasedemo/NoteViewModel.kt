package com.example.roomdatabasedemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "notes_db"
    ).build()
    private val repository = NoteRepository(db.noteDao())
    val notes: StateFlow<List<Note>> = repository.allNotes.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
    fun addNote(title: String, content: String, date: String) {
        viewModelScope.launch {
            repository.insert(Note(title = title, content = content, date = date))
        }
    }
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.delete(note)
        }
    }
    companion object {
        fun provideFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return NoteViewModel(application) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
} 