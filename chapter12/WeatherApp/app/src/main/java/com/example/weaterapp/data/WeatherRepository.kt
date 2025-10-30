package com.example.weaterapp.data

import android.content.Context
import com.example.weaterapp.api.RetrofitInstance
import com.example.weaterapp.api.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository class that manages weather data from both local cache and network API.
 * Implements caching strategy: returns cached data if fresh, otherwise fetches from API.
 */
class WeatherRepository(context: Context) {
    /** Singleton database instance */
    private val db = WeatherDatabase.getDatabase(context)
    
    /** Data Access Object for weather database operations */
    private val dao = db.weatherDao()
    
    /** Cache freshness threshold: 1 hour in milliseconds */
    private val freshnessThreshold = 60 * 60 * 1000

    /**
     * Gets weather data, using cache if available and fresh, otherwise fetching from API.
     * 
     * Caching logic:
     * 1. Check if cached data exists and is within freshness threshold
     * 2. If forceCache=true, return cached data (or throw if none exists)
     * 3. Otherwise, fetch from network, save to cache, and return
     * 
     * @param zip ZIP code to fetch weather for
     * @param apiKey OpenWeatherMap API key
     * @param forceCache If true, only return cached data (no network fetch)
     * @return WeatherEntity with weather data
     */
    suspend fun getWeather(zip: String, apiKey: String, forceCache: Boolean = false): WeatherEntity {
        val now = System.currentTimeMillis()
        val cached = dao.getWeatherByZip(zip)
        
        // Return cached data if it exists and is fresh (within threshold)
        if (cached != null && now - cached.timestamp < freshnessThreshold) {
            return cached
        }
        
        // If forceCache requested, return cached data or throw exception
        if (forceCache) {
            return cached ?: throw Exception("No cached data available.")
        }
        
        // Fetch from network on IO dispatcher
        val response = withContext(Dispatchers.IO) {
            RetrofitInstance.api.getWeatherByZip(zip, apiKey)
        }
        
        // Convert API response to entity and save to cache
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