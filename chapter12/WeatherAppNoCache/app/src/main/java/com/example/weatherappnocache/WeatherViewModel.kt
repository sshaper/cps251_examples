package com.example.weatherappnocache

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherappnocache.api.RetrofitClient
import com.example.weatherappnocache.api.WeatherResponse
import kotlinx.coroutines.launch


class WeatherViewModel : ViewModel() {
    private val _weatherResponse = mutableStateOf<WeatherResponse?>(null)
    val weatherResponse: androidx.compose.runtime.State<WeatherResponse?> = _weatherResponse
    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: androidx.compose.runtime.State<String?> = _errorMessage
    var apiKey = "80d537a4b4cd7a3b10a3c65a70316965"

    fun fetchWeather(zipcode: String) {
        viewModelScope.launch {
            try {
                _errorMessage.value = null // Clear any previous error
                val response = RetrofitClient.weatherApiService.getCurrentWeather(
                    zip = "$zipcode,us", // Append ",us" for the country code
                    appId = apiKey
                )
                _weatherResponse.value = response
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _weatherResponse.value = null
            }
        }
    }
}