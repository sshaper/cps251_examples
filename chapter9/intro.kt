package com.example.navdemo

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navdemo.ui.theme.NavDemoTheme

// Chapter 9: two composable screens hosted by one Activity.
// AppNav owns navigation; the screens receive callbacks for their buttons.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Screen layouts apply systemBarsPadding so controls remain clear of system bars.
        enableEdgeToEdge()
        setContent {
            MaterialTheme{
                Surface {
                    // Place the navigation host inside the Activity's Compose UI.
                    AppNav()
                }
            }

        }
    }
}

@Composable
fun AppNav(){
    // Remember the controller across recompositions rather than creating a new
    // navigation controller whenever Compose runs this function again.
    val navController = rememberNavController()
    // This graph registers two destinations. Home is displayed when it first opens.
    // The host connects the graph to the controller and displays the current destination.
    NavHost(navController = navController, startDestination = "home"){
        // Route strings identify destinations; navigate() must use a matching name.
        composable("home") {
            // Supply the action here so HomeScreen only needs to report a button click.
            HomeScreen(onNavigateToProfile = {
                // Add Profile above Home in the back stack, leaving Home available to return to.
                navController.navigate("profile") {
                    // Avoid another Profile entry only if Profile is already on top.
                    // This does not remove duplicate destinations elsewhere in the stack.
                    launchSingleTop = true

                }

            })
        }
        composable("profile"){
            ProfileScreen(onBack = {
                // Remove Profile to reveal the existing Home destination.
                // navigate("home") would normally add a new Home entry instead.
                navController.popBackStack()
            })

        }
    }
}

@Composable
// The callback takes no arguments and returns no value. AppNav supplies its behavior;
// this screen does not need a NavController or knowledge of route names.
fun HomeScreen(onNavigateToProfile: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            // Keep content outside the system bars in the edge-to-edge Activity.
            .systemBarsPadding(),
        horizontalAlignment = Alignment.Start
    ) {
        // Pass the callback to onClick; it runs when the user taps, not during composition.
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

@Composable
// Home is the previous destination in this example. The callback goes back;
// it does not independently search for a destination named "home".
fun ProfileScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.Start
        ){
        // AppNav connects this event to popBackStack(). Navigation Compose also
        // handles system Back from Profile without a custom BackHandler here.
        Button(onClick = onBack) {
            Text("Back to Home")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "This is the profile page",
            modifier = Modifier.padding(16.dp)
        )

    }
}
