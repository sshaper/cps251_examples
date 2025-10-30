package com.example.weaterapp.api

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Root data class representing the weather API response.
 * Maps JSON structure from OpenWeatherMap API using GsonConverterFactory.
 */
data class WeatherResponse(
    /** Main weather data (temperature and humidity) */
    val main: Main,
    
    /** List of weather descriptions (usually one item) */
    val weather: List<WeatherDescription>,
    
    /** City name (nullable - not all responses include it) */
    val name: String? = null
)

/**
 * Main weather metrics from the API response.
 */
data class Main(
    /** Temperature value (unit depends on 'units' parameter in API call) */
    val temp: Double,
    
    /** Humidity percentage (0-100) */
    val humidity: Int
)

/**
 * Weather condition description.
 */
data class WeatherDescription(
    /** Text description (e.g., "clear sky", "few clouds") */
    val description: String
)

/**
 * Retrofit interface defining API endpoints.
 * Retrofit generates method implementations at runtime based on annotations.
 * BASE_URL from RetrofitInstance is prepended to endpoint paths.
 */
interface WeatherApi {
    /**
     * Fetches weather data by ZIP code.
     * 
     * URL: BASE_URL + "data/2.5/weather?units=metric" + query parameters
     * Example: https://api.openweathermap.org/data/2.5/weather?units=metric&zip=48197,us&appid=KEY
     * 
     * @param zip ZIP code and country, format: "48197,us"
     * @param apiKey OpenWeatherMap API key for authentication
     * @return WeatherResponse with parsed weather data
     */
    @GET("data/2.5/weather?units=metric")
    suspend fun getWeatherByZip(
        @Query("zip") zip: String,
        @Query("appid") apiKey: String
    ): WeatherResponse
} 