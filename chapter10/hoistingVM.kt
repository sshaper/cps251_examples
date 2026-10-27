package com.example.bookexamplesapp

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

/**
 * One observable count shared by the screen's display and both controls.
 * Retrieved through viewModel(), this Activity-owned instance survives rotation.
 * It does not save the count to disk or automatically recover after process death.
 */
class CounterViewModel : ViewModel() {
    // Only this class writes the state. The public getter exposes its Int value.
    // Compose observes the underlying state read even through the getter.
    private var _count = mutableStateOf(0)
    val count: Int
        get() = _count.value

    // The parent forwards the increment-button event here.
    fun increment() {
        _count.value = _count.value + 1
    }

    // Keep the matching decrement operation beside the state it changes.
    fun decrement() {
        _count.value = _count.value - 1
    }
}