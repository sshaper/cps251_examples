package com.example.bookexamplesapp

// Core Android imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat

// Compose UI imports
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            CompleteThemeExample()
        }
    }
}

/**
 * CompleteThemeExample builds all three parts of a theme and gives them to MaterialTheme:
 * 1. A ColorScheme, chosen from the device's light or dark setting
 * 2. A Typography with a few customized text styles
 * 3. A Shapes with all five corner slots
 *
 * The card and buttons below read their colors, text styles and corners from the theme.
 */
@Composable
fun CompleteThemeExample() {
    val isDarkTheme = isSystemInDarkTheme()

    // 1. Color scheme: one complete set of roles for each mode
    val colors = if (isDarkTheme) {
        darkColorScheme(
            primary = Color(0xFF81C784),
            onPrimary = Color.Black,
            primaryContainer = Color(0xFF1B5E20),
            onPrimaryContainer = Color.White,
            secondary = Color(0xFFFFB74D),
            onSecondary = Color.Black,
            background = Color(0xFF101410),
            onBackground = Color.White,
            surface = Color(0xFF1C241C),
            onSurface = Color.White,
            error = Color(0xFFEF9A9A),
            onError = Color.Black
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF2E7D32),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFC8E6C9),
            onPrimaryContainer = Color.Black,
            secondary = Color(0xFFEF6C00),
            onSecondary = Color.White,
            background = Color(0xFFF1F8E9),
            onBackground = Color.Black,
            surface = Color.White,
            onSurface = Color.Black,
            error = Color(0xFFC62828),
            onError = Color.White
        )
    }

    // 2. Typography: start from the default styles and change only what you need.
    //    copy() returns a new TextStyle with the listed values replaced.
    val typography = Typography(
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, fontSize = 26.sp),
        titleMedium = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp),
        bodyMedium = MaterialTheme.typography.bodyMedium,
        labelSmall = MaterialTheme.typography.labelSmall
    )

    // 3. Shapes: every slot gets a RoundedCornerShape
    val shapes = Shapes(
        extraSmall = RoundedCornerShape(2.dp),
        small = RoundedCornerShape(6.dp),
        medium = RoundedCornerShape(10.dp),
        large = RoundedCornerShape(14.dp),
        extraLarge = RoundedCornerShape(24.dp)
    )

    // The three parts travel together into MaterialTheme
    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(top = 50.dp)
            ) {
                // Regular card: surface color, large corners
                Card(
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Today's Forecast",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Sunny with a light breeze all afternoon.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Updated 9:00 AM",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { /* TODO: Implement button action */ }) {
                            Text("Refresh")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Highlighted card: primaryContainer with its matching onPrimaryContainer text
                Card(
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Weather alert",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Pollen is high. primaryContainer with onPrimaryContainer text.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

/**
 * Preview composable for the CompleteThemeExample.
 * This allows viewing the UI in Android Studio's preview pane without running the app.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CompleteThemePreview() {
    CompleteThemeExample()
}
