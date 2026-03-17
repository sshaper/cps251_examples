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

    val viewModel: MainViewModel = viewModel()

    val Name by viewModel.Name.collectAsState()
    val Output by viewModel.output.collectAsState()

    Column(modifier = modifier.padding(16 .dp)) {

        Text(
            text = "Enter Name",
            modifier = modifier
        )

        OutlinedTextField(
            value = Name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Enter Name") },
        )

        Button(onClick = { viewModel.displayName() }) {
            Text("Display Name")
        }

        Text(text = Output)
    }
}

