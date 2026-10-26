package com.example.bookexamplesapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 * CPS251 - Chapter 9: Back Stack Demonstration
 *
 * A single-file teaching example extending the original backstack.kt.
 * Requires the androidx.navigation:navigation-compose dependency from Chapter 9.
 *
 * Try: Home -> Profile -> Settings -> Details, then Back several times.
 * The displayed history is an instructional route history, synchronized with
 * navigation actions in this example (not the NavController's internal stack).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme { BackStackChapter9Example() }
        }
    }
}

private val routes = listOf("home", "profile", "settings", "details")

@Composable
fun BackStackChapter9Example() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val currentEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentEntry?.destination?.route ?: "home"

    // Teaching display: keep an observable route history. NavController owns the
    // real back stack; this list is only a visualization of our example's actions.
    val history = remember { mutableStateListOf("home") }

    fun push(route: String, singleTop: Boolean = false) {
        if (route !in routes) return
        navController.navigate(route) { launchSingleTop = singleTop }
        if (!singleTop || history.lastOrNull() != route) history.add(route)
    }

    fun goBack() {
        if (navController.previousBackStackEntry != null) {
            if (navController.popBackStack() && history.size > 1) history.removeAt(history.lastIndex)
        } else {
            Toast.makeText(context, "Already at Home - no previous screen", Toast.LENGTH_SHORT).show()
        }
    }

    fun goUp() {
        if (navController.previousBackStackEntry != null) {
            if (navController.navigateUp() && history.size > 1) history.removeAt(history.lastIndex)
        } else {
            Toast.makeText(context, "No parent screen to navigate up to", Toast.LENGTH_SHORT).show()
        }
    }

    // popUpTo("profile") keeps Profile but removes all entries above it.
    fun popToProfile() {
        if (history.contains("profile")) {
            navController.navigate("profile") {
                popUpTo("profile") { inclusive = false }
                launchSingleTop = true
            }
            while (history.size > 1 && history.last() != "profile") history.removeAt(history.lastIndex)
        } else {
            Toast.makeText(context, "Visit Profile first", Toast.LENGTH_SHORT).show()
        }
    }

    // inclusive=true removes the target Profile as well; Settings becomes top.
    // We only enable this when there is a Profile beneath the current screen.
    fun removeThroughProfile() {
        val profileIndex = history.indexOfLast { it == "profile" }
        if (profileIndex >= 0) {
            navController.navigate("settings") {
                popUpTo("profile") { inclusive = true }
                launchSingleTop = true
            }
            while (history.size > profileIndex) history.removeAt(history.lastIndex)
            history.add("settings")
        } else {
            Toast.makeText(context, "Visit Profile first", Toast.LENGTH_SHORT).show()
        }
    }

    // Reset to Home and clear all destinations above Home.
    fun resetToHome() {
        navController.navigate("home") {
            popUpTo("home") { inclusive = false }
            launchSingleTop = true
        }
        history.clear()
        history.add("home")
    }

    // Intercept device Back, just like the original example.
    BackHandler { goBack() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Chapter 9 - Navigation Back Stack", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Current destination: ${currentRoute.uppercase()}")
        Spacer(Modifier.height(8.dp))
        Text("Back stack (bottom to top):", style = MaterialTheme.typography.titleMedium)
        history.forEachIndexed { index, route ->
            Text("${index + 1}. ${route.replaceFirstChar { it.uppercase() }}" +
                if (index == history.lastIndex) "  <-- TOP (visible)" else "")
        }
        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))

        // Each route renders a different screen, while the shared controls below
        // let students experiment with the stack from any destination.
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.weight(1f)
        ) {
            composable("home") { DemoScreen("Home", "Start destination of the app") }
            composable("profile") { DemoScreen("Profile", "A screen pushed onto Home") }
            composable("settings") { DemoScreen("Settings", "Try navigating onward to Details") }
            composable("details") { DemoScreen("Details", "Press Back to reveal the previous screen") }
        }

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text("Push a destination", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(onClick = { push("profile") }, modifier = Modifier.weight(1f)) { Text("Profile") }
                Button(onClick = { push("settings") }, modifier = Modifier.weight(1f)) { Text("Settings") }
                Button(onClick = { push("details") }, modifier = Modifier.weight(1f)) { Text("Details") }
            }
            Button(onClick = { push(currentRoute, singleTop = true) }, modifier = Modifier.fillMaxWidth()) {
                Text("Navigate to current screen (launchSingleTop)")
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                OutlinedButton(onClick = { goBack() }, modifier = Modifier.weight(1f)) { Text("popBackStack") }
                OutlinedButton(onClick = { goUp() }, modifier = Modifier.weight(1f)) { Text("navigateUp") }
            }
            OutlinedButton(onClick = { popToProfile() }, modifier = Modifier.fillMaxWidth()) {
                Text("popUpTo Profile (inclusive = false)")
            }
            OutlinedButton(onClick = { removeThroughProfile() }, modifier = Modifier.fillMaxWidth()) {
                Text("popUpTo Profile (inclusive = true)")
            }
            Button(onClick = { resetToHome() }, modifier = Modifier.fillMaxWidth()) {
                Text("Clear history above Home")
            }
            Text("Tip: Compare Home -> Profile -> Settings -> Details before and after each action.",
                style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun DemoScreen(title: String, explanation: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("$title Screen", style = MaterialTheme.typography.headlineMedium)
        Text(explanation, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BackStackChapter9Preview() {
    MaterialTheme { BackStackChapter9Example() }
}
