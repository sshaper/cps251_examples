package com.example.weaterapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weaterapp.data.WeatherEntity
import com.example.weaterapp.data.WeatherRepository
import com.example.weaterapp.util.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing weather app state and business logic.
 * Follows MVVM pattern by separating UI state from business logic.
 * 
 * This ViewModel:
 * - Manages all UI-related state using StateFlow
 * - Coordinates with WeatherRepository for data fetching
 * - Handles network connectivity checks
 * - Implements optimistic UI updates (shows cache first, then updates from network)
 */
class WeatherMainViewModel(
    private val repository: WeatherRepository,
    private val context: Context
) : ViewModel() {
    
    // API key for OpenWeatherMap API
    private val apiKey = "xxxxxxx"
    
    // Private mutable state flows
    private val _zipcode = MutableStateFlow("")
    val zipcode: StateFlow<String> = _zipcode.asStateFlow()
    
    private val _weather = MutableStateFlow<WeatherEntity?>(null)
    val weather: StateFlow<WeatherEntity?> = _weather.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    
    private val _fromCache = MutableStateFlow(false)
    val fromCache: StateFlow<Boolean> = _fromCache.asStateFlow()
    
    /**
     * Updates the zipcode state when user types in the input field.
     * 
     * @param newZipcode The new zipcode value entered by the user
     */
    fun onZipcodeChange(newZipcode: String) {
        _zipcode.value = newZipcode
    }
    
    /**
     * Fetches weather data for the current zipcode.
     * 
     * Implements optimistic UI strategy:
     * 1. First tries to show cached data instantly (if available)
     * 2. Then updates from network if online, otherwise shows appropriate error
     * 
     * All state updates happen through StateFlow, which automatically triggers
     * UI recomposition via collectAsState() in the composable.
     */
    fun fetchWeather() {
        val zip = _zipcode.value
        if (zip.isBlank()) {
            return
        }
        
        // Update loading state
        _loading.value = true
        _error.value = null
        _fromCache.value = false
        
        // Use viewModelScope to launch coroutines
        // viewModelScope automatically cancels when ViewModel is cleared
        viewModelScope.launch {
            try {
                // Step 1: Show cached data instantly if available (optimistic UI)
                try {
                    val cached = repository.getWeather(zip, apiKey, forceCache = true)
                    _weather.value = cached
                    _fromCache.value = true
                } catch (_: Exception) {
                    // No cached data available, that's okay
                    _weather.value = null
                    _fromCache.value = false
                }
                
                // Step 2: Update from network if online, otherwise show error
                if (NetworkUtils.isOnline(context)) {
                    try {
                        val entity = repository.getWeather(zip, apiKey)
                        _weather.value = entity
                        _fromCache.value = false
                    } catch (e: Exception) {
                        _error.value = "Could not update weather from network. Showing cached data if available."
                    }
                } else {
                    _error.value = "Offline: showing cached data if available."
                }
            } finally {
                _loading.value = false
            }
        }
    }
    
    /**
     * Factory for creating WeatherMainViewModel instances with dependencies.
     * 
     * This factory pattern allows dependency injection, making the ViewModel:
     * - Testable (can inject mock repositories)
     * - Maintainable (dependencies are explicit)
     * - Following clean architecture principles
     * 
     * @param repository WeatherRepository instance for data operations
     * @param context Android Context for network connectivity checks
     * @return ViewModelProvider.Factory that creates WeatherMainViewModel instances
     */
    companion object {
        fun provideFactory(
            repository: WeatherRepository,
            context: Context
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(WeatherMainViewModel::class.java)) {
                        return WeatherMainViewModel(repository, context) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
