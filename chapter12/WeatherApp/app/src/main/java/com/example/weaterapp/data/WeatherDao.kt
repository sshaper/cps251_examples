package com.example.weaterapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather WHERE zip = :zip LIMIT 1")
    suspend fun getWeatherByZip(zip: String): WeatherEntity?

    @Insert
    suspend fun insertWeather(weather: WeatherEntity)
}