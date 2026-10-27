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
    // Used when the caller omits this argument. Retrieves an instance for the
    // current owner (the Activity here), rather than creating one per screen call.
    // A caller may explicitly supply an instance instead. The package import
    // identifies the class; it does not create the instance or establish sharing.
    viewModel: CounterViewModel = viewModel()
) {
    Column(modifier = Modifier
        .padding(16.dp)
        .padding(top = 50.dp)) {
        // Read observable state; the holder owns the number and update rule.
        Text("Count: ${viewModel.count}")
        // Forward a click to the holder; the UI does not assign count directly.
        Button(onClick = { viewModel.increment() }) {
            Text("Increment")
        }
    }
}