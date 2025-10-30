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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch



@Composable
fun WeatherAppScreen(weatherViewModel: WeatherViewModel = viewModel()) {
    var zipcode by remember { mutableStateOf("") }
    val weatherResponse by weatherViewModel.weatherResponse
    val errorMessage by weatherViewModel.errorMessage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = zipcode,
            onValueChange = { zipcode = it },
            label = { Text("Enter Zip Code") },
            singleLine = true,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Button(onClick = { weatherViewModel.fetchWeather(zipcode) }) {
            Text("Get Weather")
        }

        when {
            weatherResponse != null -> {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), horizontalAlignment = Alignment.Start) {
                    Text("City: ${weatherResponse?.name}")
                    Text("Temperature: ${weatherResponse?.main?.temp}°C")
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

