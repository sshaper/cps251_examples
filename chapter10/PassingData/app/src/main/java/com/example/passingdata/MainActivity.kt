package com.example.passingdata

import android.R.attr.name
import android.R.attr.text
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.passingdata.ui.theme.PassingDataTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PassingDataTheme {
                // Supply system-bar padding to the screen through its modifier.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PassingData(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PassingData(modifier: Modifier = Modifier) {

    // The Activity is the owner here because this app has no navigation host.
    // Retrieval reuses its managed instance across recomposition and rotation.
    val viewModel: MainViewModel = viewModel()

    // Collect each StateFlow into observable Compose State. The by delegate
    // reads its String value, so Name and Output can be used directly below.
    // collectAsState follows composition lifetime; it does not pause collection
    // just because the Activity moves to the background.
    val Name by viewModel.Name.collectAsState()
    val Output by viewModel.output.collectAsState()

    Column(modifier = modifier.padding(16 .dp)) {

        Text(
            text = "Enter Name",
            modifier = modifier
        )

        // State comes down from the ViewModel; edited text goes back as an event.
        // it is the full new String supplied by onValueChange, not one character.
        OutlinedTextField(
            value = Name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Enter Name") },
        )

        // The ViewModel formats the message; the UI only requests and displays it.
        Button(onClick = { viewModel.displayName() }) {
            Text("Display Name")
        }

        Text(text = Output)
    }
}

