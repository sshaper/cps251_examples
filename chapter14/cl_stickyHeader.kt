// Main package for the app
package com.example.bookexamplesapp

// Android and Jetpack Compose imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

/**
 * MainActivity is the entry point of the app.
 * - Sets up the Compose UI and applies the MaterialTheme.
 * - Configures the status bar for better visibility.
 * - Displays a contact list with sticky headers for each starting letter.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set status bar icons to dark for visibility on light backgrounds
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = true
        // Set up the Compose UI with MaterialTheme
        setContent {
            MaterialTheme {
                ContactListWithStickyHeaders()
            }
        }
    }
}

/**
 * Displays a scrollable, alphabetically grouped contact list with sticky headers.
 * - Uses LazyColumn for efficient scrolling.
 * - Each group is headed by a bold, large letter that sticks to the top.
 * - The list is padded to avoid overlap with the system status bar/camera cutout.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactListWithStickyHeaders() {
    // Group contacts by their first letter (uppercase)
    val contacts = listOf(
        // ... (many contacts for scrolling test)
        Contact("Alice"),
        Contact("Aaron"),
        Contact("Adam"),
        Contact("Amelia"),
        Contact("Andrew"),
        Contact("Anna"),
        Contact("Anthony"),
        Contact("Ava"),
        Contact("Bob"),
        Contact("Benjamin"),
        Contact("Bella"),
        Contact("Brandon"),
        Contact("Brooke"),
        Contact("Charlie"),
        Contact("Chloe"),
        Contact("Christopher"),
        Contact("Claire"),
        Contact("Caleb"),
        Contact("David"),
        Contact("Daniel"),
        Contact("Diana"),
        Contact("Dylan"),
        Contact("Daisy"),
        Contact("Eve"),
        Contact("Emma"),
        Contact("Ethan"),
        Contact("Emily"),
        Contact("Evan"),
        Contact("Elizabeth"),
        Contact("Frank"),
        Contact("Faith"),
        Contact("Frederick"),
        Contact("Fiona"),
        Contact("George"),
        Contact("Grace"),
        Contact("Gabriel"),
        Contact("Georgia"),
        Contact("Gavin"),
        Contact("Hannah"),
        Contact("Henry"),
        Contact("Harper"),
        Contact("Harrison"),
        Contact("Hailey"),
        Contact("Ian"),
        Contact("Isabella"),
        Contact("Isaac"),
        Contact("Ivy"),
        Contact("Jack"),
        Contact("Julia"),
        Contact("James"),
        Contact("Jasmine"),
        Contact("Jacob"),
        Contact("Katherine"),
        Contact("Kevin"),
        Contact("Kayla"),
        Contact("Kyle"),
        Contact("Liam"),
        Contact("Lily"),
        Contact("Lucas"),
        Contact("Lucy"),
        Contact("Logan"),
        Contact("Mia"),
        Contact("Mason"),
        Contact("Madison"),
        Contact("Michael"),
        Contact("Maya"),
        Contact("Noah"),
        Contact("Natalie"),
        Contact("Nathan"),
        Contact("Nicole"),
        Contact("Oliver"),
        Contact("Olivia"),
        Contact("Owen"),
        Contact("Paige"),
        Contact("Peter"),
        Contact("Penelope"),
        Contact("Patrick"),
        Contact("Quinn"),
        Contact("Quentin"),
        Contact("Rachel"),
        Contact("Ryan"),
        Contact("Riley"),
        Contact("Robert"),
        Contact("Sophia"),
        Contact("Samuel"),
        Contact("Sarah"),
        Contact("Sebastian"),
        Contact("Scarlett"),
        Contact("Thomas"),
        Contact("Taylor"),
        Contact("Tyler"),
        Contact("Tiffany"),
        Contact("Ulysses"),
        Contact("Uma"),
        Contact("Victoria"),
        Contact("Vincent"),
        Contact("Valentina"),
        Contact("Victor"),
        Contact("William"),
        Contact("Willow"),
        Contact("Wyatt"),
        Contact("Wendy"),
        Contact("Xavier"),
        Contact("Xena"),
        Contact("Yasmine"),
        Contact("Yusuf"),
        Contact("Zoe"),
        Contact("Zachary"),
        Contact("Zara")
    ).groupBy { it.name.first().uppercase() }

    LazyColumn(
        // Use system insets to ensure content is not under the status bar/camera
        modifier = Modifier.padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        contacts.forEach { (letter, contactList) ->
            stickyHeader {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = letter,
                        // Make the header letter bold and large
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            // Render each contact in the current group
            items(contactList) { contact ->
                ListItem(
                    headlineContent = { Text(contact.name) },
                    modifier = Modifier.clickable { /* Handle contact selection */ }
                )
            }
        }
    }
}

/**
 * Data class representing a contact.
 * @param name The contact's display name.
 */
data class Contact(val name: String)

/**
 * Preview for Android Studio's design view.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyScreenPreview() {
    ContactListWithStickyHeaders()
}

