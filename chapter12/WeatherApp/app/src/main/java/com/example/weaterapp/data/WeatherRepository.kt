package com.example.weaterapp.data

import android.content.Context
import com.example.weaterapp.api.RetrofitInstance
import com.example.weaterapp.api.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(context: Context) {
    private val db = WeatherDatabase.getDatabase(context) // Use the singleton database instance
    private val dao = db.weatherDao()
    private val freshnessThreshold = 60 * 60 * 1000 // 1 hour in ms

    suspend fun getWeather(zip: String, apiKey: String, forceCache: Boolean = false): WeatherEntity {
        val now = System.currentTimeMillis()
        val cached = dao.getWeatherByZip(zip)
        if (cached != null && now - cached.timestamp < freshnessThreshold) {
            return cached
        }
        if (forceCache) {
            // Only return cache if requested
            return cached ?: throw Exception("No cached data available.")
        }
        // Fetch from network
        val response = withContext(Dispatchers.IO) {
            RetrofitInstance.api.getWeatherByZip(zip, apiKey)
        }
        val entity = WeatherEntity(
            zip = zip,
            city = response.name ?: zip,
            temperature = response.main.temp,
            description = response.weather.firstOrNull()?.description ?: "",
            humidity = response.main.humidity,
            timestamp = now
        )
        dao.insertWeather(entity)
        return entity
    }
} 