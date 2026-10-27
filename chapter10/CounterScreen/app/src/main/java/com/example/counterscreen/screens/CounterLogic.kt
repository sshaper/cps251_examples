package com.example.counterscreen.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * A plain state holder: groups the observable count and its update operation.
 * It has no Android-managed lifetime. CounterScreen remembers this instance.
 */
class CounterLogic {
    // by delegates access to Compose state; getValue/setValue support delegation.
    // The screen can read count but must request changes through increment().
    var count by mutableStateOf(0)
        private set
    fun increment() { count++ }
}