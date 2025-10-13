package com.example.bookexamplesapp

// Core Android imports
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat

// Compose UI imports
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Navigation imports
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * MainActivity is the entry point of the application.
 * It sets up the basic window configuration and initializes the Compose UI.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure the window to use light status bar icons
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true

        // Set up the Compose UI
        setContent {
            MaterialTheme {
                Surface {
                    // The NavController should be created inside a Composable context
                    val navController = rememberNavController()
                    SimpleNavigationExample()
                }
            }
        }
    }
}

/**
 * SimpleNavigationExample sets up the navigation structure of the app.
 * It uses Jetpack Compose Navigation to manage screen transitions between Home and Profile screens.
 */
@Composable
fun SimpleNavigationExample() {
    val navController = rememberNavController()
    val context = LocalContext.current // Get context for the Toast

    // 2. Add the BackHandler to intercept the system back button.
    BackHandler {
        // Check if there is a screen to go back to.
        if (navController.previousBackStackEntry != null) {
            navController.popBackStack()
        } else {
            // If on the start destination, show a toast instead of exiting.
            Toast.makeText(context, "Nothing more to go back to!", Toast.LENGTH_SHORT).show()
        }
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") { it -> // This 'it ->' is the required fix
            HomeScreen(
                onNavigateToProfile = {
                    navController.navigate("profile") {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("profile") { it -> // This 'it ->' is also required
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}





/**
 * HomeScreen displays the main screen of the application.
 * It contains a button to navigate to the profile screen and a text message.
 */
@Composable
fun HomeScreen(onNavigateToProfile: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onNavigateToProfile) {
                Text("Go to Profile")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "This is the home page",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

/**
 * ProfileScreen displays the profile screen of the application.
 * It contains a button to navigate back to the home screen and a text message.
 */
@Composable
fun ProfileScreen(onNavigateBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onNavigateBack) {
                Text("Back to Home")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "This is the profile page",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

/**
 * Preview function to see the UI in Android Studio's preview pane.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewsFeedExamplePreview() {
    MaterialTheme {
        SimpleNavigationExample()
    }
}
