package com.example.weaterapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

/**
 * Data Access Object interface for weather database operations.
 * Room generates the implementation at compile time based on annotations.
 * All operations use suspend functions to run on background threads.
 */
@Dao
interface WeatherDao {
    /**
     * Retrieves weather data by ZIP code from the database.
     * Uses parameter binding (:zip) to prevent SQL injection.
     * 
     * @param zip ZIP code to search for (e.g., "48197,us")
     * @return WeatherEntity if found, null otherwise
     */
    @Query("SELECT * FROM weather WHERE zip = :zip LIMIT 1")
    suspend fun getWeatherByZip(zip: String): WeatherEntity?

    /**
     * Inserts or replaces weather data in the database.
     * Replaces existing record if zip (primary key) already exists.
     * 
     * @param weather WeatherEntity to insert/update
     */
    @Insert
    suspend fun insertWeather(weather: WeatherEntity)
}