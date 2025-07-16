package com.example.book

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * MainActivity is the entry point of the application.
 * It sets up the Compose UI and manages the ViewModel for data loading.
 */
class MainActivity : ComponentActivity() {
    // Create a single instance of the ViewModel for the activity
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    DataScreen(viewModel)
                }
            }
        }
    }
}

/**
 * DataScreen composable displays the data loading UI and counter.
 * It demonstrates that UI remains responsive during coroutine operations.
 *
 * @param viewModel The ViewModel that manages the data loading state and counter
 */
@Composable
fun DataScreen(viewModel: MainViewModel) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Data Loading Section
        Text("Data Loading Demo", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        
        // Show loading indicator when data is being loaded
        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            // Display the loaded data or placeholder text
            Text(viewModel.data ?: "No data loaded yet")
        }

        // Button to trigger data loading
        Button(
            onClick = { viewModel.loadData() },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Load Data")
        }

        // Divider between sections
        Divider(modifier = Modifier.padding(vertical = 16.dp))

        // Counter Section
        Text("Counter Demo", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        
        // Display current counter value
        Text("Counter: ${viewModel.counter}")
        
        // Button to increment counter
        Button(
            onClick = { viewModel.incrementCounter() },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Increment Counter")
        }
    }
}
