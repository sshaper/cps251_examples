package com.example.passingdata

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val _Name = MutableStateFlow("")
    val Name: StateFlow<String> = _Name

    private val _output= MutableStateFlow("")
    val output: StateFlow<String> = _output

    fun onNameChange(name: String) {
        _Name.value = name
    }

    fun displayName() {
        _output.value = "The name is ${_Name.value}"
    }
}