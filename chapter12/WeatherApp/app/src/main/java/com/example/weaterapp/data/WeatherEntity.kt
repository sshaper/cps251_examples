package com.example.weaterapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing weather data in the database.
 * @Entity annotation maps this class to the "weather" table.
 * Room uses this to generate database schema and queries.
 */
@Entity(tableName = "weather")
data class WeatherEntity(
    /** Primary key: ZIP code (unique identifier for each weather record) */
    @PrimaryKey val zip: String,
    
    /** City name */
    val city: String,
    
    /** Temperature value */
    val temperature: Double,
    
    /** Weather condition description */
    val description: String,
    
    /** Humidity percentage */
    val humidity: Int,
    
    /** Timestamp in milliseconds - used to check cache freshness */
    val timestamp: Long
)
