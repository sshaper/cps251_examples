package com.example.weaterapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.weaterapp.data.WeatherEntity
import com.example.weaterapp.data.WeatherRepository
import com.example.weaterapp.util.NetworkUtils
import kotlinx.coroutines.launch

/**
 * Main composable function for the Weather App screen.
 * Manages UI state and coordinates weather data fetching with caching support.
 */
@Composable
fun WeatherApp() {
    val context = LocalContext.current
    val repository = remember { WeatherRepository(context) }
    
    // UI state variables
    var zip by remember { mutableStateOf("") }
    var weather by remember { mutableStateOf<WeatherEntity?>(null) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    var fromCache by remember { mutableStateOf(false) }
    
    val coroutineScope = rememberCoroutineScope()
    val apiKey = "80d537a4b4cd7a3b10a3c65a70316965"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = zip,
                onValueChange = { zip = it },
                label = { Text("Enter zip code (e.g. 48197,us)") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (zip.isNotBlank()) {
                        loading = true
                        error = null
                        fromCache = false
                        coroutineScope.launch {
                            // Step 1: Show cached data instantly if available (optimistic UI)
                            try {
                                val cached = repository.getWeather(zip, apiKey, forceCache = true)
                                weather = cached
                                fromCache = true
                            } catch (_: Exception) {
                                weather = null
                                fromCache = false
                            }
                            
                            // Step 2: Update from network if online, otherwise show error
                            if (NetworkUtils.isOnline(context)) {
                                try {
                                    val entity = repository.getWeather(zip, apiKey)
                                    weather = entity
                                    fromCache = false
                                } catch (e: Exception) {
                                    error = "Could not update weather from network. Showing cached data if available."
                                }
                            } else {
                                error = "Offline: showing cached data if available."
                            }
                            loading = false
                        }
                    }
                }
            ) {
                Text("Get Weather")
            }
            
            // Display error message if any
            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            // Show loading indicator or weather data
            when {
                loading -> CircularProgressIndicator()
                weather != null -> WeatherDisplayCached(weather!!, fromCache)
            }
        }
    }
}

/**
 * Displays weather information including temperature, description, and humidity.
 * Shows a cache indicator when displaying cached data.
 */
@Composable
fun WeatherDisplayCached(weather: WeatherEntity, fromCache: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Weather in ${weather.city}", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Temperature: ${weather.temperature}°C")
        Text("Description: ${weather.description}")
        Text("Humidity: ${weather.humidity}%")
        if (fromCache) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("(Data from cache)", color = MaterialTheme.colorScheme.secondary)
        }
    }
}