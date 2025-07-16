package com.example.weaterapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val zip: String,
    val city: String,
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val timestamp: Long // for cache freshness
)

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE zip = :zip LIMIT 1")
    suspend fun getWeatherByZip(zip: String): WeatherEntity?

    @Insert
    suspend fun insertWeather(weather: WeatherEntity)
} 