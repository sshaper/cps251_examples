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
                onOpenEditor = {
                    navController.navigate("editor")
                    status = "Opened a new, empty Editor. No saved Editor state was restored."
                },
                onRestoreEditor = {
                    // Restore a previously saved Editor entry if one exists.
                    // Without saved state, this opens a new Editor with empty text.
                    navController.navigate("editor") {
                        restoreState = true
                    }
                    status = "Requested saved Editor state. If a saved entry existed, its note was restored; otherwise this is an empty Editor."
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
                status = status,
                onOpenDetails = {
                    navController.navigate("details")
                    status = "Opened Details above Editor. Editor remains in the active stack, so Back or Up can return to its note."
                },
                onSaveAndReturnHome = {
                    // Target Editor itself, include it in the pop, and save its state.
                    // Because inclusive is true, restore/clear can use the editor route.
                    // Home stays on the active stack beneath it.
                    navController.popBackStack("editor", inclusive = true, saveState = true)
                    status = "Removed Editor from the active stack and saved its state. Home remains. Use Restore saved Editor to recover the note."
                }
            )
        }
        composable("details") {
            BackStackDetailsScreen(
                status = status,
                onBack = {
                    // Home -> Editor -> Details becomes Home -> Editor.
                    navController.popBackStack()
                    status = "Removed Details only. Returned to the existing Editor entry with its note."
                },
                onUp = {
                    // Same visible result as Back in this normal in-app flow.
                    // Up has different behavior for some deep-link/task entry paths.
                    navController.navigateUp()
                    status = "Up removed Details in this flow. The existing Editor entry and its note remain."
                },
                onReturnHome = {
                    // Remove Editor and Details but keep the existing Home entry.
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                        // Home is now on top: reuse it rather than add another Home.
                        launchSingleTop = true
                    }
                    status = "Kept HOME only. Removed Editor and Details without saving the current Editor note. Keeping Home does not keep Editor."
                },
                onReplaceHome = {
                    // Remove Home as well, then add a fresh Home entry.
                    // Named routes make the target explicit; 0 is not a stack index.
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                    status = "Removed the old Home, Editor, and Details, then created a new Home. The current Editor note was not saved."
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
        Text("Last button action: $status")
        Button(onClick = onOpenEditor) { Text("Open new Editor") }
        Text("Adds a new Editor with an empty note. This does not restore a previously saved copy.")
        Button(onClick = onRestoreEditor) { Text("Restore saved Editor") }
        Text("Reopens the saved Editor and its note, if one exists. Restoring brings that saved entry back into the active stack; it is not a permanent backup. If nothing is saved, opens an empty Editor.")
        Button(onClick = onClearSavedEditor) { Text("Clear saved Editor state") }
        Text("Discards a previously saved Editor copy. Home stays on screen. Restore cannot recover that copy afterward.")
    }
}

@Composable
fun BackStackEditorScreen(
    status: String,
    onOpenDetails: () -> Unit,
    onSaveAndReturnHome: () -> Unit
) {
    // rememberSaveable integrates this String with the destination's saved UI state.
    // Ordinary remember alone does not provide the saved-text demonstration.
    var note by rememberSaveable { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().systemBarsPadding()
            .verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Editor", style = MaterialTheme.typography.headlineSmall)
        Text("Last button action: $status")
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("A note to remember") }
        )
        Button(onClick = onOpenDetails) { Text("Open Details") }
        Text("Adds Details above this Editor. Back or Up returns to this note. A Details-to-Home action removes this Editor without saving it.")
        Button(onClick = onSaveAndReturnHome) { Text("Save Editor state and return Home") }
        Text("Removes Editor from the active stack but sets its state aside. On Home, choose Restore saved Editor to recover this note. This does not write a document to disk.")
    }
}

@Composable
fun BackStackDetailsScreen(
    status: String,
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
        Text("Last button action: $status")
        Text("The active path is Home -> Editor -> Details. Keeping Home does not keep the Editor note.")
        Button(onClick = onBack) { Text("Back: popBackStack") }
        Text("Removes Details only. Returns to the existing Editor with its note intact: Home -> Editor.")
        Button(onClick = onUp) { Text("Up: navigateUp") }
        Text("In this example, also removes Details only and returns to the existing Editor and note. Up can differ from Back for other app entry paths.")
        Button(onClick = onReturnHome) { Text("Home: keep Home, discard Editor") }
        Text("Keeps the existing HOME entry, not Editor. Removes Editor and Details without saving the current note. To retain this note, go Back to Editor and use Save Editor state and return Home instead.")
        Button(onClick = onReplaceHome) { Text("Home: replace Home, discard Editor") }
        Text("Removes the old Home as well as Editor and Details, then creates a new Home. The current Editor note is not saved. The difference from the button above is which Home entry remains.")
    }
}
