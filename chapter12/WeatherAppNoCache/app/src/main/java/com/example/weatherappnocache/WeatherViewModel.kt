// app/src/main/java/com/example/weatherappnocache/WeatherViewModel.kt
package com.example.weatherappnocache

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherappnocache.api.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    private val _zipcode = MutableStateFlow("")
    val zipcode: StateFlow<String> = _zipcode.asStateFlow()

    private val _weatherResponse = MutableStateFlow<WeatherResponse?>(null)
    val weatherResponse: StateFlow<WeatherResponse?> = _weatherResponse.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onZipcodeChange(newZipcode: String) {
        _zipcode.value = newZipcode
    }

    fun fetchWeather() {
        viewModelScope.launch {
            _errorMessage.value = null // Clear any previous error
            repository.getCurrentWeather(_zipcode.value)
                .onSuccess { response ->
                    _weatherResponse.value = response
                }
                .onFailure { e ->
                    _errorMessage.value = e.message
                    _weatherResponse.value = null
                }
        }
    }

    // Factory for ViewModel injection
    companion object {
        fun provideFactory(repository: WeatherRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                        return WeatherViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}