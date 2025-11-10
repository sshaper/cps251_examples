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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weaterapp.data.WeatherEntity
import com.example.weaterapp.data.WeatherRepository

/**
 * Main composable function for the Weather App screen.
 * 
 * This composable follows the MVVM pattern:
 * - UI (Composable) observes state from ViewModel using collectAsState()
 * - Business logic and state management are handled by WeatherMainViewModel
 * - Data fetching is delegated to WeatherRepository through the ViewModel
 * 
 * The UI is now focused solely on displaying data and handling user interactions,
 * while all business logic lives in the ViewModel.
 */
@Composable
fun WeatherApp() {
    val context = LocalContext.current
    
    // Create repository instance (persisted across recompositions)
    val repository = remember { WeatherRepository(context) }
    
    // Create ViewModel using factory pattern with dependency injection
    val viewModel: WeatherMainViewModel = viewModel(
        factory = WeatherMainViewModel.provideFactory(repository, context)
    )
    
    // Collect state from ViewModel using collectAsState()
    // These automatically trigger recomposition when state changes
    val zipcode by viewModel.zipcode.collectAsState()
    val weather by viewModel.weather.collectAsState()
    val error by viewModel.error.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val fromCache by viewModel.fromCache.collectAsState()

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
            // Zipcode input field - bound to ViewModel state
            OutlinedTextField(
                value = zipcode,
                onValueChange = { viewModel.onZipcodeChange(it) },
                label = { Text("Enter zip code (e.g. 48197,us)") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // Button triggers ViewModel to fetch weather
            Button(
                onClick = { viewModel.fetchWeather() }
            ) {
                Text("Get Weather")
            }
            
            // Display error message if any (from ViewModel state)
            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            // Show loading indicator or weather data (from ViewModel state)
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