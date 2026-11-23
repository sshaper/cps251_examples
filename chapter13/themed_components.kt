package com.example.bookexamplesapp

// Core Android imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat

// Compose UI imports
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * MainActivity is the entry point of the application.
 * It sets up the basic window configuration and initializes the Compose UI.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Request dark status-bar icons (true means a light status-bar appearance).
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        setContent {
            ThemedComponentsExample()
        }
    }
}

/**
 * ThemedComponentsExample shows how individual components read the theme:
 * 1. TopAppBar colors with TopAppBarDefaults.topAppBarColors
 * 2. A FloatingActionButton with its own colors, shape and elevation, centered by the Scaffold
 * 3. An OutlinedTextField with border colors and an error message
 * 4. Icons tinted with theme roles
 *
 * Every color below comes from MaterialTheme.colorScheme, so light and dark mode both work.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemedComponentsExample() {
    val colors = if (isSystemInDarkTheme()) {
        darkColorScheme(
            primary = Color(0xFF80CBC4),
            onPrimary = Color.Black,
            secondary = Color(0xFFFFB74D),
            onSecondary = Color.Black,
            error = Color(0xFFEF9A9A),
            onError = Color.Black
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF00695C),
            onPrimary = Color.White,
            secondary = Color(0xFFEF6C00),
            onSecondary = Color.White,
            error = Color(0xFFC62828),
            onError = Color.White
        )
    }

    MaterialTheme(colorScheme = colors) {
        var plantName by remember { mutableStateOf("") }
        var plantTouched by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Plant Care") },
                    // Colors for the bar itself and for its title text
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        plantName = ""
                        plantTouched = false
                    },
                    // The button color and the icon color are a matching pair
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(Icons.Filled.Add, "Add plant")
                }
            },
            // The Scaffold places the button; the default is the end of the screen
            floatingActionButtonPosition = FabPosition.Center
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = plantName,
                    onValueChange = {
                        plantName = it
                        plantTouched = true
                    },
                    label = { Text("Plant name") },
                    // isError switches the field to the theme's error color
                    isError = plantName.isBlank() && plantTouched,
                    supportingText = {
                        if (plantName.isBlank() && plantTouched) {
                            Text("Plant name cannot be empty")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // tint colors the icon with a theme role
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favorite plant",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "Needs water",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/**
 * Preview composable for the ThemedComponentsExample.
 * This allows viewing the UI in Android Studio's preview pane without running the app.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ThemedComponentsPreview() {
    ThemedComponentsExample()
}
