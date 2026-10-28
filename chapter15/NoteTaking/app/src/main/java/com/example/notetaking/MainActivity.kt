package com.example.notetaking

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
import com.example.notetaking.ui.theme.NoteTakingTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteTakingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NoteTakingApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun NoteTakingApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val fileManager = remember { SimpleFileManager(context) }
    val scope = rememberCoroutineScope()
    
    var notes by remember { mutableStateOf<List<Note>>(emptyList()) }
    var selectedNote by remember { mutableStateOf<Note?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var saving by remember { mutableStateOf(false) }
    // Perform blocking file work on IO and publish the returned data from the calling coroutine.
    suspend fun readNotes(): List<Note> = withContext(Dispatchers.IO) {
        fileManager.getAllNoteIds().mapNotNull { fileManager.loadNote(it) }
    }
    LaunchedEffect(Unit) {
        try { notes = readNotes() }
        catch (cancelled: kotlinx.coroutines.CancellationException) { throw cancelled }
        catch (error: Exception) { errorMessage = "Could not load notes." }
    }
    androidx.compose.foundation.layout.Column(modifier) {
    errorMessage?.let { androidx.compose.material3.Text(it) }
    if (isEditing) {
        NoteEditorScreen(
            note = selectedNote,
            onSave = { note ->
                if (!saving) scope.launch {
                    saving = true
                    try {
                        val saved = withContext(Dispatchers.IO) { fileManager.saveNote(note) }
                        if (saved) {
                            notes = readNotes()
                            isEditing = false
                            selectedNote = null
                            errorMessage = null
                        } else errorMessage = "Could not save note. Your editor is still open."
                    } catch (cancelled: kotlinx.coroutines.CancellationException) { throw cancelled }
                    catch (error: Exception) { errorMessage = "Could not reload notes after saving." }
                    finally { saving = false }
                }
            },
            onBackClick = {
                isEditing = false
                selectedNote = null
            }
        )
    } else {
        NoteListScreen(
            notes = notes,
            onNoteClick = { note ->
                selectedNote = note
                isEditing = true
            },
            onAddNoteClick = {
                selectedNote = null
                isEditing = true
            }
        )
    }
}
}
