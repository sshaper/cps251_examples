package com.example.bookexamplesapp

import android.R.attr.name
import android.R.attr.onClick
import android.R.id.home
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.navdemo.ui.theme.NavDemoTheme

// Chapter 9: send a name from Home to Profile using a required route argument.
// Use this standalone example in a project with Navigation Compose configured.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme{
                Surface {
                    // Keep navigation wiring above the screens that report user events.
                    AppNav()
                }
            }

        }
    }
}

@Composable
fun AppNav(){
    // Reuse the controller across recompositions; Home is the initial destination.
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home"){
        composable("home") {
            // Home supplies a String to this callback; it is the entered name.
            HomeScreen(onNavigateToProfile = {
                // $it is a Kotlin string template: insert the callback's name into the text.
                // For Alex, build profile/Alex to match profile/{userName}.
                // Kotlin builds the string; Navigation matches and parses it at runtime.
                // This teaching version inserts raw text. Unrestricted values need
                // Uri.encode(value) before insertion because /, ?, and # affect routes.
                navController.navigate("profile/$it") {
                    // Only prevent another copy when this destination is already on top.
                    launchSingleTop = true

                }

            })
        }
        composable(
            // The placeholder and argument key below must use the same spelling.
            route = "profile/{userName}",
            arguments = listOf(
                // Required text argument: no default or nullable option is declared.
                navArgument("userName") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            // This entry holds the parsed arguments for this visit to Profile.
            // Bundle/getString access is nullable even though the route requires a name.
            val username = backStackEntry.arguments?.getString("userName")

            ProfileScreen(
                userName = username,
                // Reveal the existing Home entry rather than navigating to a new Home.
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun HomeScreen(onNavigateToProfile: (String) -> Unit) {
    // Local input state drives the field and button. It is not shared profile state
    // and remember alone does not promise restoration after Activity recreation.
    var name by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.Start
    ) {
        // Each edit changes state, allowing Compose to update the field and enabled state.
        OutlinedTextField(
            value = name,
            onValueChange = {name = it},
            label = { Text("Your Name") }
        )

        // Send the current text only when tapped. isNotBlank rejects empty/whitespace
        // input; it does not trim text or escape route characters.
        Button(onClick = { onNavigateToProfile(name) },enabled = name.isNotBlank()) {

            Text("Go to Profile")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "This is the home page",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
// The screen receives a value, not a controller or a live link to Home's text state.
// Normal navigation supplies a name; a null passed directly here would display "null".
fun ProfileScreen(userName: String?, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.Start
        ){

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back to Home")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Hello, $userName, This is the profile page",
            modifier = Modifier.padding(16.dp)
        )

    }
}