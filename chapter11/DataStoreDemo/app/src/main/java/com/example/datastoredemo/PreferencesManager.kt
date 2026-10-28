package com.example.datastoredemo

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// This file defines the PreferencesManager, which acts as a central point for managing
// user preferences using Jetpack DataStore. It defines keys for different settings
// and provides functions to read and write these settings persistently.

// Object to hold the keys for various preferences. Using object ensures these keys are
// singletons and consistently referenced throughout the app.
object PreferencesKeys {
    // Defines a preference key for storing the user's name as a String.
    val USER_NAME = stringPreferencesKey("user_name")
    // Defines a preference key for storing the dark mode setting as a Boolean.
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    // Defines a preference key for storing the font size as an Int.
    val FONT_SIZE = intPreferencesKey("font_size")
    // Defines a preference key for storing the notifications setting as a Boolean.
    val NOTIFICATIONS = booleanPreferencesKey("notifications")
}

// Extension property on Context to create a singleton DataStore instance.
// The `preferencesDataStore` delegate creates and manages the DataStore.
// The runtime file is simple_preferences.preferences_pb in app-private files/datastore.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "simple_preferences")

// Manages reading and writing user preferences using DataStore.
// It takes a Context to access the DataStore instance.
class PreferencesManager(private val context: Context) {

    // Exposes the user's name as a Flow.
    // Any changes to USER_NAME in DataStore will automatically be emitted to this Flow.
    // If USER_NAME is not set, it defaults to "Guest".
    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_NAME] ?: "Guest"
    }

    // Exposes the dark mode setting as a Flow.
    // Defaults to false (dark mode disabled) if not set.
    val darkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.DARK_MODE] ?: false
    }

    // Exposes the font size setting as a Flow.
    // Defaults to 16 if not set.
    val fontSize: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.FONT_SIZE] ?: 16
    }

    // Exposes the notifications setting as a Flow.
    // Defaults to true (notifications enabled) if not set.
    val notifications: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.NOTIFICATIONS] ?: true
    }

    // --- Update functions ---

    // Suspending write: DataStore handles storage work without blocking the UI thread.
    // The edit block commits its key changes together; suspend alone is not a thread switch.
    suspend fun updateUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = name
        }
    }

    // Suspends function to update the dark mode setting.
    suspend fun updateDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = enabled
        }
    }

    // Suspends function to update the font size.
    suspend fun updateFontSize(size: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_SIZE] = size
        }
    }

    // Suspends function to update the notifications setting.
    suspend fun updateNotifications(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NOTIFICATIONS] = enabled
        }
    }

    // Suspends function to clear all stored preferences.
    // This effectively resets all settings to their default values (as defined in the Flows).
    suspend fun clearAllPreferences() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
} 