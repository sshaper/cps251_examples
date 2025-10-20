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
    val counterLogic = remember { CounterLogic() }

    Column(modifier = Modifier
        .padding(16.dp)
        .padding(top = 50.dp)

    ) {
        Text("Count: ${counterLogic.count}")
        Button(onClick = { counterLogic.increment() }) {
            Text("Increment")
        }
    }
}