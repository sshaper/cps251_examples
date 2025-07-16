package com.example.weaterapp.api

import retrofit2.http.GET
import retrofit2.http.Query

// Data class for the weather response (main fields only)
data class WeatherResponse(
    val main: Main,
    val weather: List<WeatherDescription>,
    val name: String? = null // city name
)

data class Main(
    val temp: Double,
    val humidity: Int
)

data class WeatherDescription(
    val description: String
)

interface WeatherApi {
    // Example: https://api.openweathermap.org/data/2.5/weather?zip=48197,us&appid=YOUR_API_KEY&units=metric
    @GET("data/2.5/weather?units=metric")
    suspend fun getWeatherByZip(
        @Query("zip") zip: String, // e.g. "48197,us"
        @Query("appid") apiKey: String
    ): WeatherResponse
} 