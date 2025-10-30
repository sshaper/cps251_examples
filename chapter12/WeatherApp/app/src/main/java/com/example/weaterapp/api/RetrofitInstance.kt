package com.example.weaterapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object that provides a configured Retrofit instance.
 * Centralizes Retrofit configuration and ensures only one instance exists.
 */
object RetrofitInstance {
    /** Base URL for all API requests. Retrofit appends endpoint paths to this URL. */
    private const val BASE_URL = "https://api.openweathermap.org/"

    /**
     * Lazy-initialized property providing access to the WeatherApi interface.
     * Created on first access using Retrofit.Builder with Gson converter for JSON parsing.
     */
    val api: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
} 