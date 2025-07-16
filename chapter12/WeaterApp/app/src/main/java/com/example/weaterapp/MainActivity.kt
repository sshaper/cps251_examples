package com.example.weaterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.weaterapp.api.RetrofitInstance
import com.example.weaterapp.api.WeatherResponse
import com.example.weaterapp.data.WeatherEntity
import com.example.weaterapp.data.WeatherRepository
import com.example.weaterapp.util.NetworkUtils
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure the window to use light status bar icons
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true


        // Set up the Compose UI
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherApp()
                }
            }
        }
    }
}

@Composable
fun WeatherApp() {
    val context = LocalContext.current
    val repository = remember { WeatherRepository(context) }
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
                            try {
                                // Always show cache instantly if available
                                val cached = repository.getWeather(zip, apiKey, forceCache = true)
                                weather = cached
                                fromCache = true
                            } catch (_: Exception) {
                                weather = null
                                fromCache = false
                            }
                            // Then try to update from network if online
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
            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            when {
                loading -> CircularProgressIndicator()
                weather != null -> WeatherDisplayCached(weather!!, fromCache)
            }
        }
    }
}

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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    WeatherApp ()
}