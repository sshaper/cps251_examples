// Package declaration for the Android app
package com.example.bookexamplesapp

// Android framework imports
import android.os.Bundle

// Jetpack Compose Activity imports
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

// Jetpack Compose UI foundation imports
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

// Jetpack Compose Material Design 3 imports
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

// Jetpack Compose runtime and UI imports
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// AndroidX core imports for window management
import androidx.core.view.WindowCompat

/**
 * MainActivity serves as the entry point for the Android application.
 * This class extends ComponentActivity, which is the base class for activities
 * that use Jetpack Compose for their UI.
 * 
 * Key responsibilities:
 * - Initializes the app's main UI using Jetpack Compose
 * - Configures the status bar appearance for better user experience
 * - Sets up the Material Design theme for consistent styling
 * - Launches the main composable function (SectionedSettingsList)
 */
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is first created. This is where we:
     * 1. Call the parent class's onCreate method
     * 2. Configure the status bar to use light icons (dark icons on light background)
     * 3. Set up the Compose UI with MaterialTheme wrapper
     * 4. Launch our main composable function
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Configure the status bar to use dark icons for better visibility
        // This ensures the status bar icons are visible against light backgrounds
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        
        // Set up the Compose UI with MaterialTheme for consistent design
        setContent {
            MaterialTheme {
                // Launch our main composable that displays the settings list
                SectionedSettingsList()
            }
        }
    }
}

/**
 * SectionedSettingsList is the main composable function that creates a scrollable list
 * of settings organized into sections. This demonstrates how to create a settings screen
 * with grouped items, similar to what you'd find in most mobile apps.
 * 
 * Features:
 * - Uses LazyColumn for efficient scrolling of large lists
 * - Groups settings into logical sections (Account, App)
 * - Each section has a title and multiple setting items
 * - Items are clickable (though click handling is not implemented)
 * - Uses Material Design 3 components for modern UI
 */
@Composable
fun SectionedSettingsList() {
    // Define the settings data structure with sections and their items
    // This creates a hierarchical list: Sections contain multiple SettingItems
    val settings = listOf(
        SettingsSection(
            "Account",  // Section title
            listOf(
                SettingItem("Profile"),      // Individual setting item
                SettingItem("Privacy"),      // Individual setting item
                SettingItem("Notifications") // Individual setting item
            )
        ),
        SettingsSection(
            "App",      // Section title
            listOf(
                SettingItem("Theme"),        // Individual setting item
                SettingItem("Language"),     // Individual setting item
                SettingItem("About")         // Individual setting item
            )
        )
    )
    
    // LazyColumn provides efficient scrolling for lists of any size
    // It only renders items that are currently visible on screen
    LazyColumn {
        // Iterate through each settings section
        settings.forEach { section ->
            // Add section title as a header item
            item {
                Text(
                    text = section.title,
                    style = MaterialTheme.typography.titleMedium,  // Use Material Design typography
                    modifier = Modifier.padding(16.dp).padding(top=50.dp),  // Add spacing around text
                    color = MaterialTheme.colorScheme.primary     // Use primary color from theme
                )
            }
            
            // Add all items in this section using items() for efficient rendering
            items(section.items) { item ->
                ListItem(
                    headlineContent = { Text(item.title) },  // Display the setting item title
                    modifier = Modifier.clickable { /* Handle click */ }  // Make item clickable (placeholder)
                )
            }
            
            // Add a divider after each section for visual separation
            item {
                Divider(modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

/**
 * Data class representing a section of settings.
 * Each section has a title and a list of setting items.
 * 
 * @param title The name of the settings section (e.g., "Account", "App")
 * @param items List of SettingItem objects that belong to this section
 */
data class SettingsSection(val title: String, val items: List<SettingItem>)

/**
 * Data class representing an individual setting item.
 * Each setting item has a title that will be displayed in the UI.
 * 
 * @param title The display name of the setting (e.g., "Profile", "Theme")
 */
data class SettingItem(val title: String)

/**
 * Preview function for the SectionedSettingsList composable.
 * This allows developers to see how the UI looks in Android Studio's
 * design view without running the full app.
 * 
 * @param showBackground Whether to show the background in the preview
 * @param showSystemUi Whether to show system UI elements (status bar, navigation bar)
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyScreenPreview() {
    // Display the SectionedSettingsList in the preview
    SectionedSettingsList()
}

