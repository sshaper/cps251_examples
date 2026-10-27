package com.example.book

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

/**
 * Counter state and the operation that changes it belong together here.
 * Android retains this instance across rotation when retrieved with viewModel().
 * This is in-memory state, not a saved file or process-death recovery mechanism.
 */
class MainViewModel : ViewModel() {
    // Compose tracks reads of this observable state even through the count getter.
    // Keep the mutable holder private so the UI requests changes through increment().
    private var _count = mutableStateOf(0)
    // Public callers can read count; only this class can use its setter.
    // The property is a var with restricted write access, rather than a val.
    var count: Int
        get() = _count.value
        private set(value) {
            _count.value = value
        }

    // The button sends an event here; changing the state updates observing UI.
    fun increment() {
        count = count + 1
    }
}
