package com.example.roomdatabasedemo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// Opt in to experimental Material 3 APIs used by this example.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(viewModel: NoteViewModel) {
    // Collect the list of notes from the ViewModel as a State, so UI recomposes on changes.
    val notes by viewModel.notes.collectAsState()
    // State for the title input field.
    var title by remember { mutableStateOf("") }
    // State for the content input field.
    var content by remember { mutableStateOf("") }
    // Original saved note being edited; typing changes only local title/content values.
    var editingNote by remember { mutableStateOf<Note?>(null) }
    // State to control the visibility of the delete confirmation dialog.
    var showDeleteDialog by remember { mutableStateOf<Note?>(null) }
    // Formatter for displaying dates in a consistent format.
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    // State for managing SnackBar messages.
    val snackbarHostState = remember { SnackbarHostState() }
    // Coroutine scope for launching suspending functions, like showing snackbars.
    val scope = rememberCoroutineScope()

    // Scaffold provides a basic screen layout with a SnackBarHost.
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        // Preserve the fixed 50.dp top offset used to position the form below the phone top.
        // This layout does not apply the Scaffold padding parameter.
        Column(modifier = Modifier
            .padding(16.dp)
            .padding(top = 50.dp)) {
            // Title for the note input section.
            Text("Add a Note", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            // Outlined text field for note title input.
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            // Outlined text field for note content input.
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Row for action buttons (Add/Update Note, Cancel Edit).
            Row {
                // Button to add a new note or update an existing one.
                Button(onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        if (editingNote == null) {
                            // If not editing, add a new note.
                            viewModel.addNote(title, content, dateFormat.format(Date()))
                            scope.launch { snackbarHostState.showSnackbar("Note added!") }
                        } else {
                            // copy changes the edited values while preserving the saved note's ID.
                            // The date records the most recent save, as it does when adding a note.
                            viewModel.updateNote(editingNote!!.copy(
                                title = title,
                                content = content,
                                date = dateFormat.format(Date())
                            ))
                            editingNote = null
                            scope.launch { snackbarHostState.showSnackbar("Note updated!") }
                        }
                        // Clear input fields after action.
                        title = ""
                        content = ""
                    }
                }) {
                    // Button text changes based on whether a note is being edited.
                    Text(if (editingNote == null) "Add Note" else "Update Note")
                }
                // Show "Cancel Edit" button only when a note is being edited.
                if (editingNote != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(onClick = {
                        // Clear editing state and input fields on cancel.
                        editingNote = null
                        title = ""
                        content = ""
                    }) {
                        Text("Cancel Edit")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Title for the list of notes.
            Text("Your Notes", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            // LazyColumn to efficiently display a scrollable list of notes.
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(notes.size) { idx ->
                    val note = notes[idx]
                    // Surface for individual note display, with a slight elevation.
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        tonalElevation = 2.dp
                    ) {
                        // Row to arrange note details and action buttons horizontally.
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Column for displaying note title, content, and date.
                            Column(modifier = Modifier.weight(1f)) {
                                Text(note.title, style = MaterialTheme.typography.titleSmall)
                                Text(note.content, style = MaterialTheme.typography.bodyMedium)
                                Text(note.date, style = MaterialTheme.typography.bodySmall)
                            }
                            // Column for Edit and Delete buttons.
                            Column {
                                // Button to initiate editing of a note.
                                Button(onClick = {
                                    editingNote = note
                                    title = note.title
                                    content = note.content
                                }) {
                                    Text("Edit")
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                // Button to show delete confirmation dialog.
                                Button(onClick = { showDeleteDialog = note }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
        // Delete confirmation AlertDialog.
        if (showDeleteDialog != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Delete Note") },
                text = { Text("Are you sure you want to delete this note?") },
                confirmButton = {
                    TextButton(onClick = {
                        // Request deletion; the snackbar is not an awaited database-success result.
                        viewModel.deleteNote(showDeleteDialog!!)
                        scope.launch { snackbarHostState.showSnackbar("Note deleted!") }
                        // If the deleted note was being edited, clear editing state.
                        if (editingNote == showDeleteDialog) {
                            editingNote = null
                            title = ""
                            content = ""
                        }
                        // Keep the deletion target until the editing comparison is complete.
                        showDeleteDialog = null
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
} 
