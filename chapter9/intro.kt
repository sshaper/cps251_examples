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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme{
                Surface {
                    AppNav()
                }
            }

        }
    }
}

@Composable
fun AppNav(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home"){
        composable("home") {
            HomeScreen(onNavigateToProfile = {
                navController.navigate("profile") {
                    launchSingleTop = true

                }

            })
        }
        composable("profile"){
            ProfileScreen(onBack = {
                navController.popBackStack()
            })

        }
    }
}

@Composable
fun HomeScreen(onNavigateToProfile: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.Start
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

@Composable
fun ProfileScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        horizontalAlignment = Alignment.Start
        ){
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