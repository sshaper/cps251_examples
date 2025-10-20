package com.example.counterviewmodel.screens

import android.R.attr.padding
import android.R.attr.top
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.counterviewmodel.viewmodels.CounterViewModel

@Composable
fun CounterScreen(
    viewModel: CounterViewModel = viewModel()
) {
    Column(modifier = Modifier
        .padding(16.dp)
        .padding(top = 50.dp)) {
        Text("Count: ${viewModel.count}")
        Button(onClick = { viewModel.increment() }) {
            Text("Increment")
        }
    }
}