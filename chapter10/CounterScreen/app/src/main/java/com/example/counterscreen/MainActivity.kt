package com.example.counterscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.counterscreen.screens.CounterScreen
import com.example.counterscreen.ui.theme.CounterScreenTheme


// This Activity starts the UI. File/package organization does not determine
// state lifetime; the screen's remember or viewModel retrieval does that.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Show the screen; it remembers its own CounterLogic instance.
            CounterScreen()

        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CounterScreenTheme {
        CounterScreen()
    }
}