package com.example.weatherappnocache.api

import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit reads these annotations to construct requests for the weather endpoint.
interface WeatherApiService {
    // Relative to RetrofitClient.BASE_URL; query parameters are added below.
    @GET("data/2.5/weather")
    // Retrofit uses asynchronous HTTP and resumes with a model or an exception.
    suspend fun getCurrentWeather(
        // The repository supplies ZIP,country, for example 48843,us.
        @Query("zip") zip: String,
        @Query("appid") appId: String,
        // Omitted by the repository, so this default requests Celsius and m/s.
        @Query("units") units: String = "metric"
    ): WeatherResponse
}
