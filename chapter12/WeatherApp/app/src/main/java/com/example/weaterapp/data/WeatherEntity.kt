package com.example.weaterapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val zip: String,
    val city: String,
    val temperature: Double,
    val description: String,
    val humidity: Int,
    val timestamp: Long // for cache freshness
)
