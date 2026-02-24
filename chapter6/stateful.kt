
// Android framework imports
import android.os.Bundle
import androidx.core.view.WindowCompat

// Activity imports
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

// Compose UI imports
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * MainActivity serves as the entry point of the Android application.
 * It initializes the app's UI using Jetpack Compose and configures the window appearance.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure the status bar to use dark icons for better visibility
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        // Set up the Compose UI with MaterialTheme
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

/**
 * MainScreen is the root composable that serves as the container for the app's content.
 * It provides the basic layout structure and spacing for the app's UI elements.
 *
 * @param modifier Optional Modifier to customize the layout behavior
 */
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()  // Make the column take full width
            .padding(top = 50.dp)  // Add top margin for spacing from system UI
    ) {
        EditableProfile()
    }
}

/**
 * ProfileInfo is a stateless composable that displays user information.
 * It follows the principle of separation of concerns by only handling display logic.
 *
 * @param name The user's name to display
 * @param age The user's age to display (as string)
 */
@Composable
fun ProfileInfo(name: String, age: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Name: $name",
            style = TextStyle(fontSize = 24.sp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Age: $age",
            style = TextStyle(fontSize = 20.sp)
        )
    }
}

/**
 * EditableProfile is a stateful composable that implements a profile editing system.
 * It demonstrates several key Compose concepts:
 * 1. State Management: Uses state for editing mode and profile data (name, age)
 * 2. Unidirectional Data Flow: State is held here and passed down to ProfileInfo and the text fields
 * 3. User Interaction: A button toggles edit mode; when editing, name and age are edited in place
 *
 * State Variables:
 * - isEditing: Controls whether the profile is in edit mode (shows/hides the text fields)
 * - name: The current name (displayed and edited directly in the form)
 * - age: The current age as string (displayed and edited directly in the form)
 */
@Composable
fun EditableProfile() {
    // State management for editing mode
    var isEditing by remember { mutableStateOf(false) }

    // State management for profile data
    var name by remember { mutableStateOf("John") }
    var age by remember { mutableStateOf("20") }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display current profile information
        ProfileInfo(name = name, age = age)

        Spacer(modifier = Modifier.height(16.dp))

        // Button toggles edit mode; label shows "Save" when editing, "Edit" when not
        Button(onClick = {
           isEditing = !isEditing
        }) {
            Text(if (isEditing) "Save" else "Edit")
        }

        // Conditional rendering of edit form
        if (isEditing) {
            Spacer(modifier = Modifier.height(16.dp))

            // Name input field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Age input field
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Preview composable for testing the UI in Android Studio's preview pane.
 * Shows the entire MainScreen with the profile editing functionality.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewFlow() {
    MainScreen()
}