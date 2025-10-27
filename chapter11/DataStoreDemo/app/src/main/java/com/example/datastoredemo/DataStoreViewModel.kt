package com.example.datastoredemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// This ViewModel acts as a bridge between the UI (DataStoreScreen) and the data layer
// (PreferencesManager). It exposes preference data as StateFlows for the UI to observe
// and provides functions to update those preferences, abstracting away the underlying
// DataStore operations.
class DataStoreViewModel(application: Application) : AndroidViewModel(application) {
    
    // An instance of PreferencesManager to interact with DataStore for reading and writing preferences.
    private val preferencesManager = PreferencesManager(application)
    
    // Exposes the user's name preference as a StateFlow.
    // `stateIn` converts the Flow from PreferencesManager into a StateFlow,
    // making it lifecycle-aware and optimized for UI observation.
    // `SharingStarted.Lazily` means the flow starts collecting only when there's an active collector.
    // "Guest" is the initial value until the actual user name is loaded.
    val userName: StateFlow<String> = preferencesManager.userName.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        "Guest"
    )
    
    // Exposes the dark mode preference as a StateFlow.
    // `false` is the initial value (dark mode off) until the preference is loaded.
    val darkMode: StateFlow<Boolean> = preferencesManager.darkMode.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        false
    )
    
    // Exposes the font size preference as a StateFlow.
    // `16` is the initial value (16sp) until the preference is loaded.
    val fontSize: StateFlow<Int> = preferencesManager.fontSize.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        16
    )
    
    // Exposes the notifications preference as a StateFlow.
    // `true` is the initial value (notifications on) until the preference is loaded.
    val notifications: StateFlow<Boolean> = preferencesManager.notifications.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        true
    )
    
    // --- Update functions ---

    // Launches a coroutine in the viewModelScope to update the user name.
    // It delegates the actual data modification to the PreferencesManager.
    fun updateUserName(name: String) {
        viewModelScope.launch {
            preferencesManager.updateUserName(name)
        }
    }
    
    // Launches a coroutine to update the dark mode setting.
    fun updateDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateDarkMode(enabled)
        }
    }
    
    // Launches a coroutine to update the font size setting.
    fun updateFontSize(size: Int) {
        viewModelScope.launch {
            preferencesManager.updateFontSize(size)
        }
    }
    
    // Launches a coroutine to update the notifications setting.
    fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateNotifications(enabled)
        }
    }
    
    // Launches a coroutine to clear all preferences, effectively resetting them to defaults.
    fun clearAllPreferences() {
        viewModelScope.launch {
            preferencesManager.clearAllPreferences()
        }
    }
} 