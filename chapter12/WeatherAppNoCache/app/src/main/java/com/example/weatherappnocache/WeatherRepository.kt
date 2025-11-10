// In a new file: app/src/main/java/com/example/weatherappnocache/WeatherRepository.kt
package com.example.weatherappnocache

import com.example.weatherappnocache.api.WeatherApiService
import com.example.weatherappnocache.api.WeatherResponse

class WeatherRepository(private val weatherApiService: WeatherApiService) {

    // You would typically get the API key from a more secure location
    // or through dependency injection in a real app.
    private val API_KEY = "80d537a4b4cd7a3b10a3c65a70316965"

    suspend fun getCurrentWeather(zipcode: String): Result<WeatherResponse> {
        return try {
            val response = weatherApiService.getCurrentWeather(
                zip = "$zipcode,us",
                appId = API_KEY
            )
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}