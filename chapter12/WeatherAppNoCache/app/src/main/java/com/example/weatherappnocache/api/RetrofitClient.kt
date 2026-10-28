package com.example.weatherappnocache.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Share one service configuration in this process, independent of screen redraws.
object RetrofitClient {
    // Retrofit requires the trailing slash when resolving relative endpoint paths.
    private const val BASE_URL = "https://api.openweathermap.org/"

    // Build on first access, then reuse. No HTTP request runs in this initializer.
    val weatherApiService: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Convert a successful JSON body into the declared response classes.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            // Retrofit supplies the implementation of our annotated interface.
            .create(WeatherApiService::class.java)
    }
}
