package com.example.bookexamplesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Chapter 9: demonstrate popBackStack, popUpTo, navigateUp, and clearBackStack.
 * Use this standalone file in place of another example's MainActivity.
 * Requires the Navigation Compose dependency from Introduction to Navigation.
 * The usual visible flow is Home -> Editor -> Details.
 * No custom BackHandler is needed for normal system Back navigation.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface {
                    BackStackApp()
                }
            }
        }
    }
}

@Composable
fun BackStackApp() {
    val navController = rememberNavController()
    // Feedback belongs above the destinations so navigation does not discard it.
    // This message is temporary UI state, not a history log or saved document.
    var status by remember { mutableStateOf("Choose an action to explore the back stack.") }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            BackStackHomeScreen(
                status = status,
                onOpenEditor = { navController.navigate("editor") },
                onRestoreEditor = {
                    // Restore a previously saved Editor entry if one exists.
                    // Without saved state, this opens a new Editor with empty text.
                    navController.navigate("editor") {
                        restoreState = true
                    }
                },
                onClearSavedEditor = {
                    // This discards a SAVED entry; Home remains the active screen.
                    // It neither clears the active history nor deletes a disk file.
                    val cleared = navController.clearBackStack("editor")
                    status = if (cleared) {
                        "Saved Editor state discarded. Restore now opens an empty Editor."
                    } else {
                        "No saved Editor state to discard."
                    }
                }
            )
        }
        composable("editor") {
            BackStackEditorScreen(
                onOpenDetails = { navController.navigate("details") },
                onSaveAndReturnHome = {
                    // Target Editor itself, include it in the pop, and save its state.
                    // Because inclusive is true, restore/clear can use the editor route.
                    // Home stays on the active stack beneath it.
                    navController.popBackStack("editor", inclusive = true, saveState = true)
                    status = "Editor state saved. Choose Restore saved Editor to reopen it."
                }
            )
        }
        composable("details") {
            BackStackDetailsScreen(
                onBack = {
                    // Home -> Editor -> Details becomes Home -> Editor.
                    navController.popBackStack()
                },
                onUp = {
                    // Same visible result as Back in this normal in-app flow.
                    // Up has different behavior for some deep-link/task entry paths.
                    navController.navigateUp()
                },
                onReturnHome = {
                    // Remove Editor and Details but keep the existing Home entry.
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                        // Home is now on top: reuse it rather than add another Home.
                        launchSingleTop = true
                    }
                },
                onReplaceHome = {
                    // Remove Home as well, then add a fresh Home entry.
                    // Named routes make the target explicit; 0 is not a stack index.
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
fun BackStackHomeScreen(
    status: String,
    onOpenEditor: () -> Unit,
    onRestoreEditor: () -> Unit,
    onClearSavedEditor: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding()
            .verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Home", style = MaterialTheme.typography.headlineSmall)
        Text("Flow to explore: Home -> Editor -> Details")
        Text(status)
        Button(onClick = onOpenEditor) { Text("Open new Editor") }
        Button(onClick = onRestoreEditor) { Text("Restore saved Editor") }
        Button(onClick = onClearSavedEditor) { Text("Clear saved Editor state") }
    }
}

@Composable
fun BackStackEditorScreen(onOpenDetails: () -> Unit, onSaveAndReturnHome: () -> Unit) {
    // rememberSaveable integrates this String with the destination's saved UI state.
    // Ordinary remember alone does not provide the saved-text demonstration.
    var note by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding()
            .verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Editor", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("A note to remember") }
        )
        Button(onClick = onOpenDetails) { Text("Open Details") }
        Button(onClick = onSaveAndReturnHome) { Text("Save Editor and return Home") }
        Text("Saving here retains navigation state; it does not write a document to disk.")
    }
}

@Composable
fun BackStackDetailsScreen(
    onBack: () -> Unit,
    onUp: () -> Unit,
    onReturnHome: () -> Unit,
    onReplaceHome: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding()
            .verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Details", style = MaterialTheme.typography.headlineSmall)
        Text("Compare each action by reopening Editor and Details from Home.")
        Button(onClick = onBack) { Text("Back: popBackStack") }
        Button(onClick = onUp) { Text("Up: navigateUp") }
        Button(onClick = onReturnHome) { Text("Home: keep existing entry") }
        Button(onClick = onReplaceHome) { Text("Home: replace entry") }
    }
}
