package com.example.bookexamplesapp

// Core Android imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat

// Compose UI imports
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
            ThemeAnimationExample()
        }
    }
}

// A small city card: the id identifies it in the list, isFavorite drives the animation
data class City(
    val id: Int,
    val name: String,
    val details: String,
    val isFavorite: Boolean
)

/**
 * ThemeAnimationExample animates between theme colors instead of fixed colors.
 * A city that becomes a favorite animates from the surface color to primaryContainer,
 * so the same code works in light mode and in dark mode.
 */
@Composable
fun ThemeAnimationExample() {
    val colors = if (isSystemInDarkTheme()) {
        darkColorScheme(
            primary = Color(0xFF80CBC4),
            primaryContainer = Color(0xFF004D40),
            onPrimaryContainer = Color.White,
            secondary = Color(0xFFFFB74D),
            background = Color(0xFF101414),
            surface = Color(0xFF1C2222),
            onSurface = Color.White,
            error = Color(0xFFEF9A9A)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF00695C),
            primaryContainer = Color(0xFFB2DFDB),
            onPrimaryContainer = Color.Black,
            secondary = Color(0xFFEF6C00),
            background = Color(0xFFF0F5F5),
            surface = Color.White,
            onSurface = Color.Black,
            error = Color(0xFFC62828)
        )
    }

    MaterialTheme(colorScheme = colors) {
        val cities = remember {
            mutableStateListOf(
                City(1, "Lisbon", "Trams, tiled streets and pastel de nata.", false),
                City(2, "Kyoto", "Temples, gardens and quiet side streets.", true),
                City(3, "Reykjavik", "Hot springs and long summer evenings.", false)
            )
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(top = 50.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cities, key = { it.id }) { city ->
                    AnimatedCityCard(
                        city = city,
                        onToggleFavorite = {
                            val index = cities.indexOfFirst { it.id == city.id }
                            cities[index] = city.copy(isFavorite = !city.isFavorite)
                        },
                        onRemove = { cities.remove(city) },
                        // animateItem animates the card sliding when its neighbors change
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

/**
 * AnimatedCityCard combines four animations:
 * 1. animateColorAsState moves the card between two theme colors
 * 2. animateDpAsState moves the elevation
 * 3. animateContentSize smooths the height change when the content appears
 * 4. AnimatedVisibility expands and shrinks the city details
 */
@Composable
fun AnimatedCityCard(
    city: City,
    onToggleFavorite: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    // The targets are theme roles, so they change with light and dark mode
    val cardColor by animateColorAsState(
        targetValue = if (city.isFavorite) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        animationSpec = tween(durationMillis = 300),
        label = "cardColor"
    )
    val cardElevation by animateDpAsState(
        targetValue = if (city.isFavorite) 8.dp else 2.dp,
        animationSpec = tween(durationMillis = 300),
        label = "cardElevation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(animationSpec = tween(durationMillis = 300)),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = city.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            // enter and exit choose how the content grows and shrinks
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
            ) {
                Text(
                    text = city.details,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Toggle favorite",
                        tint = if (city.isFavorite) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Remove city",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/**
 * Preview composable for the ThemeAnimationExample.
 * This allows viewing the UI in Android Studio's preview pane without running the app.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ThemeAnimationPreview() {
    ThemeAnimationExample()
}
