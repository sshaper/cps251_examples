// Main package for the app
package com.example.bookexamplesapp

// Android and Jetpack Compose imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

/**
 * MainActivity is the entry point of the app.
 * - Sets up the Compose UI and applies the MaterialTheme.
 * - Configures the status bar for better visibility.
 * - Displays a combined example screen with custom components.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set status bar icons to dark for visibility on light backgrounds
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        // Set up the Compose UI with MaterialTheme
        setContent {
            MaterialTheme {
                CombinedExampleScreen()
            }
        }
    }
}

// --- Custom Button Example ---
/**
 * A reusable custom button with consistent styling.
 * @param text The button label.
 * @param onClick The action to perform when clicked.
 * @param modifier Modifier for styling and layout.
 * @param enabled Whether the button is enabled.
 */
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}



// --- Loading Spinner Example ---
/**
 * A reusable loading spinner with a message.
 * @param message The message to display below the spinner.
 * @param modifier Modifier for styling and layout.
 */
@Composable
fun LoadingSpinner(
    message: String = "Loading...",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


// --- Custom Text Field Example ---
/**
 * A custom text field with error handling.
 * @param value The current text value.
 * @param onValueChange Callback for text changes.
 * @param label The label to display.
 * @param modifier Modifier for styling and layout.
 * @param isError Whether the field is in an error state.
 * @param errorMessage The error message to display (if any).
 * @param singleLine Whether the text field is single line.
 */
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = isError,
            singleLine = singleLine,
            modifier = Modifier.fillMaxWidth()
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}



// --- Info Card Example ---
/**
 * A card displaying an icon, title, and description.
 * @param title The card's title.
 * @param description The card's description.
 * @param icon The icon to display.
 * @param onClick Optional click handler for the card.
 * @param modifier Modifier for styling and layout.
 */
@Composable
fun InfoCard(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}






// --- Expandable Card Example ---
/**
 * A card that can expand/collapse to show or hide content.
 * @param title The card's title.
 * @param content The content to show when expanded.
 * @param modifier Modifier for styling and layout.
 */
@Composable
fun ExpandableCard(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

// --- Component Library Example ---
/**
 * Example of organizing reusable components in an object.
 */
object MyComponents {
    /**
     * A primary button for the app.
     */
    @Composable
    fun PrimaryButton(
        text: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Button(
            onClick = onClick,
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text)
        }
    }
    /**
     * An info box for displaying messages.
     */
    @Composable
    fun InfoBox(
        title: String,
        message: String,
        modifier: Modifier = Modifier
    ) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// --- Combined Example Screen ---
/**
 * This screen demonstrates all the custom components working together.
 * Students can see how to compose and reuse UI building blocks.
 */
@Composable
fun CombinedExampleScreen() {
    // State for the email field
    var email by remember { mutableStateOf("") }
    // State for email error
    var emailError by remember { mutableStateOf(false) }
    // State for loading spinner
    var loading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp), // Extra top padding for visibility
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Custom Components Demo",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        // Custom Button
        CustomButton(
            text = if (loading) "Loading..." else "Submit",
            onClick = {
                emailError = email.isBlank() || !email.contains("@")
                if (!emailError) {
                    loading = true
                    coroutineScope.launch {
                        delay(5000L) // It's good practice to use 'L' for Long
                        loading = false
                    }
                } // <-- This closing brace was also missing
            },
            enabled = !loading
        )

        // Loading Spinner (shows only if loading)
        if (loading) {
            LoadingSpinner(
                message = "Submitting...",
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Custom Text Field
        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            isError = emailError,
            errorMessage = if (emailError) "Please enter a valid email" else null
        )

        // Info Card
        InfoCard(
            title = "Weather",
            description = "Partly cloudy, 72°F",
            icon = Icons.Default.Cloud
        )


        // Expandable Card
        ExpandableCard(
            title = "How to use this app",
            content = "This app helps you organize your tasks and stay productive. Tap the + button to add new tasks, and swipe to delete completed ones."
        )
        // Component Library usage
        MyComponents.PrimaryButton(
            text = "Save",
            onClick = { /* Save action */ }
        )
        MyComponents.InfoBox(
            title = "Note",
            message = "Your changes have been saved successfully."
        )
    

        
    }
}

/**
 * Preview for Android Studio's design view.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyScreenPreview() {
    CombinedExampleScreen()
}