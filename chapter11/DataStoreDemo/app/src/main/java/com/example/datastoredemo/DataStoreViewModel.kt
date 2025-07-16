package com.example.datastoredemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DataStoreViewModel(application: Application) : AndroidViewModel(application) {
    
    private val preferencesManager = PreferencesManager(application)
    
    // Expose preferences as StateFlow for the UI
    val userName: StateFlow<String> = preferencesManager.userName.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        "Guest"
    )
    
    val darkMode: StateFlow<Boolean> = preferencesManager.darkMode.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        false
    )
    
    val fontSize: StateFlow<Int> = preferencesManager.fontSize.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        16
    )
    
    val notifications: StateFlow<Boolean> = preferencesManager.notifications.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        true
    )
    
    // Update functions
    fun updateUserName(name: String) {
        viewModelScope.launch {
            preferencesManager.updateUserName(name)
        }
    }
    
    fun updateDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateDarkMode(enabled)
        }
    }
    
    fun updateFontSize(size: Int) {
        viewModelScope.launch {
            preferencesManager.updateFontSize(size)
        }
    }
    
    fun updateNotifications(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateNotifications(enabled)
        }
    }
    
    fun clearAllPreferences() {
        viewModelScope.launch {
            preferencesManager.clearAllPreferences()
        }
    }
} 