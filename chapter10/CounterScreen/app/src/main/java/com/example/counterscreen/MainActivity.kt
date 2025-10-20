package com.example.counterscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.counterscreen.screens.CounterScreen
import com.example.counterscreen.ui.theme.CounterScreenTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Call your composable screen and pass the ViewModel
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