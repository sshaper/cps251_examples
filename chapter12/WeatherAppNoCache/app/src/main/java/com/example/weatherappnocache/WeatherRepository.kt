// In a new file: app/src/main/java/com/example/weatherappnocache/WeatherRepository.kt
package com.example.weatherappnocache

import com.example.weatherappnocache.api.WeatherApiService
import com.example.weatherappnocache.api.WeatherResponse
import kotlinx.coroutines.CancellationException

// The ViewModel requests weather without knowing endpoint or query details.
// This version makes a network request each time; it has no local data cache.
class WeatherRepository(private val weatherApiService: WeatherApiService) {

    // Teaching example configuration. Do not publish your own key in source control.
    // Build-time configuration can keep a key out of source, but cannot make an
    // embedded Android app key secret from someone inspecting the installed app.
    private val API_KEY = "80d537a4b4cd7a3b10a3c65a70316965"

    // Called by the ViewModel coroutine. Retrofit suspends while awaiting HTTP.
    suspend fun getCurrentWeather(zipcode: String): Result<WeatherResponse> {
        return try {
            val response = weatherApiService.getCurrentWeather(
                // The screen accepts a ZIP only; this app requests US locations.
                zip = "$zipcode,us",
                appId = API_KEY
            )
            // The service has already converted successful JSON into the model.
            Result.success(response)
        } catch (e: CancellationException) {
            // Cancellation belongs to the coroutine lifetime, not the error UI.
            throw e
        } catch (e: Exception) {
            // HTTP, connection, and conversion failures take the same result path.
            Result.failure(e)
        }
    }
}