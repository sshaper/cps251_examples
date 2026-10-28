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

// Retains this screen state across configuration changes, not process death.
// The repository is supplied by the factory rather than constructed here.
class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {
    // Only this class can mutate each flow; the screen collects read-only views.
    private val _zipcode = MutableStateFlow("")
    val zipcode: StateFlow<String> = _zipcode.asStateFlow()

    // Null means no successful response is currently available to display.
    private val _weatherResponse = MutableStateFlow<WeatherResponse?>(null)
    val weatherResponse: StateFlow<WeatherResponse?> = _weatherResponse.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Typing changes input state only; it does not start a network request.
    fun onZipcodeChange(newZipcode: String) {
        _zipcode.value = newZipcode
    }

    // Each click starts a job. There is no loading flag or latest-request policy.
    fun fetchWeather() {
        // Clearing the ViewModel cancels this scope, including a suspended call.
        viewModelScope.launch {
            // Keep old weather visible while requesting again; clear the old error.
            _errorMessage.value = null
            repository.getCurrentWeather(_zipcode.value)
                .onSuccess { response ->
                    _weatherResponse.value = response
                }
                .onFailure { e ->
                    // Some exceptions have no message. Keep failure distinguishable
                    // from the initial state, whose errorMessage is null.
                    _errorMessage.value = e.message ?: "Unable to retrieve weather."
                    _weatherResponse.value = null
                }
        }
    }

    // Android calls this factory only when the owner needs a new ViewModel.
    // The anonymous factory captures the repository passed to provideFactory.
    companion object {
        fun provideFactory(repository: WeatherRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    // Can a WeatherViewModel be assigned to the requested type?
                    if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                        return WeatherViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}