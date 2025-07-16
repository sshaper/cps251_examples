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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(viewModel: NoteViewModel) {
    val notes by viewModel.notes.collectAsState()
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Note?>(null) }
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier
            .padding(16.dp)
            .padding(top = 50.dp)) {
            Text("Add a Note", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        if (editingNote == null) {
                            viewModel.addNote(title, content, dateFormat.format(Date()))
                            scope.launch { snackbarHostState.showSnackbar("Note added!") }
                        } else {
                            viewModel.deleteNote(editingNote!!)
                            viewModel.addNote(title, content, dateFormat.format(Date()))
                            editingNote = null
                            scope.launch { snackbarHostState.showSnackbar("Note updated!") }
                        }
                        title = ""
                        content = ""
                    }
                }) {
                    Text(if (editingNote == null) "Add Note" else "Update Note")
                }
                if (editingNote != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(onClick = {
                        editingNote = null
                        title = ""
                        content = ""
                    }) {
                        Text("Cancel Edit")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Your Notes", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(notes.size) { idx ->
                    val note = notes[idx]
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(note.title, style = MaterialTheme.typography.titleSmall)
                                Text(note.content, style = MaterialTheme.typography.bodyMedium)
                                Text(note.date, style = MaterialTheme.typography.bodySmall)
                            }
                            Column {
                                Button(onClick = {
                                    editingNote = note
                                    title = note.title
                                    content = note.content
                                }) {
                                    Text("Edit")
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Button(onClick = { showDeleteDialog = note }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
        if (showDeleteDialog != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = null },
                title = { Text("Delete Note") },
                text = { Text("Are you sure you want to delete this note?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.deleteNote(showDeleteDialog!!)
                        scope.launch { snackbarHostState.showSnackbar("Note deleted!") }
                        showDeleteDialog = null
                        if (editingNote == showDeleteDialog) {
                            editingNote = null
                            title = ""
                            content = ""
                        }
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