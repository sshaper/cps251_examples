package com.example.passingdata

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Owns the typed name and the last message requested by the user.
 * These are separate states: editing the name does not regenerate the message.
 * The Activity-owned instance survives rotation, but does not persist to disk.
 */
class MainViewModel : ViewModel() {

    // MutableStateFlow holds a current value, initially an empty String.
    // The public StateFlow type exposes reads/collection without a value setter.
    // Name preserves this example's existing spelling; Kotlin properties normally
    // start with a lowercase letter. The UI must use the matching property name.
    private val _Name = MutableStateFlow("")
    val Name: StateFlow<String> = _Name

    // Output changes only when displayName() runs, not on every keystroke.
    private val _output= MutableStateFlow("")
    val output: StateFlow<String> = _output

    // Called by the text field's onValueChange with the complete new text.
    fun onNameChange(name: String) {
        _Name.value = name
    }

    // Build a message from the name stored at the moment the button is pressed.
    fun displayName() {
        _output.value = "The name is ${_Name.value}"
    }
}