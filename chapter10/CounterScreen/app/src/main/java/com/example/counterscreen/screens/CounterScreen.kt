package com.example.counterscreen.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CounterScreen() {
    // Remember the object, not just its number. Recomposition reuses it here.
    // Removing this location or recreating the Activity loses this instance.
    // CounterLogic shares this package, so it needs no import in this file.
    val counterLogic = remember { CounterLogic() }

    Column(modifier = Modifier
        .padding(16.dp)
        .padding(top = 50.dp)

    ) {
        // Read observable state; the holder owns the number and update rule.
        Text("Count: ${counterLogic.count}")
        // Forward a click to the holder; the UI does not assign count directly.
        Button(onClick = { counterLogic.increment() }) {
            Text("Increment")
        }
    }
}