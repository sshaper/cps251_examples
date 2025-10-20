package com.example.counterscreen.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class CounterLogic {
    var count by mutableStateOf(0)
        private set
    fun increment() { count++ }
}