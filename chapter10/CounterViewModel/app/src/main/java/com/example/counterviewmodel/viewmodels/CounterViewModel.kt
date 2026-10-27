package com.example.counterviewmodel.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
/**
 * Same counter responsibility as CounterLogic, with a managed ViewModel lifetime.
 * This standalone app retrieves it for the Activity, retaining it across rotation.
 * The count is in memory, not disk storage or automatic process-death recovery.
 */
class CounterViewModel : ViewModel() {
    // Compose observes count reads. The private setter keeps writes in this class.
    // Delegation requires getValue and setValue; mutableStateOf supplies the holder.
    var count by mutableStateOf(0)
        private set
    fun increment() { count++ }
}