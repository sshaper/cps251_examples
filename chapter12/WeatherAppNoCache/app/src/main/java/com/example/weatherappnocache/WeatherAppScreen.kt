package com.example.weatherappnocache

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherappnocache.api.RetrofitClient


@Composable
fun WeatherAppScreen() {
    // Accessing the lazy property configures the shared service; it does not fetch.
    val weatherApiService = RetrofitClient.weatherApiService
    // Reuse this repository during recomposition. remember is not disk storage.
    val weatherRepository = remember { WeatherRepository(weatherApiService) }
    // With no nearer owner, this Activity hosts the retained ViewModel.
    // A new factory argument does not replace an already retained instance.
    val weatherViewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModel.provideFactory(weatherRepository)
    )

    // Bridge StateFlow values into Compose State so changes update the screen.
    // collectAsState follows composition; it is not lifecycle STARTED collection.
    val zipcode by weatherViewModel.zipcode.collectAsState()
    val weatherResponse by weatherViewModel.weatherResponse.collectAsState()
    val errorMessage by weatherViewModel.errorMessage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Send edits upward; the collected value comes back down to the field.
        OutlinedTextField(
            value = zipcode,
            onValueChange = { weatherViewModel.onZipcodeChange(it) },
            label = { Text("Enter Zip Code") },
            singleLine = true,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Fetch only in response to this event, never in the composable body.
        Button(onClick = { weatherViewModel.fetchWeather() }) {
            Text("Get Weather")
        }

        // Initial, successful, and failed states; no separate loading branch.
        when {
            weatherResponse != null -> {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalAlignment = Alignment.Start) {
                    Text("City: ${weatherResponse?.name}")
                    Text("Temperature: ${weatherResponse?.main?.temp}°C")
                    // The API returns an array; an empty list has no first item.
                    Text("Description: ${weatherResponse?.weather?.firstOrNull()?.description}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Other Info:")
                    Text("Temp Min: ${weatherResponse?.main?.temp_min}")
                    Text("Temp Max: ${weatherResponse?.main?.temp_max}")
                    Text("Pressure: ${weatherResponse?.main?.pressure}")
                    Text("Humidity: ${weatherResponse?.main?.humidity}")
                    Text("Wind Speed: ${weatherResponse?.wind?.speed}")
                    Text("Wind Direction: ${weatherResponse?.wind?.deg}")
                    Text("Clouds: ${weatherResponse?.clouds?.all}")
                    Text("Sunrise: ${weatherResponse?.sys?.sunrise}")
                    Text("Sunset: ${weatherResponse?.sys?.sunset}")
                }
            }
            errorMessage != null -> {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalAlignment = Alignment.Start) {
                    // Fixed UI wording covers all failures, not only unknown ZIPs.
                    Text("No weather found for that zip code")
                }
            }
            else -> {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalAlignment = Alignment.Start) {
                    Text("Enter a zip code and press 'Get Weather'")
                }
            }
        }
    }
}

