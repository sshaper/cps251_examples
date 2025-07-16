package com.example.bookexamplesapp

// Android framework imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

// Compose layout imports
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer

// Material Design 3 imports
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState

// Compose runtime imports
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// Compose UI imports
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Android system imports
import androidx.core.view.WindowCompat
import com.example.bookexamplesapp.ui.theme.BookExamplesAppTheme

/**
 * MainActivity - The entry point of the application
 * This activity demonstrates the use of Bottom Sheets and Dialogs in Jetpack Compose
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Configure the status bar to use dark icons for better visibility
        // This ensures the status bar icons are visible against light backgrounds
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        
        // Set up the Compose UI with MaterialTheme
        setContent {
            MaterialTheme {
                MyScreen()
            }
        }
    }
}


/**
 * MyScreen - Main screen composable that demonstrates Bottom Sheets and Dialogs
 * 
 * This screen contains:
 * - Two buttons to trigger different UI overlays
 * - State management for showing/hiding overlays
 * - Integration between bottom sheet and dialog
 */
@Composable
fun MyScreen() {
    // State variables to control the visibility of overlays
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showOptionsSheet by remember { mutableStateOf(false) }

    // Main content column with buttons
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp) // Extra top padding to avoid status bar
    ) {
        // Button to show the options bottom sheet
        Button(onClick = { showOptionsSheet = true }) {
            Text("Show Options")
        }

        // Button to show the delete confirmation dialog directly
        Button(onClick = { showDeleteDialog = true }) {
            Text("Delete Item")
        }
    }

    // Conditional rendering of the options bottom sheet
    // This demonstrates how to show a modal bottom sheet
    if (showOptionsSheet) {
        OptionsBottomSheet(
            onDismiss = { showOptionsSheet = false },
            onOptionSelected = { option ->
                // Handle the selected option from the bottom sheet
                when (option) {
                    "Delete" -> showDeleteDialog = true // Chain to dialog
                    "Edit" -> { /* Handle edit action */ }
                    "Share" -> { /* Handle share action */ }
                }
            }
        )
    }

    // Conditional rendering of the delete confirmation dialog
    // This demonstrates how to show an alert dialog
    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                // Handle the actual deletion here
                // For this example, we just close the dialog
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}


/**
 * OptionsBottomSheet - A modal bottom sheet that displays a list of action options
 * 
 * This composable demonstrates:
 * - How to create a modal bottom sheet
 * - How to handle option selection
 * - How to automatically dismiss the sheet after selection
 * 
 * @param onDismiss Callback when the sheet should be dismissed
 * @param onOptionSelected Callback when an option is selected, receives the option name
 */
@OptIn(ExperimentalMaterial3Api::class) // Required for ModalBottomSheet
@Composable
fun OptionsBottomSheet(
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit
) {
    // Create a modal bottom sheet that slides up from the bottom
    ModalBottomSheet(
        onDismissRequest = onDismiss, // Called when user taps outside or swipes down
        sheetState = rememberModalBottomSheetState() // Manages the sheet's state
    ) {
        // Content inside the bottom sheet
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header text for the bottom sheet
            Text(
                text = "Choose an Option",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Create a button for each option
            listOf("Edit", "Share", "Delete", "Report").forEach { option ->
                TextButton(
                    onClick = { 
                        // When an option is selected:
                        onOptionSelected(option) // Notify parent of selection
                        onDismiss() // Close the bottom sheet
                    },
                    modifier = Modifier.fillMaxWidth() // Make button full width
                ) {
                    Text(option)
                }
            }
            
            // Add some bottom spacing for better visual appearance
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * DeleteConfirmationDialog - A confirmation dialog for destructive actions
 * 
 * This composable demonstrates:
 * - How to create an alert dialog
 * - How to handle confirmation and cancellation
 * - Best practices for destructive action confirmation
 * 
 * @param onConfirm Callback when user confirms the deletion
 * @param onDismiss Callback when user cancels or dismisses the dialog
 */
@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    // Create an alert dialog that appears in the center of the screen
    AlertDialog(
        onDismissRequest = onDismiss, // Called when user taps outside the dialog
        title = { Text("Delete Item") }, // Dialog title
        text = { Text("Are you sure you want to delete this item?") }, // Dialog message
        confirmButton = {
            // Button that confirms the action (destructive action)
            TextButton(onClick = onConfirm) {
                Text("Delete")
            }
        },
        dismissButton = {
            // Button that cancels the action (safe action)
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Preview function for MyScreen
 * This allows us to see the UI in Android Studio's preview pane
 * without running the app
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyScreenPreview() {
    MyScreen()
}

