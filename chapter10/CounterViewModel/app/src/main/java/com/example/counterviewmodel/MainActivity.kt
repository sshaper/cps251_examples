package com.example.counterviewmodel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.counterviewmodel.screens.CounterScreen
import com.example.counterviewmodel.ui.theme.CounterViewModelTheme





// This Activity starts the UI. File/package organization does not determine
// state lifetime; the screen's remember or viewModel retrieval does that.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Show the screen; its default parameter retrieves the managed ViewModel.
            CounterScreen()

        }
    }
}

