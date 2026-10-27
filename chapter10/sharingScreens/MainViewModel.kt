package com.example.book

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Owns the counter used by both screens. MainActivity retrieves one managed
 * instance and passes it explicitly through MainScreen to each destination.
 * This class is not a singleton; another owner could have a different instance.
 * No disk persistence or automatic process-death restoration is implemented.
 */
class MainViewModel : ViewModel() {
    // Compose observes reads of count. by delegates to mutableStateOf's value;
    // getValue/setValue imports support that syntax. Only this class can assign
    // count because the setter is private; screens request increment() instead.
    var count by mutableStateOf(0)
        private set

    // Both buttons reach this operation on the same instance.
    fun increment() { count++ }
}
