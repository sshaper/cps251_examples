package com.example.weatherappnocache.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("zip") zip: String,
        @Query("appid") appId: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}
