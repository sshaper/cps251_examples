package com.example.book

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainViewModel : ViewModel() {
    // The state is shared between screens
    var count by mutableStateOf(0)
        private set

    fun increment() { count++ }
}
